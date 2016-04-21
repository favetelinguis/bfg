(ns bfg.impl.message-handler
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [bfg.message-handlers.mh-handler :as mh]))

#_(defn start-message-handler [in-ch db bf ws]
  (let [
        dep {:db db
             :bf bf
             :ws ws}])
  ;; Login in go loop when message arrives
  (timbre/info "Message handler recived message: " msg)
  ;; start go-loop return close fn
  ;; use (mh/message-handler msg dep)
  )

(defrecord MessageHandler [running? stop-message-handler in-ch ws bf db]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting Message handler")
    (if-not running?
      (let [mh :n #_(start-message-handler in-ch db bf ws)
            dep {:db db
                 :bf bf
                 :ws ws}
      ;; Login in go loop when message arrives
            kill-fn :start]
        (assoc component
               :running? true
               :stop-message-handler mh))
      component))

  (stop [component]
    (timbre/info "Stopping Message handler")
    (if running?
      (assoc component
             :running? false
             :stop-message-handler (stop-message-handler))
      component)))

(defn new-message-handler [in-ch]
  (map->MessageHandler {:in-ch in-ch}))
