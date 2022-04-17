(ns health-patient.views.common)

(defn page [content]
  [:div
   [:nav.container
    [:ul [:li [:strong [:a {:href "/"} "HealthPatient"]]]]
    [:ul [:li [:a {:href "/patients"} "All patients"]]]]
   [:section.container
    [:article content]]])
