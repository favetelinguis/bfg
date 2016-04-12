(ns bfg.impl.web-server
  (:require
   [ring.middleware.resource :refer (wrap-resource)]
   [ring.middleware.content-type :refer (wrap-content-type)]
   [ring.middleware.not-modified :refer (wrap-not-modified)]
   [immuconf.config :as immuconf]
   [immutant.web             :as web]
   [immutant.web.async       :as async]
   [immutant.web.middleware  :as web-middleware]
   [environ.core             :refer (env)]

   [bfg.betfair.betting :as betting]
   [compojure.route          :as route]
   [compojure.core           :refer (rfn ANY GET routes)]
   [cheshire.core :as c]
   [ring.util.response       :refer (response redirect resource-response content-type)]

   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   ))

;; Needs to return a string
(defmulti message-handler :type)

(def setup-routes
  (routes
   (GET "/" []
     (-> (resource-response "index.html" {:root "public"})
         (content-type "text/html")))
   (GET "/*" [] (redirect "/"))))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open   (fn [channel]
                ;; start a go block and register a channel, how to handle
                ;; when the same channel reconect during a heartbeat?
                ;; we dont want on close to kill the go block and we loose all state
                ;; if we dont respond to x heartbeats we kill the go block??
                (async/send! channel (c/generate-string
                                      {:event "action"
                                       :payload {:type "SET_EVENT_TYPES"
                                                 :state {
                                                         :eventTypes (->>
                                                                      @(betting/list-event-types! "bla")
                                                                      (map #(assoc-in % [:eventType :id] (str (gensym "id")))))
                                                         }}})))

   :on-close   (fn [channel {:keys [code reason]}]
                 (println "close code:" code "reason:" reason))
   :on-message (fn [ch m]
                 (async/send! ch (c/generate-string
                                  (message-handler
                                   (c/parse-string m true)))))})

(defn handler [routes]
  (-> routes
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)
      (web-middleware/wrap-session {:timeout 20})
      ;; wrap the handler with websocket support
      ;; websocket requests will go to the callbacks, ring requests to the handler
      (web-middleware/wrap-websocket websocket-callbacks)))

(defrecord WebServer [options server sente]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting web-server at port: " (get options :port))
    (let [app (handler setup-routes)
          server (web/run app options)]
      (assoc component
             :server server)))
  (stop [component]
    (timbre/info "Stopping web-server")
    (when server
      (web/stop server)
      component)))

(defn new-web-server
  ([options]
   (map->WebServer {:options {:host (or (get options :host) "0.0.0.0")
                              :port (or (get options :port) 0)}})))

(defmethod message-handler "betting/listEventTypes"
  [{:keys (msg)}]
  {:type "event-types"
   :state {:eventTypes @(betting/list-event-types! "dont care")}})
