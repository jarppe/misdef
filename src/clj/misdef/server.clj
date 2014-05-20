(ns misdef.server
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [org.httpkit.server :as httpkit]
            [ring.util.http-response :refer [ok content-type header] :as resp]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [misdef.env :refer [dev?]]
            [misdef.index :refer [index-content]]))

(def app-routes
  [(GET  "/" [] (-> (index-content)
                    (ok)
                    (content-type "text/html; charset=\"UTF-8\"")))
   (GET "/dev/ping" [] (ok "pong\n"))
   (route/resources "/")
   (route/not-found "Not found")])

(def app (apply routes app-routes))

(defn run [& [port]]
  (httpkit/run-server (if dev? (var app) app)
                      {:port (Integer/parseInt (or port "8080") 10)
                       :join? false})
  "App running...")
