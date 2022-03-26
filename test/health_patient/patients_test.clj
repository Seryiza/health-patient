(ns health-patient.patients-test
  (:require [re-rand :refer [re-rand]]
            [clojure.test :refer [deftest testing]]
            [ring.mock.request :as mock]
            [health-patient.app :refer [app]]
            [health-patient.test-utils :as test-utils])
  (:import [java.time LocalDate]))

(test-utils/use-mount-app-fixture)
(test-utils/use-transactable-tests-fixture)

(defn generate-patient []
  {:first_name (re-rand #"[A-Z][a-z]{1,20}")
   :last_name (re-rand #"[A-Z][a-z]{1,20}")
   :middle_name (re-rand #"[A-Z][a-z]{1,20}")
   :sex (rand-nth ["male" "female" "not-known" "not-applicable"])
   #_("TODO: make random date")
   :birth_date (LocalDate/of 2000 10 20)
   :address (re-rand #"[A-Za-z0-9\ ]{1,40}")
   :cmi_number (re-rand #"[0-9]{16}")})

(deftest test-list-patients
  (testing "List all patients"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:first_name "Sergey"}
                                     {:first_name "Uniqueman"}])
    (let [response (app (mock/request :get "/patients"))
          html (test-utils/parse-html response)]
      (test-utils/http-status? response 200)
      (test-utils/html-has-text? html [:p] "Sergey")
      (test-utils/html-has-text? html [:p] "Uniqueman"))))

(deftest test-show-patient
  (testing "Show existing user"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1
                                      :first_name "Sergey"
                                      :last_name "Zaborovsky"}])
    (let [response (app (mock/request :get "/patients/1"))
          html (test-utils/parse-html response)]
      (test-utils/http-status? response 200)
      (test-utils/html-has-text? html [:p] "Sergey Zaborovsky")))

  (testing "Don't show non-existing user"
    (let [response (app (mock/request :get "/patients/2"))]
      (test-utils/http-status? response 404))))

(deftest test-delete-patient
  (testing "Delete existing user and try delete again"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1
                                      :first_name "Sergey"}])
    (let [response (app (mock/request :delete "/patients/1"))]
      (test-utils/http-status? response 200))
    (let [response (app (mock/request :delete "/patients/1"))]
      (test-utils/http-status? response 404)))

  (testing "Delete non-existing user"
    (let [response (app (mock/request :delete "/patients/2"))]
      (test-utils/http-status? response 404))))

(deftest test-update-patient
  (testing "Update exisiting user"
    (test-utils/insert-test-records :patients
                                    generate-patient
                                    [{:id 1, :first_name "Not-Sergey"}])
    (let [response (-> (mock/request :put "/patients/1")
                       (mock/json-body (test-utils/make-test-record generate-patient {:first_name "Updated-Sergey"
                                                                                      :birth_date "2000-01-01"}))
                       app)]
      (test-utils/http-status? response 200))
    (let [response (app (mock/request :get "/patients/1"))
          html (test-utils/parse-html response)]
      (test-utils/http-status? response 200)
      (test-utils/html-has-text? html [:p] "Updated-Sergey"))))
