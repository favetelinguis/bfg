(defproject bfg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.immutant/web "2.0.0-beta2"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.0"]
                 [environ "1.0.0"]]
  :main bfg.core
  :uberjar-name "bfg-standalone.jar"
  :profiles {:uberjar {:aot [bfg.core]}}
  :min-lein-version "2.4.0")
