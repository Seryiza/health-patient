(ns health-patient.patients.form-page
  (:require [dommy.core :as dommy]
            [ajax.core :as ajax]
            [struct.core :as st]
            [health-patient.schemes :as schemes]
            [health-patient.utils :as utils]))

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

(defn get-field-input [form input-name]
  (dommy/sel1 form (utils/selector-by-name input-name)))

(defn get-field-error [form input-name]
  (dommy/sel1 form (str "[data-error-for='" (name input-name) "']")))

(defn clear-form-errors [form]
  (doseq [field-input (dommy/sel form (str "[aria-invalid='true']"))]
    (dommy/set-attr! field-input :aria-invalid ""))
  (doseq [field-error (dommy/sel form "[data-error-for]")]
    (dommy/set-text! field-error "")))

(defn show-form-errors [form errors]
  (clear-form-errors form)
  (doseq [[input-name error-msg] errors]
    (when-let [field-input (get-field-input form input-name)]
      (when-let [field-error (get-field-error form input-name)]
        (dommy/set-attr! field-input :aria-invalid "true")
        (dommy/set-text! field-error error-msg)))))

(defn save-patient! [patient-id patient-data]
  (ajax/PUT (str "/patients/" patient-id)
            {:params patient-data
             :handler #(utils/redirect (str "/patients/" patient-id))
             :error-handler utils/common-error-handler
             :format :json
             :keywords? true}))

(defn try-save-patient! [form]
  (let [patient-id (get-form-patient-id form)
        form-data (utils/get-form-values form patient-input-names)
        [validation-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? validation-errors)
      (save-patient! patient-id patient-data)
      (show-form-errors form validation-errors))))

(defn create-patient! [patient-data]
  (ajax/POST "/patients"
             {:params patient-data
              :handler #(utils/redirect (str "/patients/" (:id %)))
              :error-handler utils/common-error-handler
              :format :json
              :response-format :json
              :keywords? true}))

(defn try-create-patient! [form]
  (let [form-data (utils/get-form-values form patient-input-names)
        [validation-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? validation-errors)
      (create-patient! patient-data)
      (show-form-errors form validation-errors))))

(defn init []
  (doseq [form (dommy/sel "[data-save-patient-form]")]
    (utils/form-listen! form #(try-save-patient! form)))
  (doseq [form (dommy/sel "[data-create-patient-form]")]
    (utils/form-listen! form #(try-create-patient! form))))
