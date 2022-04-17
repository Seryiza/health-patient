(ns health-patient.views.index)

(defn index-page []
  [:div
   [:h2 "Hello!"]
   [:p "It's a web app to manipulate patients. See " [:a {:href "/patients"} "list of patients"]]])
