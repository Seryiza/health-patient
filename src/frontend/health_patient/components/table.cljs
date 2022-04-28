(ns health-patient.components.table
  (:require [stylo.core :refer [c-eco]]
            [stylo.rule :refer [defrules]]))

(defrules {:table-header-border {:border-bottom "3px solid #edf0f3"}
           :table-row-border {:border-bottom "1px solid #edf0f3"}})

(defn table [& content]
  [:table {:class (c-eco :border-collapse :w-full)}
   content])

(defn header [& content]
  [:thead [:tr content]])

(defn header-column [& content]
  [:td {:class (c-eco [:px 4] [:py 2] :table-header-border)}
   content])

(defn body [& content]
  [:tbody content])

(defn row [& content]
  [:tr content])

(defn column [& content]
  [:td {:class (c-eco [:px 4] [:py 2] :table-row-border)}
   content])
