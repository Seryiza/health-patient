(ns health-patient.html
    (:require [hiccup.core :as hiccup]
              [clojure.java.io :as io]))

(defn render [template]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (hiccup/html template)})
