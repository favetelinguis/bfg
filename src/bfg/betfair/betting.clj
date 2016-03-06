(ns bfg.betfair.betting
  (:require
   [org.httpkit.client :as http]
   [schema.core :as s]
   [schema.experimental.generators :as g]
   ))

#_(defn- send-betting-request!
  [token app-key payload endpoint]
  (let [url "https://api.betfair.com/exchange/betting/rest/v1.0/"
        options {:url (str url endpoint)
                 :method :post
                 :timeout 4000
                 :keepalive 30000
                 :body payload
                 :headers {"Accept" "application/json"
                           "X-Authentication" token
                           "X-Application" app-key}}
        ] (http/request options)))

(s/defrecord EventType
    [id :- s/Str
     name :- s/Str])

(s/defrecord EventTypeResult
    [event-type :- EventType
     name :- s/Str])

(defn- send-betting-request!
  "A mock that returns a promise with a generated data"
  [token app-key payload endpoint]
  (let [return (promise)
        number (rand-int 20)]
    (deliver return [(g/sample number EventTypeResult)])
    return))

(defn list-event-types! [market-filter]
  (send-betting-request! "t" "ak" market-filter "listEventTypes"))

(list-event-types! 1)
