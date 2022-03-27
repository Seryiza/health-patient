(ns health-patient.db
  (:require [mount.core :refer [defstate]]
            [next.jdbc.connection :as jdbc-conn]
            [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as hugsql-adapter]
            [health-patient.config :refer [config]])
  (:import com.zaxxer.hikari.HikariDataSource))

(defstate db
  :start (do
           (hugsql/set-adapter! (hugsql-adapter/hugsql-adapter-next-jdbc))
           (jdbc-conn/->pool HikariDataSource {:jdbcUrl (:database-jdbc-url config)
                                               :maximumPoolSize 15}))
  :stop (.close db))
