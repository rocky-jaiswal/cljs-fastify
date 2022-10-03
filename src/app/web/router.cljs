(ns app.web.router
  (:require
   [promesa.core :as p]
   [app.web.controllers.app :as main]
   [app.web.controllers.users :as users]))

(defn health-handler [_ reply]
  (p/let [resp (main/health)]
    (.send reply (clj->js resp))))

(defn create-user [req reply]
  (p/let [resp (users/create (js->clj (.-body req)))]
    (.send reply (clj->js resp))))

(defn router [^js app]
  (.get app "/health" health-handler)
  (.post app "/users" create-user))
