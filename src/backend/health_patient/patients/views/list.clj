(ns health-patient.patients.views.list
  (:require [health-patient.views.base :as base]
            [hiccup.element :as elem]))

(defn patient-row [patient]
  [:tr {:data-patient-id (:id patient)}
   [:td (:id patient)]
   [:td (:cmi_number patient)]
   [:td (str (:first_name patient) " " (:middle_name patient) " " (:last_name patient))]
   [:td
    (elem/link-to {:role "button"}
                  (str "/patients/" (:id patient))
                  "Details")
    (elem/link-to {:role "button"}
                  (str "/patients/" (:id patient) "/edit")
                  "Edit")
    [:button.secondary {:data-delete-patient-button true
                        :data-patient-id (:id patient)} "Delete"]]])

(defn list-page [patients]
  (base/base-template
    [:div
     [:h2 "Patients"]
     (elem/link-to {:role "button"} "/create-patient" "Create new patient")
     [:table {:role "grid"}
      [:thead
       [:tr
        [:th "ID"]
        [:th "CMI number"]
        [:th "Full name"]
        [:th "Actions"]]]
      [:tbody
       (map patient-row patients)]]]))
