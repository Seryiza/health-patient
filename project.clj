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
                 [ring/ring-json "0.5.1"]
                 [funcool/struct "1.4.0"]
                 [route-map "0.0.7"]
                 [hiccup "1.0.5"]
                 [re-rand "0.1.0"]
                 [healthsamurai/matcho "0.3.9"]
                 [com.health-samurai/macrocss "0.1.0"]]
  :plugins [[migratus-lein "0.7.3"]]
  :min-lein-version "2.0.0"

  :resource-paths ["resources"]
  :source-paths ["src/common" "src/backend"]
  :profiles {:uberjar {:main health-patient.server
                       :uberjar-name "health-patient.jar"
                       :aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "1.2.0"]]}
             :cljs {:source-paths ["src/frontend" "src/common"]
                    :dependencies [[thheller/shadow-cljs "2.18.0"]
                                   [reagent "1.1.1"]
                                   [re-frame "1.2.0"]
                                   [day8.re-frame/test "0.1.5"]
                                   [kibu/pushy "0.3.8"]
                                   [day8.re-frame/http-fx "0.2.4"]
                                   [cljs-ajax "0.8.4"]
                                   [cider/cider-nrepl "0.24.0"]]}}
  :migratus {:store :database
             :migration-dir "migrations"
             :db (System/getenv "DATABASE_JDBC_URL")})
