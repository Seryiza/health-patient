(ns health-patient-frontend.patients-test
  (:require [clojure.test :refer [deftest is]]
            [re-frame.core :as rf]
            [day8.re-frame.test :as rf-test]
            [health-patient.db]
            [health-patient.subs]))

(deftest start-page-is-home
  (rf-test/run-test-sync
    (rf/dispatch [:initialize-db])
    (let [active-page @(rf/subscribe [:active-page])]
      (is (= active-page :home)))))
