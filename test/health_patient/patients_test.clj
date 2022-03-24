(ns health-patient.patients-test
  (:require [re-rand :refer [re-rand]]
            [clojure.test :refer [deftest is]]
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
  (test-utils/insert-test-records :patients
                                  generate-patient
                                  [{:first_name "Sergey"}
                                   {:first_name "Uniqueman"}])
  (let [response (app (mock/request :get "/patients"))
        html (test-utils/parse-html response)]
    (test-utils/http-status? response 200)
    (test-utils/html-has-text? html [:p] "Sergey")
    (test-utils/html-has-text? html [:p] "Uniqueman")))
