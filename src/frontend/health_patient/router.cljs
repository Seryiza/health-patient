(ns health-patient.router
  (:require [route-map.core :as rm]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [health-patient.views.index :as index]
            [health-patient.views.patients :as patients]))

(def routes
  {:. :home
   "patients" {:. :patients-list
               [:id] {:. :patient-view
                      "edit" :patient-edit}}})

(defn get-page [page-name]
  (case page-name
    :home [index/index-page]
    :patients-list [patients/list-page]
    :patient-view [patients/view-page]
    :patient-edit [patients/edit-page]
    [index/index-page]))

(def history
  (pushy/pushy
    #(rf/dispatch [:set-active-page {:page (:match %)
                                     :path-params (:params %)}])
    #(rm/match % routes)))

(defn set-token! [url]
  (pushy/set-token! history url))

(defn start! []
  (pushy/start! history))
