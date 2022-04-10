(ns health-patient.patients.db
  (:require [health-patient.db :as db]))

(defn all-patients [db]
  (db/honey-execute!
    db
    {:select :*
     :from :patients
     :where [:= :is_deleted false]
     :order-by [:id]}))

(defn patient-by-id [db {:keys [id]}]
  (db/honey-execute-one!
    db
    {:select :*
     :from :patients
     :where [:and
             [:= :id id]
             [:= :is_deleted false]]}))

(defn delete-patient-by-id [db {:keys [id]}]
  (db/honey-update!
    db
    {:update :patients
     :set {:is_deleted true}
     :where [:and
             [:= :id id]
             [:= :is_deleted false]]}))

(defn update-patient-by-id [db {:keys [id] :as updated-patient}]
  (db/honey-update!
    db
    {:update :patients
     :set updated-patient
     :where [:and
             [:= :id id]
             [:= :is_deleted false]]}))

(defn insert-patient [db new-patient]
  (db/honey-execute-one!
    db
    {:insert-into :patients
                  :values [new-patient]}
    {:return-keys true}))
