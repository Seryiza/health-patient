(ns health-patient.router
  (:require [route-map.core :as rm]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [health-patient.views.index :as index]
            [health-patient.views.patients :as patients]
            [health-patient.views.common :as common]))

(def routes
  {:. :home
   "patients" {:. :patients-list
               "create" :patient-create
               [:id] {:. :patient-view
                      "edit" :patient-edit}}})

(defn get-page [page-name]
  (case page-name
    :home [index/index-page]
    :patients-list [patients/list-page]
    :patient-view [patients/view-page]
    (:patient-create :patient-edit) [patients/form-page]
    [common/not-found-page]))

(def history
  (pushy/pushy
    #(rf/dispatch [:set-active-page {:page (:match %)
                                     :path-params (:params %)}])
    #(or (rm/match % routes) :not-found)))

(defn set-token! [url]
  (pushy/set-token! history url))

(rf/reg-fx
 :set-url
 (fn [{:keys [url]}]
   (set-token! url)))

(rf/reg-event-fx
  :set-active-page
  (fn [{:keys [db]} [_ {:keys [page path-params]}]]
    {:db (assoc db :active-page page)
     :fx (case page
           :patients-list [[:dispatch [:get-patients]]]
           :patient-create [[:dispatch [:clear-patient-form]]]
           (:patient-view :patient-edit) [[:dispatch [:get-patient (:id path-params)]]]
           [])}))

(defn start! []
  (pushy/start! history))
