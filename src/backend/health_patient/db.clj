(ns health-patient.db
  (:require [mount.core :refer [defstate]]
            [next.jdbc.connection :as jdbc-conn]
            [next.jdbc.result-set :as rs]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [health-patient.config :refer [config]])
  (:import com.zaxxer.hikari.HikariDataSource))

(defstate db
  :start (jdbc-conn/->pool HikariDataSource {:jdbcUrl (:database-jdbc-url config)
                                             :maximumPoolSize 15})
  :stop (.close db))

(def default-opts {:builder-fn rs/as-unqualified-maps})

(defn honey-execute!
  ([db statement]
   (honey-execute! db statement default-opts))
  ([db statement opts]
   (jdbc/execute! db (sql/format statement) opts)))

(defn honey-execute-one!
  ([db statement]
   (honey-execute-one! db statement default-opts))
  ([db statement opts]
   (jdbc/execute-one! db (sql/format statement) opts)))

(defn honey-update! [db statement & [opts]]
  (:next.jdbc/update-count (honey-execute-one! db statement opts)))
