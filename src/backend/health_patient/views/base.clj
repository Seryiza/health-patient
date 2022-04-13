(ns health-patient.views.base
  (:require [hiccup.page :as page]
            [hiccup.element :as elem]))

(defn base-template [content]
  (page/html5 {:lang "en"}
    [:head
     [:meta {:charset "UTF-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title "Health Patient"]
     (page/include-css "https://unpkg.com/@picocss/pico@latest/css/pico.min.css")
     (page/include-css "/css/app.css")]
    [:body
     [:nav.container
      [:ul [:li [:strong (elem/link-to "/" "HP")]]]
      [:ul [:li (elem/link-to "/patients" "All patients")]]]
     [:section.container
      [:article content]]
     (page/include-js "/js/main.js")]))
