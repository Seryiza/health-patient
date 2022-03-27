(ns health-patient.patients.db
  (:require [hugsql.core :as hugsql]
            [clojure.java.io :as io]))

(hugsql/def-db-fns (io/resource "sql/patients.sql"))

