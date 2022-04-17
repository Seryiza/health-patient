(ns health-patient.router
  (:require [route-map.core :as rm]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [health-patient.views.index :as index]
            [health-patient.views.patients :as patients]))

(def routes
  {:. :home
   "patients" :patients-list})

(defn get-page [page-name]
  (case page-name
    :home [index/index-page]
    :patients-list [patients/list-page]
    [index/index-page]))

(def history
  (pushy/pushy
    #(rf/dispatch [:set-active-page {:page (:match %)}])
    #(rm/match % routes)))

(defn start! []
  (pushy/start! history))
