(ns com.ytk.layout
  (:require [selmer.parser :as parser]
            [cambium.core  :as log]
            [selmer.filters :as filters]
            [clojure.java.io :as io]
            [ring.util.http-response :refer :all]
            [ring.util.response :refer [redirect]]
            [clojure.string :as str]
            [markdown.core :refer [md-to-html-string]]
            [ring.middleware.content-type :as content-typer]
            [ring.middleware.resource :refer [resource-request]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [clojure.data.json :as json]))

(declare ^:dynamic *app-context*)
;(def static-base-url "/static")
(parser/set-resource-path!  (clojure.java.io/resource "public"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))
(filters/add-filter! :markdown (fn [content] [:safe (md-to-html-string content)]))
(selmer.parser/cache-on!)

(defn render
  "renders the HTML template located relative to resources/templates"
  [template & [params]]
  (let [file (str "templates/" template)]
    (log/debug (str "Template file to load " file))
   (content-type
   (ok
    (parser/render-file
     file
     (assoc params
            :page template
            :csrf-token *anti-forgery-token*
            :servlet-context *app-context*)))
   "text/html; charset=utf-8")))

(defn render-static
  [req]
  (content-typer/content-type-response
    (resource-request req "public")
    req
    )
  )

; https://github.com/metosin/ring-http-response gives me the list of ok etc.

(defn rest-json-ok
  "Send 200 response for rest apis"
  [content-data & [params]]
  (content-type
   (ok
    (parser/render
     (json/write-str content-data)
     params)
    )
   "application/json; charset=utf-8"))

(defn rest-json-created
  "Send 201 response for rest apis"
  [content-data url & [params]]
  (content-type
   (created
    url
    (parser/render
     (json/write-str content-data)
     params)
    )
   "application/json; charset=utf-8"))

(defn rest-json-not-found
  "Send 404 response for rest apis"
  [content-data & [params]]
  (content-type
   (not-found
    (parser/render
     (json/write-str content-data)
     params)
    )
   "application/json; charset=utf-8"))

(defn rest-json-500
  "Send 400 response for rest apis"
  [content-data & [params]]
  (content-type
   (bad-request
    (parser/render
     (json/write-str content-data)
     params)
    )
   "application/json; charset=utf-8"))

(defn redirect-url
  "Redirect to input url"
  [url]
  (redirect url))
