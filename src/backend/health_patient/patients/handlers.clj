(ns health-patient.patients.handlers
  (:require [ring.util.response :as response]
            [struct.core :as st]
            [health-patient.html :as html]
            [health-patient.db :refer [db]]
            [health-patient.schemes :as schemes]
            [health-patient.patients.views.list :as list-views]
            [health-patient.patients.views.show :as show-views]
            [health-patient.patients.views.form :as form-views]
            [health-patient.patients.db :as patients]))

(defn show-all-patients [request]
  (let [all-patients (patients/all-patients db)]
    (html/render (list-views/list-page all-patients))))

(defn show-patient [request]
  (let [patient-id (-> request :path-params :id)
        patient (patients/patient-by-id db {:id patient-id})]
    (if (nil? patient)
      (response/not-found "Patient not found.")
      (html/render (show-views/show-page patient)))))

(defn show-create-form [_]
  (html/render (form-views/form-page {:patient {}
                                      :patient-exist? false})))

(defn show-edit-form [request]
  (let [patient-id (-> request :path-params :id)
        patient (patients/patient-by-id db {:id patient-id})]
    (if (nil? patient)
      (response/not-found "Patient not found.")
      (html/render (form-views/form-page {:patient patient
                                          :patient-exist? true})))))

(defn delete-patient [request]
  (let [patient-id (-> request :path-params :id)
        affected-patients (patients/delete-patient-by-id db {:id patient-id})]
    (if (zero? affected-patients)
      (response/not-found "Patient not found.")
      (response/response "Patient deleted."))))

(defn update-patient [request]
  (let [patient-id (-> request :path-params :id)
        form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data schemes/+patient-scheme+)
        patient-data-with-id (assoc patient-data :id patient-id)]
    (if (empty? form-errors)
      (do
        (patients/update-patient-by-id db patient-data-with-id)
        (response/response nil))
      (response/bad-request {:form-errors form-errors}))))

(defn create-patient [request]
  (let [form-data (-> request :params)
        [form-errors patient-data] (st/validate form-data schemes/+patient-scheme+)]
    (if (empty? form-errors)
      (let [inserted-data (patients/insert-patient db patient-data)
            patiend-id (:id inserted-data)
            patient-url (str "/patients/" patiend-id)]
        (response/created patient-url {:id patiend-id}))
      (response/bad-request {:form-errors form-errors}))))
