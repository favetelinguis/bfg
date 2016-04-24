(ns dev
  (:require
   ;; Used to load system
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [immuconf.config :as immuconf]
   [com.stuartsierra.component :as component]
   [taoensso.timbre :as timbre]
   [taoensso.timbre.appenders.core :as appenders]
   ;; Misc stuff used in repl for testing etc
   [clojure.core.async :as a :refer (put!)]
   [taoensso.timbre :as timbre]
   [criterium.core :as c :refer (bench)]
   [schema.test :as st]
   [schema.core :as s :refer (validate explain with-fn-validation)]
   [clojure.test :as test]
   [clojure.pprint :refer (pprint)]
   [clojure.core.async :as a :refer (put!)]
   [bfg.assembly :refer (dev-system debug-chan)]
   ))

;;HELPER FNS

(def system nil)

(defn init
  "Reload system configuration and constructs the current development system."
  []
  (let [env (immuconf/load)]
    (alter-var-root #'system
                    (constantly (dev-system (immuconf/get env))))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system component/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
    (fn [s] (when s (component/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'dev/go))

;; SETUP CONFIG need to be done in some other place to work in prod also
(timbre/merge-config! {:appenders {:spit (appenders/spit-appender {:fname "./bfg.log"})}})
(timbre/merge-config! {:appenders {:println {:enabled? false}}})
