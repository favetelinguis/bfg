(ns bfg.impl.message-handler
  (:require
   [clojure.core.async :as a]
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [bfg.message-handlers.mh-handler :as mh]
   [clojure.core.async :as a]))

(defn start-message-handler! [in-chan component]
  (let [kill-chan (a/chan)]
    (a/go-loop []
      (let [[msg ch] (a/alts! [kill-chan in-chan])]
        (when-not (= ch kill-chan)
          (do
            (timbre/info (:type msg))
            (mh/message-handler component msg)
            (recur)))))
    (fn stop! []
      (a/put! kill-chan :stop))))

(defrecord MessageHandler [running? kill-fn! in-ch ws bf db]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting Message handler")
    (if-not running?
      (let [stop-mh! (start-message-handler! in-ch component)]
        (assoc component
               :running? true
               :kill-fn! stop-mh!))
      component))

  (stop [component]
    (timbre/info "Stopping Message handler")
    (if running?
      (do
        (kill-fn!)
        (assoc component
               :running? false
               :kill-fn! nil))
      component)))

(defn new-message-handler [in-ch]
  (map->MessageHandler {:in-ch in-ch}))
