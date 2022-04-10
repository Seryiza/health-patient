(ns health-patient.db
  (:require [next.jdbc.connection :as jdbc-conn]
            [next.jdbc.result-set :as rs]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [health-patient.config :as config])
  (:import com.zaxxer.hikari.HikariDataSource))

(defn make-db-conn [jdbc-url]
  (jdbc-conn/->pool
    HikariDataSource
    {:jdbcUrl jdbc-url
     :maximumPoolSize 15}))

(def db (atom (make-db-conn (config/get-val :database-jdbc-url))))

(def default-jdbc-opts {:builder-fn rs/as-unqualified-maps})

(defn honey-execute!
  ([db statement]
   (honey-execute! db statement default-jdbc-opts))
  ([db statement opts]
   (jdbc/execute! db (sql/format statement) opts)))

(defn honey-execute-one!
  ([db statement]
   (honey-execute-one! db statement default-jdbc-opts))
  ([db statement opts]
   (jdbc/execute-one! db (sql/format statement) opts)))

(defn honey-update! [db statement & [opts]]
  (:next.jdbc/update-count (honey-execute-one! db statement opts)))
