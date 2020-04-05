(ns com.ytk.config
  )

(def data (atom nil))

(defn init
  []
  (reset! data (read-string (slurp "config/config.clj"))))


(defn value
  [k]
  (k @data))
