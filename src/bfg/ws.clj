(ns bfg.ws
  (:require
   [bfg.betfair.betting :as betting]
   [immutant.web.async       :as async]
   [compojure.route          :as route]
   [compojure.core           :refer (ANY GET defroutes)]
   [cheshire.core :as c]
   [ring.util.response       :refer (response redirect content-type)]))

;; Needs to return a string
(defmulti message-handler :type)

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open   (fn [channel]
                (async/send! channel "Ready to reverse your messages!"))
   :on-close   (fn [channel {:keys [code reason]}]
                 (println "close code:" code "reason:" reason))
   :on-message (fn [ch m]
                 (async/send! ch (c/generate-string
                                  (message-handler
                                   (c/parse-string m true)))))})

(defroutes routes
  (GET "/" {c :context} (redirect (str c "/index.html")))
  (route/resources "/"))

(defmethod message-handler "betting/listEventTypes"
  [{:keys (msg)}]
  {:msg @(betting/list-event-types! "dont care")})
