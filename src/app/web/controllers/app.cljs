(ns app.web.controllers.app
  (:require [promesa.core :as p]
            ;; [promesa.exec :as exec]
            ["node-fetch" :as fetch]))

(defn health []
  (->
   (p/delay 1000 {:success true})
   (p/then (fn [v] v))))

(defn fetch-uuid-v1
  []
  (p/let [response (fetch "https://httpbin.org/uuid")]
    (.json response)))

(defn slow []
  (-> (p/delay 5000 "hello")
      (p/then (fn [v]
                (str "Received:" v)))))

(defn main []
  (p/let [str (slow) resp (fetch-uuid-v1)]
    (println str)
    (println (js->clj resp))))

(main)
