(ns bfg.impl.db
  (:require
   [taoensso.timbre :as timbre]
   [chime :refer [chime-ch]]
   [clj-time.periodic :refer [periodic-seq]]
   [clj-time.core :as t]
   [clojure.core.async :as a :refer [<! go-loop]]
   [rethinkdb.query :as r]
   [com.stuartsierra.component :as component]))

(declare db-connect)

(defrecord Database [running? conn out-chan]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting database connection")
    (if-not running?
      (let [c (db-connect)]
        (assoc component
               :running? true
               :conn c))
      component
      ))

  (stop [component]
    (timbre/info "Closing database connection")
    (if running?
      (assoc component
             {:running? false
              :conn (r/close conn)})
      component)))

(defn new-database [out-chan]
  (map->Database {:out-chan out-chan}))

;; use clojure time to generate a id at each pont
;; use scheduler to generate a tick each 500ms
#_(def conn (atom 0))

(defn db-connect []
  (try
    (r/connect :host "127.0.0.1" :port 28015 :db "bfg")
    (catch Exception e (str "caught exception: " (.getMessage e)))))

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

#_(defn get-table-changes []
  (let [c
        (-> (r/table "ticks")
            (r/changes)
            (r/run @conn {:async? true}))]
    (go-loop []
      (when-let [msg (<! c)]
        (println msg)
        (recur)))))

#_(defn get-data-changes []
  (let [c
        (-> (r/table "ticks")
            (r/pluck "data")
            (r/changes)
            (r/run @conn {:async? true}))]
    (go-loop []
      (when-let [msg (<! c)]
        (println msg)
        (recur)))))

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
