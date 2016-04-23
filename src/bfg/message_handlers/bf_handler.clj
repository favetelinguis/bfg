(ns bfg.message-handlers.bf-handler
  (:require
   [taoensso.timbre :as timbre]
   ))

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ msg] (:type msg)))

(defmethod message-handler "GET_EVENTTYPES" get-eventtypes
  [bf msg]
  (timbre/info (:type msg)))
