(ns bfg.impl.web-server
  (:require
   [immutant.web             :as web]
   [immutant.web.async       :as async]
   [immutant.web.middleware  :as web-middleware]

   [compojure.route          :as route]
   [compojure.core           :refer (ANY GET defroutes)]
   [ring.util.response       :refer (response redirect resource-response content-type)]

   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]

   [bfg.message-handlers.ws-handler :as mh]
   ))

(defonce channels (atom #{}))

(defn connect! [channel]
  (timbre/info "WS channel open")
  (swap! channels conj channel))

(defn disconnect! [channel {:keys [code reason]}]
  (timbre/info "WS channel closed")
  (swap! channels #(remove #{channel} %)))

(defn incoming-message! [channel message]
  (timbre/info "WS incoming message: " message)
  (mh/message-handler message))

#_(defn send-response! [response]
  (timbre/info "WS outgoing message: " response)
      (doseq [channel @channels]
        (async/send! channel (c/generate-string response))))

(def websocket-callbacks
   {:on-open connect!
    :on-close disconnect!
    :on-message incoming-message!})

(defroutes routes
  (GET "/" {c :context} (redirect (str c "/index.html")))
  (route/resources "/"))

(defrecord WebServer [options server out-chan]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting web-server at port: " (get options :port))
    (let [server (web/run (-> routes
                              (web-middleware/wrap-session {:timeout 20})
                              (web-middleware/wrap-websocket websocket-callbacks))
                   options)]
      (assoc component
             :server server)))
  (stop [component]
    (timbre/info "Stopping web-server")
    (when server
      (web/stop server)
      component)))

(defn new-web-server
  ([options out-chan]
   (map->WebServer {:options {:host (or (get options :host) "0.0.0.0")
                              :port (or (get options :port) 0)}
                    :out-chan out-chan})))
