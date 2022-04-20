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
        (assoc-in [:loading :patients] false)
        (assoc :flash ["Can't load patients list from server."]))))

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

(rf/reg-event-fx
  :get-patient
  (fn [{:keys [db]} [_ patient-id]]
    {:db (-> db
             (assoc :flash [])
             (assoc-in [:loading :patient patient-id] true))
     :http-xhrio {:method :get
                  :uri (str "/api/patients/" patient-id)
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:get-patient-success patient-id]
                  :on-failure [:get-patient-failure patient-id]}}))

(rf/reg-event-db
  :get-patient-success
  (fn [db [_ patient-id patient-data]]
    (-> db
        (assoc-in [:loading :patient patient-id] false)
        (assoc :patient patient-data))))

(rf/reg-event-db
  :get-patient-failure
  (fn [db [_ patient-id]]
    (-> db
        (assoc-in [:loading :patient patient-id] false)
        (assoc :flash ["Can't load this patient from server."]))))

(rf/reg-event-fx
  :upsert-patient
  (fn [{:keys [db]} [_ {:keys [id] :as patient-data}]]
    (let [new? (nil? id)]
      {:db (assoc db :flash [])
       :http-xhrio {:method (if new? :post :put)
                    :uri    (if new?
                              "/api/patients"
                              (str "/api/patients/" id))
                    :params patient-data
                    :format (ajax/json-request-format)
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success [:upsert-patient-success]
                    :on-failure [:upsert-patient-failure new?]}})))

(rf/reg-event-fx
  :upsert-patient-success
  (fn [_ [_ {:keys [id]}]]
    {:set-url {:url (str "/patients/" id)}}))

(rf/reg-event-db
  :upsert-patient-failure
  (fn [db [_ new? {:keys [response]}]]
    (-> db
        (assoc :form-errors (:form-errors response))
        (assoc :flash (if new? ["Can't create patient on server."] ["Can't edit patient on server."])))))

(rf/reg-event-db
  :edit-patient-field
  (fn [db [_ field value]]
    (assoc-in db [:patient field] value)))
