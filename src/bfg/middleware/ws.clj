(ns bfg.middleware.ws
  (:require
   [taoensso.timbre :as timbre]
   ))

(defn cleanup [msg fn]
  (let [{:keys [id type data]} msg
        new-data-keyword (keyword id)
        transformed-data (map fn data)]
    {:type type
     new-data-keyword transformed-data}))

(defn transform-competitions-list
  [{:keys [marketCount competition competitionRegion]}]
  (let [{:keys [id name]} competition]
    {:id id
     :name name
     :competitionRegion competitionRegion
     :marketCount marketCount}))

(defn transform-eventtypes-list
  [{:keys [marketCount eventType]}]
  (let [{:keys [id name]} eventType]
    {:id id
     :name name
     :marketCount marketCount}))

(defmulti handler :type)

(defmethod handler "GET_EVENTTYPES" get-eventtypes [msg]
  (timbre/info "Incoming Middleware: " (:type msg))
  (let [updated-msg (assoc msg :topic "BF")]
    updated-msg))

(defmethod handler "RESP_EVENTTYPES" resp-eventtypes [msg]
  (timbre/info "Outgoing middleware " (:type msg))
  (let []
    (cleanup msg transform-eventtypes-list)))

(defmethod handler "GET_COMPETITIONS" get-competitions [msg]
  (timbre/info "Incoming Middleware: " (:type msg))
  (let [updated-msg (assoc msg :topic "BF")]
    updated-msg))

(defmethod handler "RESP_COMPETITIONS" resp-competitions[msg]
  (timbre/info "Outgoing middleware " (:type msg))
  (let []
    (cleanup msg transform-competitions-list)))
