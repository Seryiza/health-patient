(ns health-patient.views
  (:require [hiccup.page :as page]))

(defn reactive-page []
  (page/html5 {:lang "en"}
    [:head
     [:meta {:charset "UTF-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title "Health Patient"]
     (page/include-css "/css/app.css")]
    [:body
     [:div#app]
     (page/include-js "/js/app.js")]))
