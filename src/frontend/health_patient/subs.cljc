(ns health-patient.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :active-page (fn [db _] (:active-page db)))
(rf/reg-sub :patients (fn [db _] (:patients db)))
(rf/reg-sub :patient (fn [db _] (:patient db)))
(rf/reg-sub :loading (fn [db _] (:loading db)))
(rf/reg-sub :flash (fn [db _] (:flash db)))
(rf/reg-sub :form-errors (fn [db _] (:form-errors db)))
(rf/reg-sub :search-query (fn [db _] (:search-query db)))
