(ns health-patient.patients.handlers
  (:require [ring.util.response :as response]
            [health-patient.html :as html]
            [health-patient.db :refer [db]]
            [health-patient.patients.db :as patients]))

(defn show-all-patients [request]
  (let [all-patients (patients/all-patients db)]
    (html/render request "patients/list.html" {:patients all-patients})))

(defn show-patient [request]
  (let [patient-id (-> request :path-params :id)
        patient (patients/patient-by-id db {:id patient-id})]
    (if (nil? patient)
      (response/not-found "Patient not found.")
      (html/render request "patients/show.html" {:patient patient}))))

(defn delete-patient [request]
  (let [patient-id (-> request :path-params :id)
        affected-patients (patients/delete-patient-by-id db {:id patient-id})]
    (if (zero? affected-patients)
      (response/not-found "Patient not found.")
      (response/response "Patient deleted."))))
