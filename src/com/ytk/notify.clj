(ns com.ytk.notify
  (:require [clj-http.client :as http]
            [com.ytk.db :as db]
            [cambium.core  :as log]
            [clojure.pprint :as pp]
            [com.ytk.config :as config]
            [clj-time.format :as f]))

(def zoko (atom nil))

(defn init
  []
  (reset! zoko (config/value :zoko)))

(def custom-formatter (f/formatter "dd MMMM, yyyy"))

(defn wa-notify-user-attendance
  [phone attendance-record]
  (try
    (log/info (str "Beginning new thread & Type of date-on is - " (type (:date-on attendance-record))))
    (let [zoko (config/value :zoko)
          message (str "Attendance marked for " (f/unparse custom-formatter (:date-on attendance-record)))
          url (str (:server zoko) (:person-send-url zoko))
          message-result (http/post url
                                    {:headers {"apikey" (:apikey zoko)}
                                     :content-type :json
                                     :accept :json
                                     :form-params {"messagePlatform" (:message-platform zoko)
                                              "messsageType" (:message-type zoko)
                                              "number" phone
                                              "message" message}
                                     })
          notify-record {:user-id (:employee-id attendance-record)
                         :group-flag "N"
                         :message message
                         :phone phone
                         :notify-method "Whatsapp"
                         :notify-type "Text"
                         :results (str message-result)}]
      (db/save-notify-record notify-record)
      notify-record)
    (catch Exception e
      (log/error (str "Error in wa-notify-user-attendance - " e))
      (throw e))))


(defn wa-notify-group-notice
  [notice]
  (try
    (let [zoko (config/value :zoko)
          phone (:tzana-test-group-id zoko)
          notice-type (:notice-type notice)
          message (str (cond
                             (= notice-type "A") "FOR YOUR ACTION"
                             (= notice-type "I") "FOR YOUR INFORMATION"
                             (= notice-type "T") "TZANA NEWS"
                             (= notice-type "P") "PHARMA NEWS"
                             (= notice-type "N") "NOTICE")
                       "\n-----------------------------\n"
                       (:notice-subject notice)
                       "\n-----------------------------\n"
                       (:notice notice))
          url (str (:server zoko) (:group-send-url zoko))
          message-result (http/post url
                                    {:headers {"apikey" (:apikey zoko)}
                                     :content-type :json
                                     :accept :json
                                     :form-params {"messagePlatform" (:message-platform zoko)
                                                   "messsageType" (:message-type zoko)
                                                   "number" phone
                                                   "message" message}
                                     })
          notify-record {:user-id 0
                         :group-flag "Y"
                         :message message
                         :phone phone
                         :notify-method "Whatsapp"
                         :notify-type "Text"
                         :results (str message-result)}]
      (db/save-notify-record notify-record)
      nil)
    (catch Exception e
      (log/error (str "Error in wa-notify-group-notice - " e))
      (throw e))))
