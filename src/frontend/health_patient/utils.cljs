(ns health-patient.utils
  (:require [dommy.core :as dommy]))

(defn redirect [url]
  (.assign js/location url))

(defn common-error-handler [{:keys [status status-text]}]
  (.log js/console (str "Error happened: " status " " status-text))
  (js/alert "Ooops, error happend. Try again later or ask support team."))

(defn form-listen! [form handler]
  (dommy/listen! form :submit (fn [event] (.preventDefault event)
                                          (handler event))))

(defn selector-by-name [node-name]
  (str "[name='" (name node-name) "']"))

(defn get-input-value [form input-name]
  (when-let [input (dommy/sel1 form (selector-by-name input-name))]
    (dommy/value input)))

(defn get-form-values [form input-names]
  (->> input-names
       (map #(hash-map % (get-input-value form %)))
       (apply merge)))

(defn get-field-input [form input-name]
  (dommy/sel1 form (selector-by-name input-name)))

(defn get-field-error [form input-name]
  (dommy/sel1 form (str "[data-error-for='" (name input-name) "']")))

(defn clear-form-errors [form]
  (doseq [field-input (dommy/sel form (str "[aria-invalid='true']"))]
    (dommy/set-attr! field-input :aria-invalid ""))
  (doseq [field-error (dommy/sel form "[data-error-for]")]
    (dommy/set-text! field-error "")))

(defn show-form-errors [form errors]
  (clear-form-errors form)
  (doseq [[input-name error-msg] errors]
    (when-let [field-input (get-field-input form input-name)]
      (when-let [field-error (get-field-error form input-name)]
        (dommy/set-attr! field-input :aria-invalid "true")
        (dommy/set-text! field-error error-msg)))))
