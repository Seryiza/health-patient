(ns health-patient.patients.list-page
  (:require [dommy.core :as dommy]
            [ajax.core :as ajax]
            [health-patient.utils :as utils]))

(defn delete-patient-row! [patient-id]
  (let [row-sel (str "tr[data-patient-id='" patient-id "']")]
    (when-let [row (dommy/sel1 row-sel)]
      (dommy/remove! row))))

(defn delete-patient! [patient-id]
  (ajax/DELETE (str "/patients/" patient-id)
               {:handler #(delete-patient-row! patient-id)
                :error-handler utils/common-error-handler}))

(defn init []
  (doseq [delete-patient-button (dommy/sel "[data-delete-patient-button]")]
    (let [patient-id (dommy/attr delete-patient-button :data-patient-id)]
      (dommy/listen! delete-patient-button :click #(delete-patient! patient-id)))))
