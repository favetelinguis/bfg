(ns bfg.message-handlers.db-handler
  (:require
   [rethinkdb.query :as r]
   [taoensso.timbre :as timbre]
   [clojure.core.async :as a]))

(defn transform-eventtypes-list
  [{:keys [marketCount eventType]}]
  (let [{:keys [id name]} eventType]
    {:id id
     :name name
     :marketCount marketCount}))

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ msg] (:type msg)))

(defmethod message-handler "RESP_EVENTTYPES" resp-eventtypes [db msg]
  (timbre/info (:type msg))
  (let [data (map transform-eventtypes-list (:payload msg))
        db-conn (:conn db)
        ]
    (-> (r/table "junk")
        (r/get 1)
        (r/replace {:id 1 :topic "WS" :type "RESP_EVENTTYPES" :eventTypes data})
        (r/run db-conn))))
