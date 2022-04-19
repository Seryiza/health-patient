(ns health-patient.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [health-patient.db :as db]
            [health-patient.router :as router]))

(rf/reg-fx
 :set-url
 (fn [{:keys [url]}]
   (router/set-token! url)))

(rf/reg-event-db
  :initialize-db
  (fn [_ _] db/default-db))

(rf/reg-event-fx
  :set-active-page
  (fn [{:keys [db]} [_ {:keys [page path-params]}]]
    {:db (assoc db :active-page page)
     :fx (case page
           :patients-list [[:dispatch [:get-patients]]]
           (:patient-view :patient-edit) [[:dispatch [:get-patient (:id path-params)]]]
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
                  :on-success [:delete-patient-success]
                  :on-failure [:delete-patient-failure]}}))

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

(rf/reg-event-fx
  :get-patient
  (fn [{:keys [db]} [_ patient-id]]
    {:db (assoc db :flash [])
     :http-xhrio {:method :get
                  :uri (str "/api/patients/" patient-id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:get-patient-success]
                  :on-failure [:get-patient-failure]}}))

(rf/reg-event-db
  :get-patient-success
  (fn [db [_ patient-data]]
    (assoc db :patient patient-data)))

(rf/reg-event-db
  :get-patient-failure
  (fn [db _]
    (assoc db :flash ["Can't load this patient from server."])))

(rf/reg-event-fx
  :edit-patient
  (fn [{:keys [db]} [_ {:keys [id] :as patient-data}]]
    {:db (assoc db :flash [])
     :http-xhrio {:method :put
                  :uri (str "/api/patients/" id)
                  :params patient-data
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:edit-patient-success id]
                  :on-failure [:edit-patient-failure id]}}))

(rf/reg-event-fx
  :edit-patient-success
  (fn [_ [_ id]]
    {:set-url {:url (str "/patients/" id)}}))

(rf/reg-event-db
  :edit-patient-failure
  (fn [db _]
    (assoc db :flash ["Can't edit patient on server."])))

(rf/reg-event-db
  :edit-patient-field
  (fn [db [_ field value]]
    (assoc-in db [:patient field] value)))
