(ns health-patient.app
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [health-patient.router :as router]
            [health-patient.patients.events]
            [health-patient.subs]
            [health-patient.views.common :as common]))

(defn active-page []
  (let [active-page @(rf/subscribe [:active-page])]
    [common/page (router/get-page active-page)]))

(defn ^:dev/after-load render []
  (rdom/render
    [active-page]
    (js/document.getElementById "app")))

(defn init []
  (router/start!)
  (rf/dispatch-sync [:initialize-db])
  (render))
