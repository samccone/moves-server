(ns moves-server.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [clj-http.client :as client]
            [environ.core :refer [env]])
  (:gen-class))

(defn get-access-token [code]
  (let [client-id (env :client-id)
        client-secret (env :client-secret)
        redirect-uri (env :redirect-uri)
        url (str "https://api.moves-app.com/oauth/v1/access_token?grant_type=authorization_code&code=" code "&client_id=" client-id "&client_secret=" client-secret)]
    (:body (client/post url {:throw-exceptions false}))))

(defn auth-handler [request]
  (if-let [code (-> request :params :code)]
    {:status 200
     :body (get-access-token code)}
    {:status 500
     :body "error"}))

(defn unmatched-handler [request]
  {:status 404
   :body (str "404 Unknown Route " (:uri request))})

(defn handler [request]
  (case (:uri request)
    "/auth" (auth-handler request)
    (unmatched-handler request)))

(def app (-> handler
             (wrap-resource "web/public")
             wrap-keyword-params
             wrap-params))

(defn -main [& args]
  (jetty/run-jetty app {:port (Integer. (or (env :port) "3333"))}))
