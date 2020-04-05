(defproject YoutubeForKids "0.0.1"
  :description "Youtube for Kids"
  :url "https://ytk"
  :min-lein-version "2.0.0"
  :dependencies [
                 [org.clojure/clojure "1.10.1"]
                 [clj-time "0.15.2"]
                 [cheshire "5.9.0"]
                 [markdown-clj "1.10.1"]
                 [com.clojure-goes-fast/clj-memory-meter "0.1.2"]
                 [selmer "1.12.18"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [hikari-cp "2.10.0"]
                 [funcool/clojure.jdbc "0.9.0"]
                 [calfpath "0.7.2"]
                 [ring/ring "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring-json-response "0.2.0"]
                 [asphalt "0.6.7"]
                 [metosin/ring-http-response "0.9.1"]
                 [cambium/cambium.core         "0.9.3"]
                 [cambium/cambium.codec-cheshire "0.9.3"]
                 [cambium/cambium.logback.json   "0.4.3"]
                 [com.draines/postal "2.0.3"]
                 [clj-http "3.10.0"]
                 [org.clojure/core.async "0.7.559"]
                 [crypto-random "1.2.0"]
                 ]
  :global-vars {*warn-on-reflection* false
                *assert* true
                *unchecked-math* :no-warn-on-boxed}
  :resource-paths ["resources" "resources/public"]
  :jvm-opts ["-ea"]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler com.ytk.main/ring-handler
         :init    com.ytk.main/init})
