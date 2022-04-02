(ns health-patient.patients.views.show
  (:require [health-patient.views.base :as base]
            [health-patient.patients.views.common :as common]
            [hiccup.element :as elem]
            [hiccup.core :refer [h]]))

(defn patient-field [patient field-fn label]
  [:p
   [:b (str label ": ")]
   (-> patient field-fn h)])

(defn show-page [patient]
  (base/base-template
    [:div
     [:hgroup
      [:h2 (str "Patient #" (:id patient))]
      [:h3 (-> patient common/patient-full-name h)]]
     [:section
      (elem/link-to {:role "button"}
                    (str "/patients/" (:id patient) "/edit")
                    "Edit current patient")]
     (patient-field patient :first_name "First name")
     (patient-field patient :middle_name "Middle name")
     (patient-field patient :last_name "Last name")
     (patient-field patient :sex "Sex")
     (patient-field patient :birth_date "Birth date")
     (patient-field patient :address "Address")
     (patient-field patient :cmi_number "CMI number")]))
