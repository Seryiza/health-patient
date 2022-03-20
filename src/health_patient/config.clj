(ns health-patient.config
  (:require [mount.core :refer [defstate]]))

(defstate config
  :start {:database-url (System/getenv "DATABASE_URL")})
