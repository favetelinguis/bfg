(ns bfg.message-handlers.ws-handler
  (:require [bfg.betfair.api :as bf]))

(defmulti message-handler
  "Message handler for ws calls"
  (fn [{:keys [dispatch]} _] (first dispatch)))

(defmethod message-handler :betfair call-betfair [message betfair]
  (let [updated-message (update message :dispatch rest)]
    (bf/message-handler updated-message betfair)))
