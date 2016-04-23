(ns bfg.message-handlers.mh-handler
  (:require
   [taoensso.timbre :as timbre]
   [bfg.message-handlers.bf-handler :as bfc]
   [bfg.message-handlers.ws-handler :as ws]
   [bfg.message-handlers.db-handler :as db]
   ))

(defmulti message-handler
  (fn [_ msg] (:type msg)))

(defmethod message-handler "GET_EVENTTYPES" get-eventtypes
  [{:keys [bf]} msg]
  (let []
    (timbre/info (:type msg))
    (bfc/message-handler bf msg)))

(defmethod message-handler :default
  [{:keys [bf]} msg]
  (timbre/info "In default with: " msg))
