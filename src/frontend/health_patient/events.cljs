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
    {:db (assoc-in db [:loading :patients] true)
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
        (assoc :flash [])
        (assoc :patients patients))))

(rf/reg-event-db
  :get-patients-failure
  (fn [db _]
    (-> db
        (assoc :flash ["Can't load patients list from server."])
        (assoc-in [:loading :patients] false))))
