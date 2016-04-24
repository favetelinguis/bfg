(ns bfg.message-handlers.bf-handler
  (:require
   [taoensso.timbre :as timbre]
   [org.httpkit.client :as http]
   [cheshire.core :as c]
   [clojure.core.async :as a :refer (<! go-loop)]
   [clojure.core.async :as a]))

(def basic-request-body
  {:method :post
   :timeout 4000
   :keepalive 30000
   })

(defn send!
  [betfair msg type uri]
  (let [{:keys [token out-chan]} betfair
        url (str (get-in betfair [:config :betting-api]) uri "/")
        app-key (get-in betfair [:config :app-key])
        content (c/generate-string (:body msg))
        request (merge basic-request-body {:url url
                                           :body content
                                           :headers {"content-type" "application/json"
                                                     "Accept" "application/json"
                                                     "X-Authentication" token
                                                     "X-Application" app-key}})
        ]
    (http/request request (fn [{:keys [status body error]}]
                            (if error
                              (timbre/error "Betfair call failed, exception is " error)
                              (if (not (= status 200))
                                (timbre/error "Betfair status: " status)
                                (a/put! out-chan {:type type
                                                  :topic "DB"
                                                  :payload (c/parse-string body true)})))))))

(defmulti message-handler
  "Message handler for db calls"
  (fn [ _ msg] (:type msg)))

(defmethod message-handler "GET_EVENTTYPES" get-eventtypes
  [betfair msg]
  (timbre/info (:type msg))
  (send! betfair msg "RESP_EVENTTYPES" "listEventTypes"))

(defmethod message-handler "GET_COMPETITIONS" get-competitions
  [betfair msg]
  (timbre/info (:type msg))
  (send! betfair msg "RESP_COMPETITIONS" "listCompetitions"))
