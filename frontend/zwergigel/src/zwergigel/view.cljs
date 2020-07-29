(ns zwergigel.view
  (:require [reagent.core :refer [atom]]
            [clojure.string :as str]
            [zwergigel.components :as c]
            [zwergigel.http :as http]
            [zwergigel.state :refer [|> <|]]))

(declare sign-up)

(defn error [] [:div "Something went wrong."])

(defn handle-video [rsp]
  (if (= 200 (:status rsp))
    (<| :video (sort-by :coefficient > (:body rsp)))
    (<| :page error)))

(defn get-video-list [] (http/query :get :video '{} handle-video))

(defn video-action [name id rkw]
  [c/button name #(http/query :post [:video rkw] {:query-params {"id" id}} handle-video)])

(defn video []
  (get-video-list)
  (fn []
    (let [url (atom "") paid (atom "")]
      [:div
       [:div.new-video
        [c/a-input "text" url {:placeholder "URL"}] [c/a-input "number" paid {:placeholder "RUB"}]
        [c/button "Add" #(http/query :post :video/add {:json-params {:url @url :paid @paid}}
                                     (fn [rsp] (if (= 200 (:status rsp))
                                                 (get-video-list)
                                                 (<| :page error))))]]
       [:div.video
        [:ol
         (for [{:keys [id] :as item} (|> :video)]
           [:li (str item) [video-action "X" id :delete]])]]])))

(defn settings [] [:div "Settings"])

(defn sign-in []
  (let [login (atom "") pass (atom "")]
    [:div.login
     [c/a-input "text" login {:placeholder "Login"}]
     [c/a-input "password" pass {:placeholder "Password"}]
     [c/button "Sign in" #(http/query-no-auth :post :sign-in
                                              {:json-params {:login @login :password @pass}}
                                              (fn [rsp] (when (= 200 (:status rsp))
                                                          (<| :token (:body rsp))
                                                          (<| :page video))))]
     [c/button "Sign up" #(<| :page sign-up)]]))

(defn sign-up []
  (let [login (atom "") pass (atom "") name (atom "")]
    [:div.login
     [c/a-input "text" login {:placeholder "Login"}]
     [c/a-input "password" pass {:placeholder "Password"}]
     [c/a-input "text" name {:placeholder "Name"}]
     [c/button "Sign up" #(http/query-no-auth :post :sign-up
                                              {:json-params {:login @login :password @pass :name @name}}
                                              (fn [rsp] (when (= 200 (:status rsp))
                                                          (<| :page sign-in))))]
     [c/button "Back" #(<| :page sign-in)]]))
