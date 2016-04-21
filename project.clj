(defproject bfg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.immutant/web "2.1.2"]
                 [compojure "1.5.0"]
                 [ring/ring-core "1.3.0"]
                 [environ "1.0.0"] ;; todo remove

                 [prismatic/schema "1.0.5"]
                 [org.clojure/test.check "0.9.0"]
                 [http-kit "2.1.19"]
                 [http-kit.fake "0.2.1"]
                 [cheshire "5.5.0"]
                 [com.taoensso/timbre "4.1.4"]

                 [com.apa512/rethinkdb "0.15.12"]
                 [com.stuartsierra/component "0.3.1"]
                 [levand/immuconf "0.1.0"]
                 [clj-time "0.11.0"]
                 [jarohen/chime "0.1.9"]
                 [criterium "0.4.3"]
                 [org.clojure/core.async "0.2.374"]
                 ]

  :repl-options {:init-ns user
                 :welcome (println "Type (dev) to start")}

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]] ;;used for reload
                   :source-paths ["dev"]}
             :uberjar {:aot [bfg.core]}} ;; used for uberjar
  :main bfg.core
  :uberjar-name "bfg-standalone.jar"
  :min-lein-version "2.4.0")
