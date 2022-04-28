(ns health-patient-frontend.patients-test
  (:require [clojure.test :refer [deftest is]]
            [re-frame.core :as rf]
            [day8.re-frame.test :as rf-test]
            [health-patient.db]
            [health-patient.subs]
            [health-patient.patients.events]))

(def patients-example [{:first-name "Sergey" :cmi-number 123123123123}])

(deftest get-patients-successful
  (rf-test/run-test-sync
    (rf/dispatch [:initialize-db])
    (rf/dispatch [:get-patients])
    (let [loading @(rf/subscribe [:loading])]
      (is (true? (:patients loading))))

    (rf/dispatch [:get-patients-success {:patients patients-example}])
    (let [loading @(rf/subscribe [:loading])
          patients @(rf/subscribe [:patients])]
      (is (false? (:patients loading)))
      (is (= patients patients-example)))))
