(ns app.core
  (:require
   [promesa.core :as p]
   ["fastify" :as fastify]
   [app.web.router :as r]))

(enable-console-print!)
(set! *warn-on-infer* true)

(defonce port 3000)
(defonce server (volatile! nil))

(defn start-server []
  (let [app (fastify {:logger true})]
    (p/let [_ (r/router app)])
    (.listen app (clj->js {:port port}) (fn [_err]
                                          (js/console.log "Server started")
                                          (vreset! server app)))))

(defn start! []
  (js/console.warn "Starting server")
  (p/let [_ (start-server)]))

(defn stop! [done]
  (js/console.warn "Stopping server")
  (when-some [svr @server]
    (.close svr
            (fn [err]
              (js/console.warn "Server stopped" err)
              (done)))))

(defn main []
  (p/let [_ (start!)]))