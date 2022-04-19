(ns health-patient.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [health-patient.db :as db]))

(rf/reg-event-db
  :initialize-db
  (fn [_ _] db/default-db))

(rf/reg-event-fx
  :set-active-page
  (fn [{:keys [db]} [_ {:keys [page]}]]
    {:db (assoc db :active-page page)
     :fx (case page
           :patients-list [[:dispatch [:get-patients]]]
           [])}))

(rf/reg-event-fx
  :get-patients
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc :flash [])
             (assoc-in [:loading :patients] true))
     :http-xhrio {:method :get
                  :uri "/api/patients"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:get-patients-success]
                  :on-failure [:get-patients-failure]}}))

(rf/reg-event-db
  :get-patients-success
  (fn [db [_ {:keys [patients]}]]
    (-> db
        (assoc-in [:loading :patients] false)
        (assoc :patients patients))))

(rf/reg-event-db
  :get-patients-failure
  (fn [db _]
    (-> db
        (assoc :flash ["Can't load patients list from server."])
        (assoc-in [:loading :patients] false))))

(rf/reg-event-fx
  :delete-patient
  (fn [{:keys [db]} [_ patient-id]]
    {:db (-> db
             (assoc :flash [])
             (assoc-in [:loading :patient patient-id] true))
     :http-xhrio {:method :delete
                  :uri (str "/api/patients/" patient-id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:delete-patient-success patient-id]
                  :on-failure [:delete-patient-failure patient-id]}}))

(rf/reg-event-db
  :delete-patient-success
  (fn [db [_ patient-id]]
    (-> db
        (assoc-in [:loading :patient patient-id] false)
        (update :patients (fn [patients] (filter #(not= (:id %) patient-id) patients))))))

(rf/reg-event-db
  :delete-patient-failure
  (fn [db [_ patient-id]]
    (-> db
        (assoc-in [:loading :patient patient-id] false)
        (assoc :flash ["Can't delete patient from server."]))))
