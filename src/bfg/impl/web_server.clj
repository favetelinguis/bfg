(ns bfg.impl.web-server
  (:require
   [immutant.web             :as web]
   [immutant.web.async       :as async]
   [immutant.web.middleware  :as web-middleware]

   [compojure.route          :as route]
   [compojure.core           :refer (ANY GET routes)]
   [ring.util.response       :refer (response redirect resource-response content-type)]
   [cheshire.core :as c]

   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [clojure.core.async :as a :refer (<! go-loop)]

   [bfg.message-handlers.ws-handler :as mh]
   ))

#_(defonce channels (atom #{}))

(defn connect-fn [channels]
  (fn [channel]
    (timbre/info "WS channel open")
    (swap! channels conj channel)))

(defn disconnect-fn [channels]
  (fn [channel {:keys [code reason]}]
    (timbre/info "WS channel closed")
    (swap! channels #(remove #{channel} %))))

(defn incoming-message-fn [out-chan]
  (fn [channel message]
    (timbre/info "WS incoming message: " message)
    (let [msg (c/parse-string message true)]
      (a/put! out-chan msg))))

(defn send-fn [channels]
  (fn [response]
    (timbre/info "WS outgoing message: " response)
    (doseq [channel @channels]
      (async/send! channel (c/generate-string response)))))

#_(def websocket-callbacks
   {:on-open (connect-fn channels)
    :on-close (disconnect-fn channels)
    :on-message incoming-message!})

#_(routes
  (GET "/" {c :context} (redirect (str c "/index.html")))
  (route/resources "/"))

(defrecord WebServer [options server channels out-chan send!]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting web-server at port: " (get options :port))
    (let [websocket-callbacks {:on-open (connect-fn channels)
                               :on-close (disconnect-fn channels)
                               :on-message (incoming-message-fn out-chan)}
          web-routes (routes
                      (GET "/" {c :context} (redirect (str c "/index.html")))
                      (route/resources "/"))
          server (web/run (-> web-routes
                              (web-middleware/wrap-session {:timeout 20})
                              (web-middleware/wrap-websocket websocket-callbacks))
                   options)]
      (assoc component
             :server server
             :send! (send-fn channels))))
  (stop [component]
    (timbre/info "Stopping web-server")
    (when server
      (web/stop server)
      component)))

(defn new-web-server
  ([options out-chan]
   (map->WebServer {:options {:host (or (get options :host) "0.0.0.0")
                              :port (or (get options :port) 0)}
                    :out-chan out-chan
                    :channels (atom #{})})))
