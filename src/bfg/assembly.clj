(ns bfg.assembly
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [clojure.core.async :as async]
   [bfg.impl.web-server :refer (new-web-server)]
   ))

(defn dev-system [{:keys (betfair web-server) :as config}]
  (let []
    (-> (component/system-map
         :web-server (new-web-server web-server))
        ;; (component/system-using
        ;;  {
        ;;   :trade-manager {:trader :trader
        ;;                   :betfair-client :betfair-client
        ;;                   }
        ;;   :web-server {:sente :sente}
        ;;   })
        )))

;;(prod-system)
;; use nrepl in prod!
;;    [clojure.tools.nrepl.server :as nrepl-server]
;;    [cider.nrepl :refer (cider-nrepl-handler)]
;; (defn start-nrepl []
;;   (timbre/info "Starting nrepl at port 7888")
;;   (nrepl-server/start-server :port 7888 :handler cider-nrepl-handler))
