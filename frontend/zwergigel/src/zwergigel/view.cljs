(ns zwergigel.view
  (:require [reagent.core :refer [atom]]
            [clojure.string :as str]
            [zwergigel.components :as c]
            [zwergigel.http :as http]
            [zwergigel.state :refer [|> <|]]))

(declare sign-up)

(defn add-alert [type text] (<| :alert [type text]))
(defn error [text] (add-alert :error text))
(defn info [text] (add-alert :info text))

(defn to-page [page] (<| :alert nil) (<| :page page))

(defn handle-video [rsp]
  (if (= 200 (:status rsp))
    (<| :video (sort-by :coefficient > (:body rsp)))
    (error "Something went wrong.")))

(defn get-video-list [] (http/query :get :video '{} handle-video))

(defn video-action [name id rkw]
  [c/button name #(http/query :post [:video rkw] {:query-params {"id" id}} handle-video)])

(defn video-item [{:keys [id name paid coefficient duration thumbnailUrl] :as item}]
  (let [add (atom "")]
    [:li.row
      [:div.column.left [:img {:src thumbnailUrl :width 100}]]
      [:div.column.middle
       [:p "Name: " name]
       [:p "Duration: " duration " Paid: " paid " Coefficient: " coefficient]]
      [:div.column.right
       [c/a-input "number" add]
       [c/button "Add" #(http/query :post :video/plus {:query-params {"id" id "paid" @  add}} handle-video)]
       [:br]
       [video-action "Remove" id :delete]]]))

(defn video []
  (let [url (atom "") paid (atom "")]
    [:div
       [:div.new-video
        [c/a-input "text" url {:placeholder "URL"}] [c/a-input "number" paid {:placeholder "RUB"}]
        [c/button "Add" #(http/query :post :video/add {:json-params {:url @url :paid @paid}} handle-video)]]
       [:div.video
        [:ol
         (for [{:keys [id] :as item} (|> :video)] ^{:key id}
           [video-item item])]]]))

(defn settings [] [:div "Settings"])

(defn sign-in []
  (let [login (atom "") pass (atom "")]
    [:div.login
     [c/a-input "text" login {:placeholder "Login"}]
     [c/a-input "password" pass {:placeholder "Password"}]
     [c/button "Sign in" #(http/query-no-auth :post :sign-in
                                              {:json-params {:login @login :password @pass}}
                                              (fn [rsp] (if (= 200 (:status rsp))
                                                          (do (<| :token (:body rsp))
                                                              (get-video-list)
                                                              (to-page video))
                                                          (error "Incorrect login or password."))))]
     [c/button "Sign up" #(to-page sign-up)]]))

(defn sign-up []
  (let [login (atom "") pass (atom "") name (atom "")]
    [:div.login
     [c/a-input "text" login {:placeholder "Login"}]
     [c/a-input "password" pass {:placeholder "Password"}]
     [c/a-input "text" name {:placeholder "Name"}]
     [c/button "Sign up" #(http/query-no-auth :post :sign-up
                                              {:json-params {:login @login :password @pass :name @name}}
                                              (fn [rsp] (if (= 200 (:status rsp))
                                                          (do (to-page sign-in) (info "Successful!"))
                                                          (error "Please choose another login."))))]
     [c/button "Back" #(to-page sign-in)]]))
