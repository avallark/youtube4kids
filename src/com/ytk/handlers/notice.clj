(ns com.ytk.handlers.notice
  (:require
   [com.ytk.config :as config]
   [com.ytk.db :as db]
   [com.ytk.layout :as layout]
   [com.ytk.email :as email]
   [cambium.core  :as log]
   [com.ytk.notify :as notify]
   [clojure.core.async :as async :refer :all]))

(defn notices
  [req]
  (try
    (let [notices (db/get-latest-notices 20)]
        (layout/render "notices.html" {:page-title "Notices"
                                   :notices notices
                                   :req req}))
    (catch Exception e
      (log/error (str "Exception in notices - " e))
      (layout/rest-json-ok (str "Exception in notices - " (.getMessage e))))))

(defn notices-non-admin
  [req]
  (try
    (let [notices (db/get-latest-notices 20)]
      (layout/render "notices-non-admin.html" {:page-title "Notices"
                                               :notices notices
                                               :req req}))
    (catch Exception e
      (log/error (str "Exception in notices - " e))
      (layout/rest-json-ok (str "Exception in notices - " (.getMessage e))))))

(defn create-new-notice
  [req]
  (let [notice {:notice-subject (not-empty (get-in req [:params :notice-subject]))
                :notice (not-empty (get-in req [:params :notice]))
                :notice-type (not-empty (get-in req [:params :notice-type]))
                :created-by (get-in req [:session :user :user-id])}]
    (try
      (db/create-new-notice notice)
      (go (notify/wa-notify-group-notice notice))
      (layout/redirect-url "/notices")
      (catch Exception e
        (log/error (str "Error in create-new-notice - " e))
        (layout/rest-json-500 (str "Error in create new notice - " (.getMessage e)))))))


(defn view-notice
  [{:keys [notice-id] :as req}]
  (try
    (let [notice (db/get-notice notice-id)]
      (layout/render "view-notice.html" {:page-title "Notice"
                                         :req req
                                         :notice notice}))
    (catch Exception e
      (log/error (str "Exception in view-notice - " e))
      (layout/rest-json-ok (str "Exception in view-notice - " (.getMessage e))))))

(defn view-notice-api
  [{:keys [notice-id] :as req}]
  (try
    (log/info (str "notice-id - " notice-id))
    (let [notice (db/get-notice notice-id)]
      (layout/rest-json-ok {:status 1
                            :message "Fetched notice successfully"
                            :notice notice}))
    (catch Exception e
      (log/error (str "Exception in view-notice - " e))
      (layout/rest-json-500 (str "Exception in view-notice - " (.getMessage e))))))



(defn template-fn
  [req]
  (try
    []
    (catch Exception e
      (log/error (str "Exception in notices - " e))
      (layout/rest-json-ok (str "Exception in notices - " (.getMessage e))))))
