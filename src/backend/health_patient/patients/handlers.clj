(ns health-patient.patients.handlers
  (:require [ring.util.response :as response]
            [struct.core :as st]
            [health-patient.html :as html]
            [health-patient.db :refer [db]]
            [health-patient.patients.db :as patients]))

(def +patient-scheme+
  {:first_name [st/required st/string]
   :last_name [st/required st/string]
   :middle_name [st/required st/string]
   :sex [st/required st/string]
   :birth_date [st/required st/string]
   :address [st/required st/string]
   :cmi_number [st/required st/number-str]})

(defn show-all-patients [request]
  (let [all-patients (patients/all-patients db)]
    (html/render request "patients/list.html" {:patients all-patients})))

(defn show-patient [request]
  (let [patient-id (-> request :path-params :id)
        patient (patients/patient-by-id db {:id patient-id})]
    (if (nil? patient)
      (response/not-found "Patient not found.")
      (html/render request "patients/show.html" {:patient patient}))))

(defn show-create-form [request]
  (html/render request "patients/form.html"))

(defn show-edit-form [request]
  (let [patient-id (-> request :path-params :id)
        patient (patients/patient-by-id db {:id patient-id})]
    (if (nil? patient)
      (response/not-found "Patient not found.")
      (html/render request "patients/form.html" {:patient-exist? true
                                                 :patient patient}))))

(defn delete-patient [request]
  (let [patient-id (-> request :path-params :id)
        affected-patients (patients/delete-patient-by-id db {:id patient-id})]
    (if (zero? affected-patients)
      (response/not-found "Patient not found.")
      (response/response "Patient deleted."))))

(defn update-patient [request]
  (let [patient-id (-> request :path-params :id)
        form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data +patient-scheme+)
        patient-data-with-id (assoc patient-data :id patient-id)]
    (if (empty? form-errors)
      (do
        (patients/update-patient-by-id db patient-data-with-id)
        (response/response nil))
      (response/bad-request {:form-errors form-errors}))))

(defn create-patient [request]
  (let [form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data +patient-scheme+)]
    (if (empty? form-errors)
      (do
        (patients/insert-patient db patient-data)
        (response/created nil))
      (response/bad-request {:form-errors form-errors}))))
