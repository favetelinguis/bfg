(ns bfg.message-handlers.db-handler
  (:require
   [rethinkdb.query :as r]
   [taoensso.timbre :as timbre]
   [clojure.core.async :as a]))


(defn replace! [conn id type data]
  (-> (r/table "junk")
      (r/get id)
      (r/replace
       {:id id :topic "WS" :type type :time (r/now) :data data})
      (r/run conn)))

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ msg] (:type msg)))

(defmethod message-handler "RESP_EVENTTYPES" resp-eventtypes [db msg]
  (timbre/info (:type msg))
  (let [data (:payload msg)
        id "eventTypes"
        type "RESP_EVENTTYPES"
        db-conn (:conn db)]
    (replace! db-conn id type data)))

(defmethod message-handler "RESP_COMPETITIONS" resp-competitions [db msg]
  (timbre/info (:type msg))
  (let [data (:payload msg)
        id "competitions"
        type "RESP_COMPETITIONS"
        db-conn (:conn db)]
    (replace! db-conn id type data)))
