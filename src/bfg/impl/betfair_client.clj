(ns bfg.impl.betfair-client
  (:require
   [bfg.betfair.api :as bf]
   [bfg.protocol :refer (trade-manager-dispatch-values
                                 betfair-client-dispatch-values BetfairClientMsg)]
   [taoensso.timbre :as timbre]
   [schema.core :as s]
   [com.stuartsierra.component :as component]
   [org.httpkit.client :as http]
   [cheshire.core :as c]
   [clojure.core.async :as a :refer (<! go-loop)]
   [chime :refer (chime-ch)]
   [clj-time.core :as t]
   [clj-time.periodic :refer (periodic-seq)]))

(defn- login-to-betfair
  [url
   usr
   psw
   app-key]
  (let [options {:url url
                 :method :post
                 :timeout 4000
                 :headers {"Accept" "application/json"
                           "X-Application" app-key}
                 :form-params {"username" usr
                               "password" psw}}]
    (http/request options)))

(defn- logout-from-betfair
  [url
   token
   app-key]
  (let [options {:url url
                 :method :post
                 :timeout 4000
                 :headers {"Accept" "application/json"
                           "X-Authentication" token
                           "X-Application" app-key}}]
    (http/request options)))

(defn- betfair-keep-alive
  [url
   token
   app-key]
  (let [options {:url url
                 :method :post
                 :timeout 4000
                 :headers {"Accept" "application/json"
                           "X-Authentication" token
                           "X-Application" app-key}}]
    (http/request options)))

#_(defn- send-request
  [url token app-key msg response-ch]
  (let [{:keys (body endpoint)} msg
        options {:url (str url endpoint)
                 :method :post
                 :timeout 4000
                 :keepalive 30000
                 :body body
                 :headers {"Accept" "application/json"
                           "X-Authentication" token
                           "X-Application" app-key}}]
    (http/request options (fn [response] (a/put! response-ch
                                                 {:dispatch (:in trade-manager-dispatch-values)
                                                  :msg response
                                                  })))))

#_(defn- new-feed-request
  [url token app-key body]
  (let [options {:url url
                 :method :post
                 :timeout 4000
                 :keepalive 30000
                 :body body
                 :headers {"Accept" "application/json"
                           "X-Authentication" token
                           "X-Application" app-key}}]
    (http/request options)))

(defn- start-keep-alive
  "TODO: How to handle when keep-alive faile??"
  [keep-alive-url token app-key keep-alive-period]
  (let [timer-ch (chime-ch (rest (periodic-seq (t/now) ;; removed
                                               (-> 10000 t/seconds))))]
    (go-loop []
      (when-let [trigger-time (<! timer-ch)]
        (let [response @(betfair-keep-alive keep-alive-url token app-key)
              http-code (:status response)]
          (if-not (= http-code 200)
            (timbre/error "Failed keep alive connect: " response)
            (let [{:keys (token product status error)} (c/parse-string (:body response) true)]
              (if-not (= status "SUCCESS")
                (timbre/error "Failed betfair keep alive:" status "error:" error)
                (timbre/info "Keep alive at: " trigger-time)))))
        (recur)))
    (fn stop-fn [] (a/close! timer-ch))))

#_(defn- start-api
  [betfair-request betfair-response market-book-feed token config]
  (let [betting-url (get-in config [:betting-api])
        account-url (get-in config [:account-api])
        app-key (get config :app-key)]
    (go-loop []
      (when-let [{:keys (dispatch msg) :as request} (a/<! betfair-request)] 
        (if-let [error (s/check BetfairClientMsg request)]
          (timbre/warn "Illegal value recived: " error)
          (condp = dispatch 
            (:account betfair-client-dispatch-values)
            (send-request account-url token app-key msg betfair-response)
            
            (:betting betfair-client-dispatch-values)
            (send-request betting-url token app-key msg betfair-response)
            
            (:market-book betfair-client-dispatch-values)
            (->>
             (new-feed-request betting-url token app-key msg)
             (assoc
              {:dispatch (:market-book trade-manager-dispatch-values)}
              :msg)
             (a/>! market-book-feed))))
        (recur)))))

(defrecord Betfair-client
    [config running? token kill-keep-alive out-chan]
  component/Lifecycle
  (start [component]
    (timbre/info "Starting betfair client")
    (if-not running?
      (let [login-url (get-in config [:login-url])
            keep-alive-url (get-in config [:keep-alive-url])
            keep-alive-period (get-in config [:keep-alive-period])
            usr (get config :usr)
            psw (get config :pwd)
            app-key (get config :app-key)
            ;; try to login to betfair
            login-response @(login-to-betfair login-url usr psw app-key)
            http-code (:status login-response)]
        (if-not (= http-code 200)
          (throw (Exception. (str "Connection failed: " login-response)))
          (let [{:keys (token product status error)} (c/parse-string
                                                      (:body login-response) true)]
            (if-not (= status "SUCCESS")
              (throw (Exception. (str "Login failed: " status "error:" error)))
              (do
                ;; (start-api betfair-request betfair-response market-book-feed token config)
                (assoc component
                       :running? true
                       :token token
                       :kill-keep-alive (start-keep-alive
                                         keep-alive-url token
                                         app-key keep-alive-period)))))))
      component))
  (stop [component]
    (timbre/info "Stopping betfair client")
    (if running?
      (let [logout-url (get-in config [:logout-url])
            app-key (get config :app-key)
            logout-response @(logout-from-betfair logout-url token app-key)
            http-code (:status logout-response)]
        (if-not (= http-code 200)
          (throw (Exception. (str "Connection failed: " logout-response)))
          (let [{:keys (token product status error)} (c/parse-string
                                                      (:body logout-response) true)]
            (if-not (= status "SUCCESS")
              (throw (Exception. (str "Logout failed:" status "error:" error)))
              (do
                ;; (a/close! betfair-response)
                ;; (a/close! market-book-feed)
                (assoc component
                       :running? false
                       :token nil
                       :kill-keep-alive (kill-keep-alive)))))))
      component)))

(defn new-betfair-client [config out-chan]
  (map->Betfair-client {:config config
                        :out-chan out-chan}))
