(ns bfg.ws
  (:require
   [bfg.betfair.betting :as betting]
   [immutant.web.async       :as async]
   [compojure.route          :as route]
   [compojure.core           :refer (rfn ANY GET defroutes)]
   [cheshire.core :as c]
   [ring.util.response       :refer (response redirect resource-response content-type)]))

;; Needs to return a string
(defmulti message-handler :type)

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open   (fn [channel]
                ;; start a go block and register a channel, how to handle
                ;; when the same channel reconect during a heartbeat?
                ;; we dont want on close to kill the go block and we loose all state
                ;; if we dont respond to x heartbeats we kill the go block??
                (async/send! channel (c/generate-string
                                      {:event "action"
                                       :payload {:type "SET_STATE"
                                                 :state {
                                                         :eventTypes (->>
                                                                      @(betting/list-event-types! "bla")
                                                                      (map #(assoc-in % [:eventType :id] (str (gensym "id")))))
                                                         }}})))

   :on-close   (fn [channel {:keys [code reason]}]
                 (println "close code:" code "reason:" reason))
   :on-message (fn [ch m]
                 (async/send! ch (c/generate-string
                                  (message-handler
                                   (c/parse-string m true)))))})

(defroutes routes
  (GET "/" []
    (-> (resource-response "index.html" {:root "public"})
        (content-type "text/html")))
  (GET "/*" [] (redirect "/")))

(defmethod message-handler "betting/listEventTypes"
  [{:keys (msg)}]
  {:type "event-types"
   :state {:eventTypes @(betting/list-event-types! "dont care")}})
