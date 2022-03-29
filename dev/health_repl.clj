(ns health-repl
  (:require [health-patient.server :as health-patient]
            [clojure.tools.namespace.repl :as repl]))

(repl/set-refresh-dirs "src")

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
