(ns health-patient.test-utils
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [mount.core :as mount]
            [clojure.test :as test]
            [health-patient.app :refer [app]]
            [health-patient.config :refer [config]]
            [health-patient.db :refer [db]]))

(defn use-mount-app-fixture []
  (test/use-fixtures
    :once
    (fn [t]
      (mount/start #'health-patient.config/config
                   #'health-patient.db/db
                   #'health-patient.app/app)
      (t)
      (mount/stop))))

(defn use-transactable-tests-fixture []
  (test/use-fixtures
    :each
    (fn [t]
      (jdbc/with-transaction [tx db {:rollback-only true}]
        (with-redefs [health-patient.db/db tx]
          (t))))))

(defn make-test-record [generator needed-data]
  (merge (generator) needed-data))

(defn insert-test-records [table generator needed-records-data]
  (doseq [record (map #(make-test-record generator %) needed-records-data)]
    (sql/insert! db table record)))
