(ns bfg.core
  (:require
   [bfg.ws :as ws]
   [immuconf.config :as immuconf]
   [immutant.web             :as web]
   [immutant.web.async       :as async]
   [immutant.web.middleware  :as web-middleware]
   [compojure.route          :as route]
   [environ.core             :refer (env)]
   [compojure.core           :refer (ANY GET defroutes)])
  (:gen-class))

(defn -main [& {:as args}]
  (web/run
    (-> ws/routes
        (web-middleware/wrap-session {:timeout 20})
        ;; wrap the handler with websocket support
        ;; websocket requests will go to the callbacks, ring requests to the handler
        (web-middleware/wrap-websocket ws/websocket-callbacks))
    (merge {"host" (env :demo-web-host), "port" (env :demo-web-port)}
           args)))
