(ns health-patient.patients.handlers
  (:require [health-patient.html :as html]
            [health-patient.db :refer [db]]
            [health-patient.patients.db :as patients]))

(defn show-all-patients [request]
  (let [all-patients (patients/all-patients db)]
    (html/render request "patients/list.html" {:patients all-patients})))
