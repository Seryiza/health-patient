(ns health-patient.patients.views.common
  (:require [hiccup.core :refer [h]]))

(defn patient-full-name [patient]
  (str (:first_name patient) " " (:middle_name patient) " " (:last_name patient)))
