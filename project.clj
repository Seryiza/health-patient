(defproject health-patient "0.1.0"
  :description "Web service to work with patients records"
  :url "https://github.com/Seryiza/health-patient"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.postgresql/postgresql "42.3.1"]
                 [migratus "1.3.6"]
                 [com.layerware/hugsql "0.5.1"]
                 [hikari-cp "2.13.0"]
                 [mount "0.1.16"]]
  :plugins [[migratus-lein "0.7.3"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db (System/getenv "DATABASE_URL")})
