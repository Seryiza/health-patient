(ns health-patient.views.patients
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn patient-full-name [patient]
  (str
    (:first_name patient)
    " "
    (:middle_name patient)
    " "
    (:last_name patient)))

(defn format-birth-date [birth-date]
  (let [date (js/Date. birth-date)]
    (.toLocaleDateString date [] #js {:year "numeric" :month "long" :day "numeric"})))

(defn patient-list-entry [patient]
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

(defn patient-field [label value]
  [:p
   [:b (str label ": ")]
   value])

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
                (map patient-list-entry patients)]]))])

(defn view-page []
  [:div
   (let [loading @(rf/subscribe [:loading])
         patient @(rf/subscribe [:patient])]
     (cond
       (:patient loading) [:p "Loading..."]
       (empty? patient) [:p "This patient isn't found."]
       :else [:div
              [:hgroup
               [:h2 (str "Patient #" (:id patient))]
               [:h3 (patient-full-name patient)]]
              [:section
               [:a {:href (str "/patients/" (:id patient) "/edit") :role "button"} "Edit current patient"]]
              (patient-field "First name" (:first_name patient))
              (patient-field "Middle name" (:middle_name patient))
              (patient-field "Last name" (:last_name patient))
              (patient-field "CMI number" (:cmi_number patient))
              (patient-field "Sex" (:sex patient))
              (patient-field "Birth date" (-> patient :birth_date format-birth-date))
              (patient-field "Address" (:address patient))]))])
