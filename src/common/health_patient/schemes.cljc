(ns health-patient.schemes
  (:require [struct.core :as st]))

(def +patient-scheme+
  {:first_name [st/required st/string]
   :last_name [st/required st/string]
   :middle_name [st/required st/string]
   :sex [st/required st/string]
   :birth_date [st/required st/string]
   :address [st/required st/string]
   :cmi_number [st/required st/number-str]})
