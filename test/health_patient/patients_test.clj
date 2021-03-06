(ns health-patient.patients-test
  (:require [re-rand :refer [re-rand]]
            [clojure.test :refer [deftest testing use-fixtures]]
            [clojure.string :as str]
            [matcho.core :as m]
            [health-patient.app :as app]
            [health-patient.test-utils :as test-utils]))

(use-fixtures :once test-utils/with-test-db)
(use-fixtures :each test-utils/with-transaction)

(defn generate-patient []
  {:first_name (re-rand #"[A-Z][a-z]{1,20}")
   :last_name (re-rand #"[A-Z][a-z]{1,20}")
   :middle_name (re-rand #"[A-Z][a-z]{1,20}")
   :sex (rand-nth ["male" "female" "not-known" "not-applicable"])
   :birth_date (re-rand #"19[0-9]{2}-0[1-9]-0[1-9]")
   :address (re-rand #"[A-Za-z0-9\ ]{1,40}")
   :cmi_number (re-rand #"[1-9]{16}")})

(deftest list-patients-test
  (testing "List all patients"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:first_name "Sergey"} {:first_name "Uniqueman"}])
    (let [response (app/handler (test-utils/json-request :get "/api/patients"))]
      (m/assert
        {:status 200
         :body {:patients [{:full_name #(str/includes? % "Sergey")}
                           {:full_name #(str/includes? % "Uniqueman")}]}}
        response))))


(deftest show-patient-test
  (testing "Show existing patient"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1 :first_name "Sergey" :last_name "Zaborovsky"}])
    (let [response (app/handler (test-utils/json-request :get "/api/patients/1"))]
      (m/assert
        {:status 200
         :body {:first_name "Sergey"
                :last_name "Zaborovsky"}}
        response)))

  (testing "Don't show non-existing patient"
    (let [response (app/handler (test-utils/json-request :get "/api/patients/2"))]
      (m/assert {:status 404} response))))

(deftest delete-patient-test
  (testing "Delete existing patient and try delete again"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1 :first_name "Sergey"}])
    (let [response (app/handler (test-utils/json-request :delete "/api/patients/1"))]
      (m/assert {:status 200} response))
    (let [response (app/handler (test-utils/json-request :delete "/api/patients/1"))]
      (m/assert {:status 404} response)))

  (testing "Delete non-existing patient"
    (let [response (app/handler (test-utils/json-request :delete "/api/patients/2"))]
      (m/assert {:status 404} response))))

(deftest update-patient-test
  (testing "Update exisiting patient"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1, :first_name "Not-Sergey"}])
    (let [updated-patient-data (test-utils/make-test-record generate-patient {:first_name "Updated-Sergey"})
          response (app/handler (test-utils/json-request :put "/api/patients/1" :json updated-patient-data))]
      (m/assert {:status 200} response))
    (let [response (app/handler (test-utils/json-request :get "/api/patients/1"))]
      (m/assert {:status 200 :body {:first_name "Updated-Sergey"}} response))))

(deftest insert-patient-test
  (testing "Insert new patient"
    (let [patient-data (generate-patient)
          response (app/handler (test-utils/json-request :post "/api/patients" :json patient-data))]
      (m/assert {:status 201} response))))

(deftest search-patient-test
  (testing "Search patients by name"
    (test-utils/insert-test-records :patients generate-patient [{:first_name "Sergey"}
                                                                {:first_name "Ivan"}])
    (let [response (app/handler (test-utils/json-request :get "/api/patients" :query {:search-name "Ser"}))]
      (m/assert
        {:status 200
         :body {:patients [{:full_name #(str/includes? % "Sergey")}]}}
        response))))
