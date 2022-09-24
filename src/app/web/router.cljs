(ns app.web.router
  (:require
   [promesa.core :as p]
   [app.web.controllers.app :as app]))

(defn health-handler [_ reply]
  (p/let [resp (app/health)]
    (.send reply (clj->js resp))))

(defn router [^js app]
  (.get app "/health" health-handler))
