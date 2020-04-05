(ns com.ytk.db
  (:require [jdbc.core :as jdbc]
            [hikari-cp.core :as pool]
            [asphalt.core :as a]
            ;[asphalt.transaction :as t]
            [cheshire.core :as json]
            [com.ytk.config :as config]
            [com.ytk.sql :as sql]
            [clj-time.coerce :as tc]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clojure.pprint :as pp]
            [com.ytk.init :as init]
            [cambium.core :as log])
  (:import (java.sql SQLException)))

;(jdbc/insert! db-spec :table {:col1 42 :col2 "123"})               ;; Create
;(jdbc/query   db-spec ["SELECT * FROM table WHERE id = ?" 13])     ;; Read
;(jdbc/update! db-spec :table {:col1 77 :col2 "456"} ["id = ?" 13]) ;; Update
;(jdbc/delete! db-spec :table ["id = ?" 13])                        ;; Delete


(defn validate-login
  [email-id password]
  (try (let [seq-to-rows-user (fn [seq] {
                                         :user-id (nth seq 0)
                                         :user-name (nth seq 1)
                                         :user-full-name (nth seq 2)
                                         :user-title (nth seq 3)
                                         :email-id (nth seq 4)
                                         :user-role (nth seq 5)
                                    })
             rows (a/query a/fetch-single-row @init/ds sql/login-verify {:email-id email-id
                                                                    :password password})
        user (seq-to-rows-user rows)]
         user)
       (catch SQLException e
         (log/error {} (str "Database: Error fetching user information - " e))
         (throw e))))



(defn change-password
  [email-id password]
  (log/info (str "DB: Changing password for user - " email-id))
  (try
    (a/update @init/ds
              sql/change-password {:email-id email-id
                                   :password password})
    (catch SQLException e
      (log/error {} (str "Database: Error changing passwords - " e))
      (throw e))))


;; Employees / Users

(defn get-all-users
  []
  (try (let [seq-to-rows (fn [seq] {:user-id (nth seq 0)
                                    :full-name (nth seq 1)
                                    :phone1 (nth seq 2)
                                    :user-role (nth seq 3)})
             rows (a/query a/fetch-rows
                           @init/ds
                           sql/get-all-users {})
             all-users (vec (map seq-to-rows rows))]
         all-users)
       (catch SQLException e
         (log/error (str "DB Error in get-all-users - " e))
         (throw e))))

(defn get-employee-by-id
  [employee-id]
  (log/info (str "Requesting user details for id - " employee-id))
  (try (let [seq-to-rows-user (fn [seq] {
                                         :user-id (nth seq 0)
                                         :user-name (nth seq 1)
                                         :user-full-name (nth seq 2)
                                         :full-name (nth seq 2)
                                         :user-title (nth seq 3)
                                         :email-id (nth seq 4)
                                         :user-role (nth seq 5)
                                         :phone1 (nth seq 6)
                                         })
             rows (a/query a/fetch-single-row
                           @init/ds
                           sql/get-employee-by-id
                           {:employee-id employee-id})
             user (seq-to-rows-user rows)]
         user)
       (catch SQLException e
         (log/error {} (str "Database: Error fetching user information - " e))
         (throw e))))

(defn get-all-employees
  []
  (try
    (let [seq-to-map (fn [seq] {:user-id (nth seq 0)
                                :user-name (nth seq 1)
                                :user-full-name (nth seq 2)
                                :full-name (nth seq 2)
                                :user-title (nth seq 3)
                                :email-id (nth seq 4)
                                :user-role (nth seq 5)
                                :phone1 (nth seq 6)})
          rows (a/query a/fetch-rows
                        @init/ds
                        sql/get-all-employees {})
          people (vec (map seq-to-map rows))]
      (log/info "In get all employees")
      (log/info (pr-str people))
      people)
    (catch SQLException e
      (log/error (str "Database: Error get-all-employees - " e))
      (throw e))))

(defn get-all-sales
  []
  (try
    (let [seq-to-map (fn [seq] {:user-id (nth seq 0)
                                :user-name (nth seq 1)
                                :user-full-name (nth seq 2)
                                :full-name (nth seq 2)
                                :user-title (nth seq 3)
                                :email-id (nth seq 4)
                                :user-role (nth seq 5)
                                :phone1 (nth seq 6)})
          rows (a/query a/fetch-rows
                        @init/ds
                        sql/get-all-sales {})
          people (vec (map seq-to-map rows))]
      people)
    (catch SQLException e
      (log/error (str "Database: Error get-all-sales - " e))
      (throw e))))



(defn save-notify-record
  [notify-record]
  (try
    (a/update @init/ds
              sql/save-notify-record
              notify-record)
    (catch SQLException e
      (log/error (str "Error in DB for save-notify-record - " e))
      (throw e)))
  )


(defn get-latest-notices
  [count]
  (try
    (let [seq-to-map (fn [seq] {:notice-id (nth seq 0)
                              :notice-subject (nth seq 1)
                              :notice (nth seq 2)
                              :notice-type (nth seq 3)})
        rows (a/query a/fetch-rows
                      @init/ds
                      sql/get-latest-notices
                      {:count count})
        notices (vec (map seq-to-map rows))]
      notices)
    (catch SQLException e
      (log/error (str "Database error in get-latest-notices - " e))
      (throw e))))


(defn create-new-notice
  [notice]
  (try
    (a/update @init/ds
              sql/create-new-notice
              notice)
    (catch SQLException e
      (log/error (str "Database error in create-new-notice - " e))
      (throw e))))

(defn get-notice
  [notice-id]
  (try
    (let [seq-to-map (fn [seq] {:notice-id (nth seq 0)
                                :notice-type (nth seq 1)
                                :notice-subject (nth seq 2)
                                :notice (nth seq 3)})
          row (a/query a/fetch-single-row
                       @init/ds
                       sql/get-notice
                       {:notice-id notice-id})
          notice (seq-to-map row)]
      notice)
    (catch SQLException e
      (log/error (str "Database error in view-notice - " e))
      (throw e))))


(defn save-url
  [url-record]
  (try
    (let [url-id (a/genkey @init/ds
                           sql/save-url
                           url-record)]
      url-id)
    (catch SQLException e
      (log/error (str "Error in DB for save-url - " e))
      (throw e)))
  )

(defn ask-permission
  [url-record]
  (try
    (let [request-id (a/genkey @init/ds
                               sql/ask-permission
                               url-record)]
      request-id)
    (catch SQLException e
      (log/error (str "Error in DB for ask-permission - " e))
      -1))
  )

(defn get-permission-record
  [approval-token]
  (try
    (let [seq-to-map (fn [seq] {:url-id (nth seq 0)
                                :video-id (nth seq 1)
                                :title (nth seq 2)
                                :description (nth seq 3)
                                :thumbnail (nth seq 4)
                                :user-id (nth seq 5)
                                :approval-token (nth seq 6)
                                :approval-comments (nth seq 7)
                                :status (nth seq 8)})
          row (a/query a/fetch-single-row
                        @init/ds
                        sql/get-permission-record
                        {:approval-token approval-token})
          permission-record (seq-to-map row)]
      permission-record)
        (catch SQLException e
          (log/error (str "Database error in get-permission-record - " e))
          (throw e))))

(defn update-approval-status
  [approval-token approval-comments approval-status]
  (try
    (a/update @init/ds
              sql/update-approval-status
              {:approval-token approval-token
               :approval-comments approval-comments
               :approval-status approval-status})
    (catch SQLException e
      (log/error (str "Database error in update-approval-status - " e))
      (throw e)))
  )

(defn get-pending-permissions
  [user-id]
  (try
    (let [seq-to-map (fn [seq] {:url-id (nth seq 0)
                                :video-id (nth seq 1)
                                :title (nth seq 2)
                                :description (nth seq 3)
                                :thumbnail (nth seq 4)
                                :user-id (nth seq 5)
                                :approval-token (nth seq 6)
                                :approval-comments (nth seq 7)
                                :status (nth seq 8)})
          rows (a/query a/fetch-rows
                        @init/ds
                        sql/get-pending-permissions
                        {:user-id user-id})
          pending-list (vec (map seq-to-map rows))]
      pending-list)
    (catch SQLException e
      (log/error (str "Database error in pending-permissions - " e))
      (throw e)))
  )
