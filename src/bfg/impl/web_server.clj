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
   [bfg.middleware.ws :as mw]
   ))

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
      (a/put! out-chan (mw/handler msg)))))

(defn print-error [channel throwable]
  (timbre/error throwable))

(defn start-websocket-send! [component]
  (let [kill-chan (a/chan)
        in-chan (:in-chan component)
        channels (:channels component)]
    (a/go-loop []
      (let [[msg ch] (a/alts! [kill-chan in-chan])]
        (when-not (= ch kill-chan)
          (do
            (timbre/info "SENDING TO CLIENT: " (:type msg))
            (doseq [channel @channels]
              (try
                (async/send! channel (c/generate-string (mw/handler msg)))
                (catch Exception e
                  (timbre/error "WS send error: " e))))
            (recur)))))
    (fn stop! []
      (a/put! kill-chan :stop))))

(defrecord WebServer [options running? server channels in-chan out-chan kill-fn!]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting web-server at port: " (get options :port))
    (if-not running?
      (let [websocket-callbacks {:on-open (connect-fn channels)
                                 :on-close (disconnect-fn channels)
                                 :on-error print-error
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
               :kill-fn! (start-websocket-send! component)))
      component))

  (stop [component]
    (timbre/info "Stopping web-server")
    (if running?
      (do
        (kill-fn!)
        (assoc component
               :running? false
               :kill-fn! nil))
      component)))

(defn new-web-server
  ([options in-chan out-chan]
   (map->WebServer {:options {:host (or (get options :host) "0.0.0.0")
                              :port (or (get options :port) 0)}
                    :out-chan out-chan
                    :in-chan in-chan
                    :channels (atom #{})})))
