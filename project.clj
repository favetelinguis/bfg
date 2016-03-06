(defproject bfg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.immutant/web "2.1.2"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.0"]
                 [environ "1.0.0"]

                 [prismatic/schema "1.0.5"]
                 [org.clojure/test.check "0.9.0"]
                 [http-kit "2.1.18"]
                 [http-kit.fake "0.2.1"]
                 [cheshire "5.5.0"]

                 [levand/immuconf "0.1.0"]
                 ]
  :main bfg.core
  :uberjar-name "bfg-standalone.jar"
  :profiles {:uberjar {:aot [bfg.core]}}
  :min-lein-version "2.4.0")
