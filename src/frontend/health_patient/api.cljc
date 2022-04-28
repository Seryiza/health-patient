(ns health-patient.api
  (:require [clojure.string :as str]))

(def url "http://localhost:8080/api")

(defn endpoint [& path-parts]
  (str/join "/" (cons url path-parts)))

