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

(defn dev-system [{:keys (betfair web-server) :as config}]
  (let [ws-out-chan (a/chan)
        db-out-chan (a/chan)
        bf-out-chan (a/chan)
        mh-in-chan (a/merge [ws-out-chan db-out-chan bf-out-chan] 1024)
        ]
    (-> (component/system-map
         :message-handler (new-message-handler mh-in-chan)
         :web-server (new-web-server web-server ws-out-chan)
         :betfair-client (bfc/new-betfair-client betfair bf-out-chan)
         :db (new-database db-out-chan) ;;add config parameter for db
         )
        (component/system-using
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
