(ns health-patient.patients.form-page
  (:require [dommy.core :as dommy]
            [ajax.core :as ajax]
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

(defn save-patient! [patient-id patient-data]
  (ajax/PUT (str "/patients/" patient-id)
            {:params patient-data
             :handler #(utils/redirect (str "/patients/" patient-id))
             :error-handler utils/common-error-handler
             :format :json
             :keywords? true}))

(defn create-patient! [patient-data]
  (ajax/POST "/patients"
             {:params patient-data
              :handler #(utils/redirect (str "/patients/" (:id %)))
              :error-handler utils/common-error-handler
              :format :json
              :response-format :json
              :keywords? true}))

(defn init []
  (doseq [form (dommy/sel "[data-save-patient-form]")]
    (utils/form-listen! form #(save-patient! (get-form-patient-id form)
                                             (utils/get-form-values form patient-input-names))))
  (doseq [form (dommy/sel "[data-create-patient-form]")]
    (utils/form-listen! form #(create-patient! (utils/get-form-values form patient-input-names)))))
