(ns com.ytk.main
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [com.ytk.config :as config]
            [com.ytk.handlers.handler :as handler]
            [com.ytk.handlers.notice :as notice]
            [com.ytk.email :as email]
            [com.ytk.layout :as layout]
            [com.ytk.init :as init]
            [ring.util.response :refer [resource-response response content-type]]
            [ring.util.json-response :as json-response]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.not-modified :as not-modified]
            [ring.middleware.resource :as resource]
            [ring.middleware.session :as session]
            [ring.middleware.session.memory :as memory]
            [cheshire.core :as json]
            [com.ytk.db :as db]
            [com.ytk.config :as config]
            [calfpath.core  :refer [->uri ->method ->get ->head ->options ->patch ->put ->post ->delete]]
            [calfpath.route :as r]
            [cambium.codec :as codec]
            [cambium.core  :as log]
            [clojure.string :as str]
            [cambium.logback.json.flat-layout :as flat]
            [com.ytk.notify :as notify]
            )
  )

(defn simple-log-middleware [handler]
  (fn [{:keys [uri params] :as request}]
    (if (str/starts-with? uri "/static/")
      (handler request)
      (do
        (log/info (str "Request uri is " uri " of type " (get-in request [:request-method])))
        (handler request))
      )
    ))

(defn logged-in-middleware [handler]
  (fn [{:keys [uri session] :as request} ]
    (log/debug (str "In logged middleware with session - " (pr-str request) " - " uri))
    (let [uri (get-in request [:uri])
          session (get-in request [:session])](if-not (or
             (= uri "/login")
             (= uri "/logout")
             (str/starts-with? uri "/api/v0/")
             (str/starts-with? uri "/nologin/")
             (str/starts-with? uri "/static/")
             (str/starts-with? uri "/favicon.ico")
             (= uri "/session")
             )
      (if-not (= (get-in request [:session "login"]) "true")
        {:status 302
         :headers {"Location" (str "/login?uri-redirect=" uri)}}
        (handler request))
      (handler request)
      ))
    )
  )

(defn wrap-return-favicon [handler]
  (fn [req]
    (if (= [:get "/favicon.ico"] [(:request-method req) (:uri req)])
      (resource-response "favicon.ico" {:root "public/static/img"})
      (handler req))))

(defn wrap-return-manifest-pwa [handler]
  (fn [req]
    (if (= [:get "/manifest.json"] [(:request-method req) (:uri req)])
      (resource-response "manifest.json" {:root "public/static/data"})
      (handler req))))

(defn wrap-return-js-pwa [handler]
  (fn [req]
    (if (= [:get "/pwabuilder-sw.js"] [(:request-method req) (:uri req)])
      (content-type (resource-response "pwabuilder-sw.js" {:root "public/static/js"}) "application/javascript")
      (handler req))))

(defn app-routes
  "Return a vector of route specs."
  []
  (let [a "Test"]
    [{:uri "/test"
      :method :get
      :handler (fn [request]
                 (try
                   (handler/test-handler request a)
                   (catch Exception e
                     (log/error {} e (str "Error: " e))
                     (layout/rest-json-500 "Internal server error"))))}
     {:uri "/"
      :method :get
      :handler handler/root-handler}
     {:uri "/home"
      :nested [{:method :get
                :handler handler/new-home-handler-page}
               {:method :post
                :handler handler/new-home-handler}]
      }
     {:uri "/static/*"
      :method :get
      :handler handler/static-handler}
     {:uri "/login"
      :nested [{:method :get
                :handler handler/login-handler}
               {:method :post
                :handler handler/logged-in-handler}
               ]
      }
     {:uri "/session"
      :method :get
      :handler handler/session-handler}
     {:uri "/cookies"
      :method :get
      :handler handler/cookies-handler}
     {:uri "/logout"
      :method :get
      :handler handler/logout-handler}
     {:uri "/api/v0/users/all"
      :method :get
      :handler handler/get-all-users-handler}
                                        ; setup
     {:uri "/setup"
      :method :get
      :handler handler/setup-handler}
     {:uri "/api/v0/user/login"
      :method :post
      :handler handler/validate-login-handler}
     {:uri "/api/v0/user/password/change"
      :method :post
      :handler handler/change-password}
     {:uri "/notices"
      :nested [{:method :get
                :handler notice/notices}
               {:method :post
                :handler notice/create-new-notice}]}
     {:uri "/notice/:notice-id/view"
      :method :get
      :handler notice/view-notice}
     {:uri "/api/v0/notice/:notice-id/view"
      :method :get
      :handler notice/view-notice-api}
     {:uri "/notices-e"
      :method :get
      :handler notice/notices-non-admin}
     {:uri "/askpermission"
      :method :get
      :handler handler/ask-permission}
     {:uri "/permissions/pending"
      :method :get
      :handler handler/pending-permissions}
     {:uri "/nologin/permission/approval/:approval-token"
      :nested [{:method :get
                :handler handler/permission-approval-handler}
               {:method :post
                :handler handler/permission-approval-post-handler}]}
     ])
  )

(defn json-error-handler
  [handler]
  (fn [request]
    (try (handler request)
         (catch Throwable throwable
           (assoc (json-response/json-response {:message (.getMessage throwable)
                                                :stacktrace (map str (.getStackTrace throwable))})
                  :status 500)))))


(def ring-handler
  (-> (app-routes)
      r/compile-routes
      r/make-dispatcher
      logged-in-middleware
      ring.middleware.keyword-params/wrap-keyword-params
      ring.middleware.params/wrap-params
      ring.middleware.multipart-params/wrap-multipart-params
      (session/wrap-session {:cookie-name "ring-session"
                                             :root "/"
                                             :cookie-attrs {:max-age 60000
                                                            :secure false}
                                             :store (memory/memory-store)})
      ring.middleware.cookies/wrap-cookies
      json-error-handler
      wrap-return-js-pwa
      wrap-return-manifest-pwa
      wrap-return-favicon
      simple-log-middleware
      )
  )

(defn init
  []
  (flat/set-decoder! codec/destringify-val)
  (log/info "Application started. Let the good times roll.")
  (config/init)
  (init/init)
  (email/init)
  (notify/init)
  )
