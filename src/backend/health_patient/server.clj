(ns health-patient.server
  (:require [ring.adapter.jetty :as ring-jetty]
            [health-patient.config :as config]
            [health-patient.app :refer [app]])
  (:gen-class))

(def http-server (atom nil))

(defn create-server [handler]
  (ring-jetty/run-jetty handler {:port (config/get-val :http-port)
                                 :join? false}))

(defn stop-server [server]
  (if (not (nil? server))
    (.stop server)))

(defn start []
  (reset! http-server (create-server @app)))

(defn stop []
  (swap! http-server stop-server))

(defn -main []
  (start))
