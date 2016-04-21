(ns bfg.message-handlers.mh-handler
  (:require
   [bfg.message-handlers.bf-handler :as bf]
   [bfg.message-handlers.ws-handler :as ws]
   [bfg.message-handlers.db-handler :as db]
   ))

(defmulti message-handler
  (fn [{:keys [dispatch]} _] (first dispatch)))

(defmethod message-handler "db" message-handler-db [message {:keys [db]}]
  (let [new-message (update message :dispatch rest)]
    (db/message-handler new-message db)))

(defmethod message-handler "bf" message-handler-bf [message {:keys [bf]}]
  (let [new-message (update message :dispatch rest)]
    (bf/message-handler new-message bf)))

(defmethod message-handler "ws" message-handler-ws [message {:keys [ws]}]
  (let [new-message (update message :dispatch rest)]
    (ws/message-handler new-message ws)))
