(ns app.web.controllers.app
  (:require [promesa.core :as p]
            ["node-fetch" :as fetch]))

(defn slow [late]
  (-> (p/delay late nil)
      (p/then (fn [x] x))))

(defn fetch-uuid-v1
  []
  (p/let [response (fetch "https://httpbin.org/uuid")]
    (.json response)))

(defn health []
  (->
   (p/let [_ (slow 500) resp (fetch-uuid-v1)]
     resp)))
