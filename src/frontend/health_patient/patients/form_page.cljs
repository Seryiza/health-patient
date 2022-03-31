(ns health-patient.patients.form-page
  (:require [dommy.core :as dommy]
            [struct.core :as st]
            [health-patient.schemes :as schemes]
            [health-patient.utils :as utils]
            [health-patient.patients.api :as patients-api]))

(def patient-input-names
  [:first_name
   :last_name
   :middle_name
   :sex
   :birth_date
   :address
   :cmi_number])

(defn get-form-patient-id [form]
  (dommy/attr form :data-patient-id))

(defn try-save-patient! [form]
  (let [patient-id (get-form-patient-id form)
        form-data (utils/get-form-values form patient-input-names)
        [validation-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? validation-errors)
      (patients-api/save-patient! patient-id patient-data)
      (utils/show-form-errors form validation-errors))))

(defn try-create-patient! [form]
  (let [form-data (utils/get-form-values form patient-input-names)
        [validation-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? validation-errors)
      (patients-api/create-patient! patient-data)
      (utils/show-form-errors form validation-errors))))

(defn init []
  (doseq [form (dommy/sel "[data-save-patient-form]")]
    (utils/form-listen! form #(try-save-patient! form)))
  (doseq [form (dommy/sel "[data-create-patient-form]")]
    (utils/form-listen! form #(try-create-patient! form))))
