(ns health-patient.patients.views.form
  (:require [health-patient.views.base :as base]
            [hiccup.form :as form]))

(def shown-sex-labels [["Not known" "not-known"]
                       ["Male" "male"]
                       ["Female" "female"]
                       ["Not applicable" "not-applicable"]])

(defn date-input [name value]
  [:input {:type "date"
           :name name
           :value value}])

(defn form-page [{:keys [patient patient-exist?]}]
  (base/base-template
    [:div
     (if patient-exist?
       [:h2 (str "Patient #" (:id patient))]
       [:h2 "New patient"])
     [:form (if patient-exist?
              {:data-save-patient-form true
               :data-patient-id (:id patient)}
              {:data-create-patient-form true})
      [:div.errors ""]
      [:div.grid
       [:div
        (form/label "first_name" "First name:")
        (form/text-field "first_name" (:first_name patient))]
       [:div
        (form/label "middle_name" "Middle name:")
        (form/text-field "middle_name" (:middle_name patient))]
       [:div
        (form/label "last_name" "Last name:")
        (form/text-field "last_name" (:last_name patient))]]

      [:div.grid
       [:div
        (form/label "sex" "Sex:")
        (form/drop-down "sex" shown-sex-labels (:sex patient))]
       [:div
        (form/label "birth_date" "Birth date:")
        (date-input "birth_date" (:birth_date patient))]]

      [:div
       (form/label "address" "Address:")
       (form/text-field "address" (:address patient))]

      [:div
       (form/label "cmi_number" "CMI number:")
       (form/text-field "cmi_number" (:cmi_number patient))]

      [:button {:type "submit"}
       (if patient-exist? "Save" "Create")]]]))
