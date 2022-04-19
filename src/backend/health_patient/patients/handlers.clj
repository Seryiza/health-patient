(ns health-patient.patients.handlers
  (:require [ring.util.response :as response]
            [struct.core :as st]
            [health-patient.db :refer [db]]
            [health-patient.schemes :as schemes]
            [health-patient.patients.db :as patients]))

(defn get-full-patient-name [patient]
  (str (:first_name patient) " " (:middle_name patient) " " (:last_name patient)))

(defn show-all-patients [request]
  (let [search-name (-> request :params :search-name)
        all-patients (patients/all-patients @db :search-name search-name)]
    (response/response
      {:patients (map (fn [patient] {:id (:id patient)
       :cmi_number (:cmi_number patient)
       :full_name (get-full-patient-name patient)}) all-patients)})))

(defn show-patient [request]
  (let [patient-id (-> request :path-params :patient-id)
        patient (patients/patient-by-id @db {:id patient-id})]
    (if (nil? patient)
      (response/not-found
        {:message "Patient not found."})
      (response/response
        {:id (:id patient)
         :first_name (:first_name patient)
         :middle_name (:middle_name patient)
         :last_name (:last_name patient)
         :sex (:sex patient)
         :birth_date (:birth_date patient)
         :address (:address patient)
         :cmi_number (:cmi_number patient)}))))

(defn delete-patient [request]
  (let [patient-id (-> request :path-params :patient-id)
        affected-patients (patients/delete-patient-by-id @db {:id patient-id})]
    (if (zero? affected-patients)
      (response/not-found {:message "Patient not found."})
      (response/response {:message "Patient deleted."}))))

(defn update-patient [request]
  (let [patient-id (-> request :path-params :patient-id)
        form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data schemes/+patient-scheme+)
        patient-data-with-id (assoc patient-data :id patient-id)]
    (if (empty? form-errors)
      (do
        (patients/update-patient-by-id @db patient-data-with-id)
        (response/response {:id patient-id}))
      (response/bad-request {:form-errors form-errors}))))

(defn create-patient [request]
  (let [form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? form-errors)
      (let [inserted-data (patients/insert-patient @db patient-data)
            patient-id (:id inserted-data)
            patient-url (str "/patients/" patient-id)]
        (response/created patient-url {:id patient-id}))
      (response/bad-request {:form-errors form-errors}))))
