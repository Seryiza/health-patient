(ns health-patient.app
  (:require [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [route-map.core :as rm]
            [health-patient.html :as html]
            [health-patient.views :as views]
            [health-patient.patients.handlers :as patients]))

(defn show-reactive-page [_]
  (html/response (views/reactive-page)))

(def routes
  {:GET #'show-reactive-page
   "api" {"patients" {:GET #'patients/show-all-patients
                      :POST #'patients/create-patient
                      [:patient-id] {:GET #'patients/show-patient
                                     :PUT #'patients/update-patient
                                     :DELETE #'patients/delete-patient}}}})

(defn handler [{:keys [uri request-method] :as request}]
  (if-let [found-route (rm/match [request-method uri] routes)]
    ((:match found-route) (update-in request [:path-params] merge (:params found-route)))
    (show-reactive-page request)))

(def app
  (atom
    (-> handler
        wrap-json-response
        wrap-flash
        wrap-session
        wrap-keyword-params
        wrap-json-params
        wrap-params
        (wrap-resource "public"))))
