(ns health-patient.test-utils
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [net.cgrand.enlive-html :as enlive]
            [clojure.test :as test]
            [clojure.string :as str]
            [health-patient.app :as app]
            [health-patient.config :as config]
            [health-patient.db :as db])
  (:import [java.io StringReader]))

(def test-db (atom (db/make-db-conn (config/get-val :test-database-jdbc-url))))

(defn with-test-db [t]
  (let [db-before-tests @db/db]
    (reset! db/db @test-db)
    (t)
    (reset! db/db db-before-tests)))

(defn with-transaction [t]
  (jdbc/with-transaction [tx @test-db {:rollback-only true}]
    (let [db-before-test @db/db]
      (reset! db/db tx)
      (t)
      (reset! db/db db-before-test))))

(defn json-request [method uri & {:keys [json query]}]
  {:request-method method
   :uri uri
   :content-type "application/json"
   :json-params json
   :query-params query
   :params (merge query json)})

(defn make-test-record [generator needed-data]
  (merge (generator) needed-data))

(defn insert-test-records [table generator needed-records-data]
  (doseq [record (map #(make-test-record generator %) needed-records-data)]
    (sql/insert! @db/db table record)))

(defn parse-html [response]
  (enlive/html-resource (StringReader. (:body response))))

(defn html-has-text? [html selector checking-text]
  (let [node-texts (map enlive/text (enlive/select html selector))]
    (test/is (some #(str/includes? % checking-text) node-texts)
             (str "Text " checking-text " not found"))))

(defn http-status? [response status]
  (test/is (= status (:status response))))
