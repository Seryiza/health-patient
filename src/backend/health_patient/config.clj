(ns health-patient.config
  (:require [mount.core :refer [defstate]]))

(defstate config
  :start {:http-port (Integer/parseInt (System/getenv "HTTP_PORT"))
          :database-jdbc-url (System/getenv "DATABASE_JDBC_URL")})
