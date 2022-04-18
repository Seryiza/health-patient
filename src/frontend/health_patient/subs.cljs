(ns health-patient.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :active-page
  (fn [db _]
    (:active-page db)))

(rf/reg-sub
  :patients
  (fn [db _]
    (:patients db)))

(rf/reg-sub
  :loading
  (fn [db _]
    (:loading db)))
