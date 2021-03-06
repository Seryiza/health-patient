(ns health-patient.views.patients
  (:require [re-frame.core :as rf]
            [clojure.string :as str]
            [health-patient.components.table :as table]
            [health-patient.components.common :as common]))

(def shown-sex-labels [["Not known" "not-known"]
                       ["Male" "male"]
                       ["Female" "female"]
                       ["Not applicable" "not-applicable"]])

(defn patient-full-name [patient]
  (str (:first_name patient) " " (:middle_name patient) " " (:last_name patient)))

(defn format-birth-date [birth-date]
  (let [date (js/Date. birth-date)]
    (.toLocaleDateString date [] #js {:year "numeric" :month "long" :day "numeric"})))

(defn num->2digits [number]
  (str (when (< number 10) "0") number))

(defn format-html-input-date [input-date]
  (when (not (nil? input-date))
    (let [date (js/Date. input-date)
        year (.getFullYear date)
        month (inc (.getMonth date))
        day (.getDate date)]
    (str year "-" (num->2digits month) "-" (num->2digits day)))))

(defn date->iso [input-date]
  (try
    (-> (js/Date. input-date)
        .toISOString)
    (catch js/Error _
      nil)))

(defn dispatch-field-edited [dispatch-id field & [transform-fn]]
  (fn [event]
    (let [value (-> event .-target .-value)
          value (if transform-fn (transform-fn value) value)]
      (rf/dispatch [dispatch-id field value]))))

(defn patient-list-entry [patient]
  (let [patient-id (:id patient)]
    ^{:key patient-id}
    [table/row
     [table/column (:id patient)]
     [table/column (:cmi_number patient)]
     [table/column (:full_name patient)]
     [table/column
      [common/link-button (str "/patients/" (:id patient)) "Details"]
      [common/link-button (str "/patients/" (:id patient) "/edit") "Edit"]
      [common/button {:on-click #(rf/dispatch [:delete-patient patient-id])} "Delete"]]]))

(defn patient-field [label value]
  [:p
   [:b (str label ": ")]
   value])

(defn patient-edit-field [label input & [error-text]]
  [:div
   [:label
    label
    input
    (when (not (str/blank? error-text))
      [:small [:b error-text]])]])

(defn list-page []
  (let [loading @(rf/subscribe [:loading])
        patients @(rf/subscribe [:patients])
        search-query @(rf/subscribe [:search-query])]
    [:div
     [common/h2 "Patients"]
     [common/label
      "Search patient by name"
      [common/input {:type "text"
                     :on-change #(rf/dispatch [:set-search-query (-> % .-target .-value)])}]]
     [common/button {:type "button"
                     :on-click #(rf/dispatch [:get-patients {:search-query search-query}])}
      "Search"]
     [common/link-button
      "/patients/create"
      "Create new patient"]
     (cond
       (:patients loading) [:div "Loading..."]
       (empty? patients) [:div "No patients yet."]
       :else [table/table
              [table/header
               [table/header-column "ID"]
               [table/header-column "CMI number"]
               [table/header-column "Full name"]
               [table/header-column "Actions"]]
              [table/body
                (for [patient patients]
                  (patient-list-entry patient))]])]))

(defn view-page []
  [:div
   (let [loading @(rf/subscribe [:loading])
         patient @(rf/subscribe [:patient])
         patient-id (:id patient)]
     (cond
       (:patient loading) [:p "Loading..."]
       (empty? patient) [:p "This patient isn't found."]
       :else [:div
              [common/h2 (str "Patient #" patient-id ": " (patient-full-name patient))]
              [:section
               [common/link-button (str "/patients/" (:id patient) "/edit") "Edit current patient"]]
              [patient-field "First name" (:first_name patient)]
              [patient-field "Middle name" (:middle_name patient)]
              [patient-field "Last name" (:last_name patient)]
              [patient-field "CMI number" (:cmi_number patient)]
              [patient-field "Sex" (:sex patient)]
              [patient-field "Birth date" (-> patient :birth_date format-birth-date)]
              [patient-field "Address" (:address patient)]]))])

(defn form-page []
  (let [patient @(rf/subscribe [:patient])
        loading @(rf/subscribe [:loading])
        form-errors @(rf/subscribe [:form-errors])
        is-new-patient (nil? (:id patient))
        edit-field (partial dispatch-field-edited :edit-patient-field)
        upsert-patient (fn [event]
                         (.preventDefault event)
                         (rf/dispatch [:upsert-patient patient]))]
    (cond
      (:patient loading) [:div "Loading..."]
      :else [:form {:on-submit upsert-patient}
             [common/h2
              (if is-new-patient
                "New patient"
                (str "Patient #" (:id patient)))]
             [:div.form-errors]
             [:div.grid
              [patient-edit-field
                "First name"
                [common/input {:type "text"
                         :default-value (:first_name patient)
                         :on-change (edit-field :first_name)}]
                (:first_name form-errors)]
              [patient-edit-field
                "Middle name"
                [common/input {:type "text"
                         :default-value (:middle_name patient)
                         :on-change (edit-field :middle_name)}]
                (:middle_name form-errors)]
              [patient-edit-field
                "Last name"
                [common/input {:type "text"
                         :default-value (:last_name patient)
                         :on-change (edit-field :last_name)}]
                (:last_name form-errors)]]
             [:div.grid
              [patient-edit-field
               "Sex"
               [common/select {:default-value (:sex patient)
                               :on-change (edit-field :sex)}
                (for [[label value] shown-sex-labels]
                  ^{:key value} [common/option {:value value} label])]
               (:sex form-errors)]
              [patient-edit-field
                "Birth date"
                [common/input {:type "date"
                         :default-value (-> patient :birth_date format-html-input-date)
                         :on-change (edit-field :birth_date date->iso)}]
                (:birth_date form-errors)]]
             [:div.grid
              [patient-edit-field
                "Address"
                [common/input {:type "text"
                         :default-value (:address patient)
                         :on-change (edit-field :address)}]
                (:address form-errors)]]
            [:div.grid
              [patient-edit-field
                "CMI number"
                [common/input {:type "text"
                         :default-value (:cmi_number patient)
                         :on-change (edit-field :cmi_number)}]
                (:cmi_number form-errors)]]

             [common/button {:type "submit"} (if is-new-patient "Create" "Save")]])))
