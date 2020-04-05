(ns com.ytk.email
  (:require [cambium.core :as log]
            [postal.core :as postal]
            [com.ytk.config :as config]))

(def smtp (atom nil))

(defn init
  []
  (reset! smtp (config/value :smtp))
  )

(defn send-email
  [to-email subject body]
  (try
    (postal/send-message {:host (:host @smtp)
                          :port (:port @smtp)
                          :user (:user @smtp)
                          :pass (:pass @smtp)
                          :tls true}
                         {:from (:from @smtp)
                          :to to-email
                          :subject subject
                          :body body})
    (log/info {} (str "Email service - sending email to " to-email " from " (:from @smtp)))
    (catch Exception e
      (log/error (str "Email exception - " (.getMessage e)))
      (throw e)))
  )

(defn send-approval-email
  [approver-email url-record user base-url]
  (let [to approver-email
        subject (str "Request for Permission for Video from " (:user-full-name user))
        body [:alternative
              {:type "text/html"
               :content (str "<html><head> </head><body>"
                            "<h1>Request for Permission for Video</h1>"
                            "<p> Title : " (:title url-record) "</p>"
                            "<p> Description : " (:description url-record) "</p>"
                            "<img src='" (:thumbnail url-record) "'>" 
                            "<p>Video if you want to watch : https://www.youtube.com/watch?v=" (:video-id url-record) "</p>"
                            "<p> Please click here to approve the request : <a href=\""
                            base-url "/nologin/permission/approval/"
                            (:approval-token url-record)
                            "\">Approval Link.</a></p>"
                            "</body></html>")}]]
    (try
      (send-email to subject body)
      (catch Exception e
        (log/error (str "Email error in sending email - " (.getMessage e)))
        (throw e)))))

(defn send-approved-email
  [employee-email leave-record]
  (let [to employee-email
        subject (str "Leave Approved!")
        body [:alternative
              {:type "text/html"
               :content (str "<html><head> </head><body>"
                             "<h1>Leave approved</h1>"
                             "<p> Leave applied from : " (:date-on leave-record) "</p>"
                             "<p> Leave applied for : " (:no-of-days leave-record) "</p>"
                             "<p> Type of leave : " (:leave-type leave-record) "</p>"
                             "<p> Reason for this leave request : " (:leave-reason leave-record) "</p>"
                             "<p> Comments from the approver : " (:approval-comments leave-record) "</p>"
                             "</body></html>")}]]
    (try
      (send-email to subject body)
      (catch Exception e
        (log/error (str "Email error in send-approved-email - " (.getMessage e)))
        (throw e)))))

(defn send-rejected-email
  [employee-email leave-record]
  (let [to employee-email
        subject (str "Leave Rejected!")
        body [:alternative
              {:type "text/html"
               :content (str "<html><head> </head><body>"
                             "<h1>Leave rejected</h1>"
                             "<p> Leave applied from : " (:date-on leave-record) "</p>"
                             "<p> Leave applied for : " (:no-of-days leave-record) "</p>"
                             "<p> Type of leave : " (:leave-type leave-record) "</p>"
                             "<p> Reason for this leave request : " (:leave-reason leave-record) "</p>"
                             "<p> Comments from the approver : " (:approval-comments leave-record) "</p>"
                             "</body></html>")}]]
    (try
      (send-email to subject body)
      (catch Exception e
        (log/error (str "Email error in send-rejected-email - " (.getMessage e)))
        (throw e)))))
