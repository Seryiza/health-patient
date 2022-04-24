(ns health-patient.views.common
  (:require [re-frame.core :as rf]))

(defn flash-messages []
  (let [flash @(rf/subscribe [:flash])]
    (when-not (empty? flash)
      [:header
       (for [msg flash]
         ^{:key msg} [:div [:mark [:strong msg]]])])))

(defn page [content]
  [:div
   [:nav.container
    [:ul [:li [:strong [:a {:href "/"} "HealthPatient"]]]]
    [:ul [:li [:a {:href "/patients"} "All patients"]]]]
   [:section.container
    [:article
     [flash-messages]
     content]]])

(defn not-found-page []
  [:div
   [:h1 "Not found"]
   [:p "Whoops, this page doesn't exist."]])
