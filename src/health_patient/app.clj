(ns health-patient.app
  (:require [mount.core :refer [defstate]]
            [reitit.ring :as reit]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.flash :refer [wrap-flash]]
            [health-patient.patients.handlers :as patients]))

(def app-routes
  [["/patients" ["" {:get patients/show-all-patients}]
                ["/:id" {:get patients/show-patient}]]])

(defstate app
  :start (reit/ring-handler
           (reit/router app-routes)
           (reit/routes (reit/create-resource-handler {:path "/assets"})
                        (reit/redirect-trailing-slash-handler)
                        (reit/create-default-handler))
           {:middleware [wrap-params
                         wrap-keyword-params
                         wrap-session
                         wrap-flash]}))
