(ns health-patient.views.common
  (:require [re-frame.core :as rf]
            [health-patient.components.common :as common]))

(defn flash-messages []
  (let [flash @(rf/subscribe [:flash])]
    (when-not (empty? flash)
      [:header
       (for [msg flash]
         ^{:key msg} [:div [:mark [:strong msg]]])])))

(defn page [content]
  [:div
   [common/container
    [common/nav
     [common/nav-section [common/nav-item [:strong [common/link "/" "HealthPatient"]]]]
     [common/nav-section [common/nav-item [common/link "/patients" "All patients"]]]]]
   [common/container
    [common/card
     [flash-messages]
     content]]])

(defn not-found-page []
  [:div
   [:h1 "Not found"]
   [:p "Whoops, this page doesn't exist."]])
