(ns bfg.message-handlers.ws-handler
  (:require
   [taoensso.timbre :as timbre]
   ))

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ msg] (:type msg)))

(defmethod message-handler "GET_EVENTTYPES" resp-eventtypes [db msg]
  (timbre/info (:type msg)))

(defmethod message-handler "RESP_EVENTTYPES" resp-eventtypes [db msg]
  (timbre/info (:type msg)))
