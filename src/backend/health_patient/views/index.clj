(ns health-patient.views.index
  (:require [hiccup.element :as elem]
            [health-patient.views.base :as base]))

(defn index-page []
  (base/base-template
    [:div
     [:h2 "Hello!"]
     [:p
      "It's a web app to manipulate patients. See "
      (elem/link-to "/patients" "list of patients")]]))
