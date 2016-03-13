(use '[clojure.java.shell :only [sh]])

(sh "lein uberjar")
(sh "java -jar target/bfg-standalone.jar host 0.0.0.0 port 5000")
