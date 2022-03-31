(ns health-patient.patients.api
  (:require [ajax.core :as ajax]
            [health-patient.utils :as utils]))

(defn save-patient! [patient-id patient-data]
  (ajax/PUT
    (str "/patients/" patient-id)
    {:params patient-data
     :handler #(utils/redirect (str "/patients/" patient-id))
     :error-handler utils/common-error-handler
     :format :json
     :keywords? true}))

(defn create-patient! [patient-data]
  (ajax/POST
    "/patients"
    {:params patient-data
     :handler #(utils/redirect (str "/patients/" (:id %)))
     :error-handler utils/common-error-handler
     :format :json
     :response-format :json
     :keywords? true}))
