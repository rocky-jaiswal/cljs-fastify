(ns app.web.router
  (:require
   [promesa.core :as p]
   [app.web.controllers.app :as app]))

(defn router [^js app]
  (.get app "/health"
        (fn [_ reply]
          (p/let [resp (app/health)]
            (.send reply (clj->js resp))))))
