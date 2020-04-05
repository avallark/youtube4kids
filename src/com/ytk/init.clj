(ns com.ytk.init
  (:require [jdbc.core :as jdbc]
            [hikari-cp.core :as pool]
            [asphalt.core :as a]
            [asphalt.transaction :as t]
            [cheshire.core :as json]
            [com.ytk.config :as config]
            [cambium.core :as log])
  (:import (java.sql SQLException)))


(set! *unchecked-math* :no-warn-on-boxed)

(def ds (atom nil))

(defn init
  []
  (if (= (config/value :env) "dev")
    (reset! ds (config/value :db-dev))
    (reset! ds (pool/make-datasource (config/value :db)))
    ))
