(defproject health-patient "0.1.0"
  :description "Web service to work with patients records"
  :url "https://github.com/Seryiza/health-patient"

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.postgresql/postgresql "42.3.1"]
                 [com.github.seancorfield/next.jdbc "1.2.772"]
                 [migratus "1.3.6"]
                 [com.layerware/hugsql "0.5.1"]
                 [com.layerware/hugsql-adapter-next-jdbc "0.5.1"]
                 [com.zaxxer/HikariCP "5.0.1"]
                 [mount "0.1.16"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-mock "0.4.0"]
                 [enlive "1.1.6"]
                 [metosin/reitit "0.5.17"]
                 [selmer "1.12.50"]
                 [re-rand "0.1.0"]]
  :plugins [[migratus-lein "0.7.3"]]
  :min-lein-version "2.0.0"

  :resource-paths ["resources"]
  :source-paths ["src"]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "1.2.0"]]}}

  :migratus {:store :database
             :migration-dir "migrations"
             :db (System/getenv "DATABASE_JDBC_URL")})
