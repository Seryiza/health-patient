(ns health-patient.html
    (:require [selmer.parser :as selmer]
              [clojure.java.io :as io]))

(selmer/set-resource-path! (io/resource "html"))

(defn render [request template-name & [params]]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (selmer/render-file template-name params)})
