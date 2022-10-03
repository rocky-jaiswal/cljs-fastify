(ns app.web.controllers.app
  (:require [promesa.core :as p]
            ["node-fetch" :as fetch]))

(defn slow [with-delay fn]
  (-> (p/delay with-delay nil)
      (p/then (fn))))

(defn fetch-uuid-v1 []
  (p/let [response (fetch "https://httpbin.org/uuid")]
    (.json response)))

(defn health []
  (p/let [_ (slow 500 (fn [] {})) resp (fetch-uuid-v1)]
    resp))
