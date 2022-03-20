(ns health-repl
  (:require [health-patient.server :as health-patient]
            [clojure.tools.namespace.repl :as repl]
            [selmer.parser :as selmer]))

(repl/set-refresh-dirs "src")
(selmer/cache-off!)

(defn start []
  (health-patient/start))

(defn stop []
  (health-patient/stop))

(defn restart []
  (stop)
  (start))

(defn refresh []
  (stop)
  (repl/refresh :after 'health-patient.server/start))
