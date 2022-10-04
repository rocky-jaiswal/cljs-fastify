(ns app.web.controllers.users
  (:require [clojure.string :as str]
            [promesa.core :as p]
            ["node-fetch" :as fetch]))

(def base-state {:email ""
                 :password ""
                 :password-confirmation ""
                 :location ""
                 :valid false
                 :created false
                 :some-db-conn {}
                 :some-http-client {}
                 :response {}})

(def handler-state
  (atom base-state))

(defn reset-to-base-state! []
  (reset! handler-state base-state))

(add-watch handler-state :watcher
           (fn [_key _atom old-state new-state]
             (prn "-- handler-state changed --")
             (prn "old-state" old-state)
             (prn "new-state" new-state)))

(defn validate [email password password-confirmation]
  (->>
   [(str/includes? email "@") (> (.-length password) 5) (= password password-confirmation)]
   (every? true?)))

(defn fetch-uuid-v1 []
  (p/let [response (fetch "https://httpbin.org/uuid")]
    (.json response)))

(defn init-state [req-body]
  (reset-to-base-state!)
  (swap! handler-state assoc :email (get req-body "email"))
  (swap! handler-state assoc :password (get req-body "password"))
  (swap! handler-state assoc :password-confirmation (get req-body "password_confirmation"))
  handler-state)

(defn validate-request [state]
  (let [st @state valid (validate (:email st) (:password st) (:password-confirmation st))]
    (if valid (swap! state assoc :valid true) (throw (js/Error. "Bad input!"))))
  state)

(defn enrich-data [state]
  (p/->>
   (p/delay 50) ;; assume we do some service invocation here
   (swap! state assoc :location "de")
   (p/promise state)))

(defn insert-in-db [state]
  (p/->>
   (p/delay 25) ;; assume we do some DB invocation here
   (swap! state assoc :created true)
   (p/promise state)))

(defn set-response [state]
  (p/->>
   (fetch-uuid-v1)
   ((fn [resp] (swap! state assoc :response (js->clj resp))))
   (p/promise state)))

(defn create [req-body]
  (p/->> req-body
         (init-state)
         (validate-request)
         (enrich-data)
         (insert-in-db)
         (set-response)
         (:response @handler-state)))