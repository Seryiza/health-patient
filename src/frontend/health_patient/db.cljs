(ns health-patient.db)

(def default-db {:active-page :home
                 :loading {}
                 :patients []
                 :patient {:sex "not-known"}
                 :flash []})
