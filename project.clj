(defproject health-patient "1.1.0"
  :description "Web service to work with patients records"
  :url "https://github.com/Seryiza/health-patient"

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.11.4"]
                 [org.postgresql/postgresql "42.3.1"]
                 [com.github.seancorfield/next.jdbc "1.2.772"]
                 [migratus "1.3.6"]
                 [com.zaxxer/HikariCP "5.0.1"]
                 [com.github.seancorfield/honeysql "2.2.868"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-mock "0.4.0"]
                 [ring/ring-json "0.5.1"]
                 [funcool/struct "1.4.0"]
                 [enlive "1.1.6"]
                 [route-map "0.0.7"]
                 [hiccup "1.0.5"]
                 [re-rand "0.1.0"]
                 [prismatic/dommy "1.1.0"]
                 [cljs-ajax "0.8.4"]
                 [healthsamurai/matcho "0.3.9"]]
  :plugins [[migratus-lein "0.7.3"]
            [lein-cljsbuild "1.1.8"]
            [lein-figwheel "0.5.20"]]
  :min-lein-version "2.0.0"

  :resource-paths ["resources"]
  :source-paths ["src/common" "src/backend"]
  :profiles {:uberjar {:main health-patient.server
                       :uberjar-name "health-patient.jar"
                       :aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "1.2.0"]]}}

  :cljsbuild {:builds [{:id "app"
                        :source-paths ["src/common" "src/frontend"]
                        :figwheel true
                        :compiler {:main "health-patient.app"
                                   :asset-path "/js/out"
                                   :output-to "resources/public/js/app.js"
                                   :optimizations :none
                                   :pretty-print true}}]}

  :migratus {:store :database
             :migration-dir "migrations"
             :db (System/getenv "DATABASE_JDBC_URL")})
