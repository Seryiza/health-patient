(ns health-patient.patients.form-page
  (:require [dommy.core :as dommy]
            [ajax.core :as ajax]))

(def form-input-names
  [:first_name
   :last_name
   :middle_name
   :sex
   :birth_date
   :address
   :cmi_number])

(defn form-listen! [form handler]
  (dommy/listen! form :submit (fn [event]
                                (.preventDefault event)
                                (handler event))))

(defn save-patient! [patient-id patient-data]
  (ajax/PUT (str "/patients/" patient-id)
            {:params patient-data
             :format (ajax/json-request-format)}))

(defn create-patient! [patient-data]
  (ajax/POST "/patients"
             {:params patient-data
              :format (ajax/json-request-format)}))

(defn get-form-patient-id [form]
  (dommy/attr form :data-patient-id))

(defn get-form-value [form input-name]
  (let [input-sel (str "[name=" (name input-name) "]")]
    (when-let [input (dommy/sel1 form input-sel)]
      (dommy/value input))))

(defn get-patient-data [form]
  (->> form-input-names
       (map #(hash-map % (get-form-value form %)))
       (apply merge)))

(defn init []
  (doseq [form (dommy/sel "[data-save-patient-form]")]
    (form-listen! form #(save-patient! (get-form-patient-id form) (get-patient-data form))))
  (doseq [form (dommy/sel "[data-create-patient-form]")]
    (form-listen! form #(create-patient! (get-patient-data form)))))

(init)
