(ns health-patient.views.patients
  (:require [re-frame.core :as rf]))

(defn patient-entry [patient]
  (let [patient-id (:id patient)
        loading @(rf/subscribe [:loading])
        patient-loading (-> loading :patient (get patient-id) boolean)]
    ^{:key (:cmi_number patient)}
    [:tr {:aria-busy patient-loading}
     [:td (:id patient)]
     [:td (:cmi_number patient)]
     [:td (:full_name patient)]
     [:td
      [:a {:href (str "/patients/" (:id patient)) :role "button"} "Details"]
      [:a {:href (str "/patients/" (:id patient) "/edit") :role "button"} "Edit"]
      [:button.secondary {:on-click #(rf/dispatch [:delete-patient patient-id])} "Delete"]]]))

(defn list-page []
  [:div
   [:h2 "Patients"]
   [:a {:href "/create-patient" :role "button"} "Create new patient"]
   (let [loading @(rf/subscribe [:loading])
         patients @(rf/subscribe [:patients])]
     (cond
       (:patients loading) [:div "Loading..."]
       (empty? patients) [:div "No patients yet."]
       :else [:table {:role "grid"}
              [:thead
               [:tr
                [:th "ID"]
                [:th "CMI number"]
                [:th "Full name"]
                [:th "Actions"]]]
              [:tbody
                (map patient-entry patients)]]))])
