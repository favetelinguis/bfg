(ns bfg.impl.db
  (:require
   [taoensso.timbre :as timbre]
   [chime :refer [chime-ch]]
   [clj-time.periodic :refer [periodic-seq]]
   [clj-time.core :as t]
   [clojure.core.async :as a :refer [<! go-loop]]
   [rethinkdb.query :as r]
   [com.stuartsierra.component :as component]
   [bfg.message-handlers.db-handler :as db]))

(defn get-table-changes [conn out-chan]
  (let [c
        (-> (r/table "junk")
            (r/get 1)
            (r/changes)
            ;; (r/map #(:new_val %))
            (r/run conn {:async? true}))]
    (go-loop []
      (when-let [msg (<! c)]
        (let [payload (get msg :new_val)]
          (a/put! out-chan payload))
        (recur)))))

(defn changefeed [conn out-chan]
  (let [changefeed (-> (r/table "junk")
                       (r/pluck "eventTypes")
                       (r/changes)
                       ;; (r/map :new_val)
                       ;; (r/map (fn [s] (assoc s :topic "WS" :type "RESP_EVENTTYPES")))
                       (r/run conn {:async? true}))]
    (a/pipe changefeed out-chan false)))

(defn db-connect []
  (try
    (r/connect :host "127.0.0.1" :port 28015 :db "bfg")
    (catch Exception e (str "caught exception: " (.getMessage e)))))

(defn start-db-handler! [component]
  (let [kill-chan (a/chan)
        in-chan (:in-chan component)]
    (a/go-loop []
      (let [[msg ch] (a/alts! [kill-chan in-chan])]
        (when-not (= ch kill-chan)
          (do
            (timbre/info (:type msg))
            (try
              (db/message-handler component msg)
              (catch Exception e
                (timbre/error "DB error: " e)))
            (recur)))))
    (fn stop! []
      (a/put! kill-chan :stop))))

(defrecord Database [running? conn in-chan out-chan kill-fn!]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting database connection")
    (if-not running?
      (let [c (db-connect)
            temp-component (assoc component
                        :running? true
                        :conn c)]
        #_(changefeed c out-chan) ;; start a change feed for eventTypes
        (get-table-changes c out-chan)
        (assoc temp-component
               :kill-fn! (start-db-handler! temp-component)))
      component
      ))

  (stop [component]
    (timbre/info "Closing database connection")
    (if running?
      (do
        (kill-fn!)
        (r/close conn)
        (assoc component
               :running? false
                :conn nil))
      component)))

(defn new-database [in-chan out-chan]
  (map->Database {:out-chan out-chan
                  :in-chan in-chan}))

;; use clojure time to generate a id at each pont
;; use scheduler to generate a tick each 500ms
#_(def conn (atom 0))


#_(defn db-close [] (r/close conn))

#_(defn db-create [db]
  (try
    (->
     (r/db-create db)
     (r/run conn))
    (catch Exception e (str "caught exception: " (.getMessage e)))))

#_(defn db-create-table [table]
  (try
    (->
     (r/table-create table)
     (r/run conn))
    (catch Exception e (str "caught exception: " (.getMessage e)))))

#_(defn random-walk [y]
  (let [e (s/sample-normal 1)
        y (+ y e)]
    (cons y (lazy-seq (random-walk y)))))

#_(defn ex-9-15 []
  (->> (random-walk 0)
       (take 50)))



#_(def times
  (rest    ; excludes *right now*
   (periodic-seq (t/now)
                 (-> 5000 t/millis))))

#_(defn start-ticker [] ;;I can close the channel to cancel scheduler
  (let [chimes (chime-ch times {:ch (a/chan (a/sliding-buffer 1))})]
    (go-loop [ticker (random-walk 0)]
      (when-let [time (<! chimes)]
        (-> (r/table "ticks")
            (r/insert [{:time (r/now)
                        :data (take 1 ticker)}])
            (r/run @conn))
        (recur (next ticker))))))

;; I am not aware of specific snippet for time series though.
;; But you just have to create an index on your time serie data,
;; and use orderBy({index: "time"}) (something like that).
