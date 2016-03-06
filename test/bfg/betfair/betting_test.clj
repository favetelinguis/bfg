(ns bfg.betfair.betting-test
  (:require [bfg.betfair.betting :as sut]
            [org.httpkit.fake :refer :all]
            [clojure.test :refer :all]))

(deftest betting-api
  (testing "list-event-types!"
    (let [response "pong"]
      (is
       (=
        (with-fake-http
          [#".*" response]
          (:body @(sut/list-event-types! "ping")))
        response)))))
