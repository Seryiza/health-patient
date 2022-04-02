(ns health-patient.app
  (:require [mount.core :refer [defstate]]
            [reitit.ring :as reit]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [health-patient.html :as html]
            [health-patient.views.index :as index-views]
            [health-patient.patients.handlers :as patients]))

(defn show-index-page [_]
  (html/response (index-views/index-page)))

(def app-routes
  [["/" {:get show-index-page}]
   ["/patients" ["" {:get patients/show-all-patients
                     :post patients/create-patient}]
                ["/:id" ["" {:get patients/show-patient
                             :put patients/update-patient
                             :delete patients/delete-patient}]
                        ["/edit" {:get patients/show-edit-form}]]]
   ["/create-patient" {:get patients/show-create-form}]])

(defstate app
  :start (reit/ring-handler
           (reit/router app-routes)
           (reit/routes (reit/create-resource-handler {:path "/assets"})
                        (reit/redirect-trailing-slash-handler)
                        (reit/create-default-handler))
           {:middleware [wrap-params
                         wrap-json-params
                         wrap-keyword-params
                         wrap-session
                         wrap-flash
                         wrap-json-response]}))
