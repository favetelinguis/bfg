(ns bfg.message-handlers.bf-handler)

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ {:keys [dispatch]}] (first dispatch)))

(defmethod message-handler :list-event-types list-event-types [betfair message]
  1)
