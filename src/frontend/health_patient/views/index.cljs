(ns health-patient.views.index
  (:require [health-patient.components.common :as common]))

(defn index-page []
  [:div
   [common/h2 "Hello!"]
   [:p "It's a web app to manipulate patients. See " [common/link "/patients" "list of patients"]]])
