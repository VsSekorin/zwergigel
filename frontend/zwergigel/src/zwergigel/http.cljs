(ns zwergigel.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as client]
            [cljs.core.async :refer [<!]]
            [zwergigel.state :refer [|> <|]]))

;(def backend "http://localhost:8080/")
(def backend "https://zwergigel.herokuapp.com/")

(def routes {:sign-in "sign-in/"
             :sign-up "sign-up/"
             :video "video/"
             :video/add "video/add/"
             :video/delete "video/delete/"
             :video/plus "video/plus/"
             :video/delete-all "video/delete-all/"})

(defmulti get-path keyword?)
(defmethod get-path true [kw] (kw routes))
(defmethod get-path false [col] ((apply keyword col) routes))

(defn- -query [method url map handler]
  (go (let [response (<! ((case method :get client/get :post client/post) (str backend url)
                          (merge {:with-credentials? false} map)))] (handler response))))

(defn query
  ([method rkw handler] (query method rkw '{} handler))
  ([method rkw map handler] (-query method (str (get-path rkw) (|> :token)) map handler)))

(defn query-no-auth [method rkw map handler] (-query method (get-path rkw) map handler))
