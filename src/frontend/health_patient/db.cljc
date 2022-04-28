(ns health-patient.db
  (:require [re-frame.core :as rf]))

(def default-db {:active-page :home
                 :loading {}
                 :patients []
                 :patient {:sex "not-known"}
                 :flash []})

(rf/reg-event-db
  :initialize-db
  (fn [_ _] default-db))
