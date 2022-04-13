(ns health-patient.app)

(defn init []
  (js/alert "Connected!"))

(defn ^:dev/after-load render []
  (js/console.log "Reloaded"))
