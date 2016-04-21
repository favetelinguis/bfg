(ns bfg.message-handlers.db-handler
  (:require
   [rethinkdb.query :as r]
   ))

(defmulti message-handler
  "Message handler for db calls"
  (fn [{:keys [dispatch]} _] (first dispatch)))

(defmethod message-handler :list-event-types list-event-types [message db]
  ;; PERSIST IN DB
  (let [{:keys [msg]} message]
    (-> (r/table "junk")
        (r/insert [msg])
        (r/run (:conn db)))))
