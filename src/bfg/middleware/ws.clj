(ns bfg.middleware.ws
  (:require
   [taoensso.timbre :as timbre]
   ))

(defmulti handler :type)

(defmethod handler "GET_EVENTTYPES" get-eventtypes [msg]
  (timbre/info "Incoming Middleware: " (:type msg))
  (let [updated-msg (assoc msg :topic "BF")]
    updated-msg))

(defmethod handler "RESP_EVENTTYPES" resp-eventtypes [msg]
  (timbre/info "Outgoing middleware " (:type msg))
  (let [updated-msg (dissoc msg :topic)]
    updated-msg))
