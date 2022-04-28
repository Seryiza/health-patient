(ns health-patient.components.common
  (:require [stylo.core :refer [c-eco]]
            [stylo.rule :refer [defrules]]))

(defrules {:card-shadow {:box-shadow "0 0.125rem 1rem rgba(27, 40, 50, 0.04),0 0.125rem 2rem rgba(27, 40, 50, 0.08),0 0 0 0.0625rem rgba(27, 40, 50, 0.024)"}
           :input-border {:border "1px solid #A2AFB9"}})

(defn h1 [& content] [:h1 {:class (c-eco :font-bold)} content])
(defn h2 [& content] [:h2 {:class (c-eco [:mb 5] :font-bold :text-2xl)} content])

(defn container [& content]
  [:section {:class (c-eco [:w 300] :mx-auto)}
   content])

(defn nav [& content]
  [:nav {:class (c-eco :flex :justify-between)}
   content])

(defn nav-section [& content]
  [:ul
   content])

(defn nav-item [& content]
  [:li {:class (c-eco [:py 4] [:px 2])}
   content])

(defn card [& content]
  [:article {:class (c-eco [:my 16] [:py 16] [:px 8] :card-shadow)}
   content])

(defn link [url text]
  [:a {:class (c-eco [:text :blue-500])
       :href url}
   text])

(defn label [& content]
  [:label {:class (c-eco :font-normal :block [:mb 1])}
   content])

(defn input [attributes]
  [:input (merge {:class (c-eco :input-border :w-full [:p 3] [:mb 4] :rounded)} attributes)])

(defn select [attributes & content]
  [:select (merge {:class (c-eco :input-border [:bg :white] :w-full [:p 3] [:mb 4] :rounded)} attributes)
   content])

(defn option [attributes label]
  [:option attributes label])

(def button-class
  (c-eco [:bg :blue-700]
         [:text :white]
         :text-base
         :text-center
         [:mb 4]
         [:p 3]
         :rounded
         :block
         :w-full))

(defn button [attributes & content]
  [:button (merge {:class button-class} attributes)
   content])

(defn link-button [url text]
  [:a {:class button-class
       :href url}
   text])
