(ns health-patient.server
  (:require [mount.core :as mount :refer [defstate]]
            [ring.adapter.jetty :as ring-jetty]
            [health-patient.config :refer [config]]
            [health-patient.app :refer [app]])
  (:gen-class))

(defstate http-server
  :start (ring-jetty/run-jetty app {:port (:http-port config)
                                    :join? false})
  :stop (.stop http-server))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn -main []
  (start))
