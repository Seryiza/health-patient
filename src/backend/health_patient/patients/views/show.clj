(ns health-patient.patients.views.show
  (:require [health-patient.views.base :as base]
            [hiccup.element :as elem]))

(defn patient-field [patient field label]
  [:p [:b (str label ": ")] (field patient)])

(defn show-page [patient]
  (base/base-template
    [:div
     [:hgroup
      [:h2 (str "Patient #" (:id patient))]
      [:h3 (str (:first_name patient) " " (:middle_name patient) " " (:last_name patient))]]
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
