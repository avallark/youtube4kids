(ns com.ytk.sql
  (:require [asphalt.core :as a]))
(a/defsql login-verify "select user_id, user_name, full_name, user_title, email_id, user_role from users where email_id = $email-id and password = md5($password)")
(a/defsql get-employee-by-id "select user_id, user_name, full_name, user_title, email_id, user_role, phone1 from users where user_id = $employee-id")
(a/defsql get-all-users "select user_id, full_name, phone1, user_role from users where status = 'A';")
;; employees
(a/defsql get-all-employees "select user_id, user_name, full_name, user_title, email_id, user_role, phone1 from users where status = 'A';")
(a/defsql get-all-sales "select user_id, user_name, full_name, user_title, email_id, user_role, phone1 from users where status = 'A' and user_role <> 'S';")
(a/defsql get-sales-rep-user-id-by-shortname "select user_id from users where upper(user_name) = upper($shortname);")
;; setup
(a/defsql change-password "update users set password = md5($password) where email_id = $email-id;")
;; notify
(a/defsql save-notify-record "insert into notify_records (user_id, group_flag, message, phone, notify_method, notify_type, results) values ($user-id, $group-flag, $message, $phone, $notify-method, $notify-type, $results);")

(a/defsql get-latest-notices "select notice_id, notice_subject, notice, notice_type from notices order by creation_date desc limit $count;")

(a/defsql create-new-notice "insert into notices (notice_subject, notice, notice_type, created_by) values ($notice-subject, $notice, $notice-type, $created-by);")

(a/defsql get-notice "select notice_id, notice_type, notice_subject, notice from notices where notice_id = $notice-id;")

(a/defsql save-url "insert into url_details (video_id, video_title, video_description, video_thumbnail) values ($video-id, $title, $description, $thumbnail)")

(a/defsql ask-permission "insert into url_approval_requests (url_id, user_id, approval_token) values ($url-id, $user-id, $approval-token)")

(a/defsql get-pending-permissions "select u.url_id, u.video_id, u.video_title, u.video_description, u.video_thumbnail, a.user_id, a.approval_token, a.approval_comments, a.status from url_details u, url_approval_requests a where a.user_id = $user-id and a.status= 'P';")

(a/defsql get-permission-record "select u.url_id, u.video_id, u.video_title, u.video_description, u.video_thumbnail, a.user_id, a.approval_token, a.approval_comments, a.status from url_details u, url_approval_requests a where a.approval_token = $approval-token and a.url_id = u.url_id;")

(a/defsql update-approval-status "update url_approval_requests set approval_comments = $approval-comments, status = $approval-status where approval_token = $approval-token;")
