(ns health-patient.patients.list-page
  (:require [dommy.core :as dommy]
            [ajax.core :as ajax]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "Error happened: " status " " status-text))
  (js/alert "Ooops, error happend. Try again later or ask support team."))

(defn delete-patient! [patient-id]
  (ajax/DELETE (str "/patients/" patient-id)
               {:handler #(.log js/console %)
                :error-handler error-handler}))

(defn init []
  (doseq [delete-patient-button (dommy/sel "[data-delete-patient-button]")]
    (let [patient-id (dommy/attr delete-patient-button :data-patient-id)]
      (dommy/listen! delete-patient-button :click #(delete-patient! patient-id)))))
