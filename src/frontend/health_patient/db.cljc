(ns health-patient.db
  (:require [re-frame.core :as rf]))

(def default-patient
  {:sex "not-known"})

(def default-db
  {:active-page :home
   :loading {}
   :patients []
   :patient default-patient
   :flash []})

(rf/reg-event-db
  :initialize-db
  (fn [_ _] default-db))
