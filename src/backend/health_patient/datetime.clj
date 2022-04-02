(ns health-patient.datetime
  (:import [java.time.format DateTimeFormatter FormatStyle]))

(defn format-short-date [date]
  (let [formatter (DateTimeFormatter/ofLocalizedDate FormatStyle/MEDIUM)
        local-date (.toLocalDate date)]
    (.format local-date formatter)))
