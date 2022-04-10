(ns health-patient.config)

(def config
  (atom
    {:http-port (Integer/parseInt (System/getenv "HTTP_PORT"))
     :database-jdbc-url (System/getenv "DATABASE_JDBC_URL")
     :test-database-jdbc-url (System/getenv "TEST_DATABASE_JDBC_URL")}))

(defn get-val [config-key]
  (get @config config-key))
