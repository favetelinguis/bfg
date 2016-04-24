(ns bfg.assembly
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [clojure.core.async :as a]
   [bfg.impl.betfair-client :as bfc]
   [bfg.impl.web-server :refer (new-web-server)]
   [bfg.impl.message-handler :refer (new-message-handler)]
   [bfg.impl.db :refer (new-database)]
   ))

;; Can be used in the repl to send in messages to the system 
(def debug-chan (a/chan))

(defn dev-system [{:keys (betfair web-server) :as config}]
  (let [ws-out-chan (a/chan)
        db-out-chan (a/chan)
        bf-out-chan (a/chan)
        master-out-chan (a/merge [ws-out-chan db-out-chan bf-out-chan debug-chan] 1024)
        ws-in-chan (a/chan)
        db-in-chan (a/chan)
        bf-in-chan (a/chan)
        pub-chan (a/pub master-out-chan :topic)
        ]
    ;; Channel Setup
    (a/sub pub-chan "WS" ws-in-chan)
    (a/sub pub-chan "DB" db-in-chan)
    (a/sub pub-chan "BF" bf-in-chan)
    ;; System Setup
    (-> (component/system-map
         :web-server (new-web-server web-server ws-in-chan ws-out-chan)
         :betfair-client (bfc/new-betfair-client betfair bf-in-chan bf-out-chan)
         :db (new-database db-in-chan db-out-chan) ;;add config parameter for db
         )
        #_(component/system-using
         {
          :message-handler {:ws :web-server
                            :bf :betfair-client
                            :db :db}
          })
        )))

;;(prod-system)
;; use nrepl in prod!
;;    [clojure.tools.nrepl.server :as nrepl-server]
;;    [cider.nrepl :refer (cider-nrepl-handler)]
;; (defn start-nrepl []
;;   (timbre/info "Starting nrepl at port 7888")
;;   (nrepl-server/start-server :port 7888 :handler cider-nrepl-handler))
