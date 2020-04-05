(ns com.ytk.handlers.handler
  (:require
   [clj-time.core :as t]
   [crypto.random :as cr]
   [clj-time.format :as f]
   [clj-http.client :as http]
   [com.ytk.config :as config]
   [ring.util.response :refer [resource-response response]]
   [com.ytk.db :as db]
   [clojure.pprint :as pp]
   [com.ytk.layout :as layout]
   [com.ytk.email :as email]
   [cheshire.core :as json]
   [cambium.core  :as log]
   [ring.util.codec :as codec]
   [com.ytk.notify :as notify]
   [clojure.java.shell :as shell]
   [clojure.core.async :refer [go]]
   ))

(def web-formatter (f/formatter "yyyy-MM-dd"))

(defn- create-approval-token
  []
  (cr/hex 16))

(defn test-handler
  [req a]
  (layout/rest-json-ok {:status 1
                        :message a}))


(defn root-handler
  [req]
  {:status 302
   :headers {"Location" "/home"}}
  )

(defn static-handler
  [req]
  (->
   (layout/render-static req)
   )
  )


(defn get-all-users-handler
  [req]
  (try (let [all-users (db/get-all-users)]
         (layout/rest-json-ok {:status 1
                               :message "Users fetched successfully"
                               :users all-users}))
       (catch Exception e
         (log/error (str "Exception in get-all-users-handler - " e))
         (layout/rest-json-ok {:status 0
                               :message (str "Error in Users fetch - " (.getMessage e))}))))

(defn login-handler
  [req]
  (if (get-in req [:session "login"])
    {:status 302
     :headers {"Location" "/home"}}
    (layout/render "login.html" {:req req
                                 :site-title "Tz"
                                 :headers {"Service-Worker-Allowed" "/"}})
    ))

(defn session-handler
  [req]
  {:body (pr-str (:session req))
   :status 200})

(defn logout-handler [request]
  {:body "Logged out."
   :session nil
   :headers {"Location" "/home"}
   :status 302})


(defn logged-in-handler
  [req]
  (try
    (let [uri-redirect (get-in req [:params :uri-redirect])
          user (db/validate-login (get-in req [:params :user]) (get-in req [:params :password]))]
      (if user
        {:status 302
         :headers {"Location" (if (nil? uri-redirect) "/home" uri-redirect)}
         :session {"login" "true"
                   :user user}}
        (layout/render "login.html" {:error "Invalid username and/or password. Try again."
                                     :req req})
        )
      )
    (catch Throwable throwable
      (log/error (.getMessage throwable))
      (layout/render "login.html" {:error "Invalid Username and/or password. Try again"
                                   :req req}))))

(defn cookies-handler
  [req]
  {:status 200
   :body (str "cookies : " (get-in req [:cookies]))})


;; setup

(defn setup-handler
  [req]
  (let [email-id (get-in req [:session :user :email-id])]
    (layout/render "setup.html" {:email-id email-id
                                 :req req
                                 :page-title "Setup"})))

(defn validate-login-handler
  [req]
  (try (let [user (db/validate-login (get-in req [:params :user]) (get-in req [:params :password]))]
         (if user
           (layout/rest-json-ok {:status 1
                                 :user user})
           (layout/rest-json-ok {:status 0
                                 :message "Login Failed."})))
       (catch Throwable throwable
         (log/error (.getMessage throwable))
         (layout/rest-json-ok {:status 0
                               :message "Login Failed"})))
  )

(defn change-password
  [req]
  (try
    (let [out (db/change-password (get-in req [:params :user]) (get-in req [:params :password]))]
      (log/info (pr-str out))
      (layout/rest-json-ok {:status 1
                            :message "Password changed"}))
    (catch Throwable throwable
      (log/error (str "DB: Error changing password - " (.getMessage throwable)))
      (layout/rest-json-ok {:status 0
                            :message (str "Password unchanged - " (.getMessage throwable))}))))


(defn- next-month
  []
  (let [month-today (t/month (t/today))
        month (+ 1 month-today)]
    (if (= month 13)
      1
      month)))

(defn- last-month
  []
  (let [month-today (t/month (t/today))
        month (- 1 month-today)]
    (if (= month 1)
      12
      month)))

(defn this-month
  []
  (let [month-today (t/month (t/today))]
    month-today))


(defn new-home-handler-page
  [req]
  (let [a 1]
    (layout/render "newhome.html" {:a a
                                 :req req
                                 :page-title "Home"})))
(defn new-home-handler1
  [req]
  (let [search-string (get-in req [:params :search-string])
        user-id (get-in req [:session :user :user-id])
        youtube-apikey (config/value :youtube-apikey)
        youtube-url (config/value :youtube-url)
        results (http/get youtube-url
                          {:accept :json
                           :content-type :json
                           :query-params {"part" "id,snippet"
                                    "q" search-string
                                    "safeSearch" "none"
                                    "maxResults" 10
                                          "key" youtube-apikey}})
        select-map (fn [m] {:video-id (get-in m [:id :videoId])
                            :title (get-in m [:snippet :title])
                            :description (get-in m [:snippet :description])
                            :thumbnail (get-in m [:snippet :thumbnails :medium :url])})
        res-list (vec (map select-map (get-in results [:items])))]
    (log/info (pr-str res-list))
    res-list))

;    (layout/render "home.html" {:search-string search-string
;                                :req req
;                                :results results
;                                :page-title "Home"})

(defn new-home-handler
  [req]
  (let [search-string (get-in req [:params :search-string])
        user-id (get-in req [:session :user :user-id])
        youtube-apikey (config/value :youtube-apikey)
        youtube-url (config/value :youtube-url)
        results (http/get youtube-url
                          {:accept :json
                           :content-type :json
                           :query-params {"part" "id,snippet"
                                    "q" search-string
                                    "safeSearch" "strict"
                                    "maxResults" 10
                                          "key" youtube-apikey}})
        resjson (json/parse-string (:body results) true)
        select-map (fn [m] {:video-id (get-in m [:id :videoId])
                            :title (get-in m [:snippet :title])
                            :description (get-in m [:snippet :description])
                            :thumbnail (get-in m [:snippet :thumbnails :medium :url])})
        res-items (:items resjson)
        items (vec (map select-map res-items))
        ]
    (layout/render "home.html" {:search-string search-string
                                :req req
                                :results items
                                :page-title "Search results"})))


(defn pending-permissions
  [req]
  (let [user-id (get-in req [:session :user :user-id])
        pending-permissions (db/get-pending-permissions user-id)]
    (layout/render "permissions.html" {:req req
                                       :page-title "Pending Permissions"
                                       :pending-permissions pending-permissions})))

(defn ask-permission
  [req]
  (let [url-record {:video-id (get-in req [:params :video-id])
                    :title (get-in req [:params :title])
                    :description (get-in req [:params :description])
                    :thumbnail (get-in req [:params :thumbnail])}
        user-id (get-in req [:session :user :user-id])
        user (get-in req [:session :user])
        approval-token (cr/hex 16)
        url-id (db/save-url url-record)
        b (db/ask-permission (assoc url-record :user-id user-id :approval-token approval-token :url-id url-id))
        pending-permissions (db/get-pending-permissions user-id)]
    (go (email/send-approval-email (config/value :approver-email)
                                   (assoc url-record :approval-token approval-token)
                                   user
                                   (config/value :base-url)))
    (layout/render "permissions.html" {:req req
                                       :page-title "Pending Permissions"
                                       :url-id url-id
                                       :ask-permission b
                                       :pending-permissions pending-permissions})))


(defn permission-approval-handler
  [{:keys [approval-token] :as req}]
  (try
    (log/info (pr-str (db/get-permission-record approval-token)))
    (let [url-record (db/get-permission-record approval-token)
          user-record (db/get-employee-by-id (:user-id url-record))]
      (layout/render "permission-approval.html" {:url-record url-record
                                            :user user-record}))
    (catch Throwable throwable
      (log/error (str "Error in handler for leave approval for " approval-token " - " (.getMessage throwable)))
      (layout/rest-json-500 "Invalid Approval Token. Adios!"))))

(defn permission-approval-post-handler
  [req]
  (log/info (str "permission-approval-post approval-token received is - " (get-in req [:params :approval-token])))
  (let [approval-token (get-in req [:params :approval-token])
        url-record (db/get-permission-record approval-token)
        user (db/get-employee-by-id (:user-id url-record))
        approval-comments (get-in req [:params :approval-comments])
        approval-status (get-in req [:params :approval-status])
        record (assoc url-record
                      :approval-comments approval-comments)]
    (try
      (db/update-approval-status approval-token approval-comments approval-status)
      (if (= approval-status "A")
        (do
                                        ; download youtube video to plex folder
          (go (shell/sh "scripts/getVideo.sh" (str "https://www.youtube.com/watch?v=" (:video-id record))))
          (go (email/send-approved-email (:email-id user) record))
          (layout/rest-json-ok "Leaves approved and marked."))
        (do
          (go (email/send-rejected-email (:email-id user) record))
          (layout/rest-json-ok "Leave rejected and marked.")))
      (catch Exception e
        (log/error (str "Error in handler for insert leaves - " (.getMessage e)))
        (layout/rest-json-500 (str "approval rejection not processed - " (.getMessage e)))))))
