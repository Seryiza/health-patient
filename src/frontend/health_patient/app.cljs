(ns health-patient.app
  (:require [health-patient.patients.form-page :as form-page]
            [health-patient.patients.list-page :as list-page]))

(defn init-all []
  (form-page/init)
  (list-page/init))

(init-all)
