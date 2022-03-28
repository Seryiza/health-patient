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

(defn get-input-value [form input-name]
  (let [input-sel (str "[name=" (name input-name) "]")]
    (when-let [input (dommy/sel1 form input-sel)]
      (dommy/value input))))

(defn get-form-values [form input-names]
  (->> input-names
       (map #(hash-map % (get-input-value form %)))
       (apply merge)))
