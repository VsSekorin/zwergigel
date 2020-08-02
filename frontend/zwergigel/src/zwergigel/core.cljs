(ns zwergigel.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [zwergigel.state :refer [|> <|]]
      [zwergigel.view :as v]
      [zwergigel.http :as http]
      [zwergigel.components :as c]))

(def menu-items {"Video"    v/video
                 "Settings" v/settings})
(defn menu []
  [:div.menu
   (for [[name page] menu-items] ^{:key name} [c/button name #(<| :page page)])
   [c/button "Sign out" #(<| :token nil)]])

(defn main-page []
  [:div
   [:h1 [c/button "Zwergigel" #(<| :page (if (|> :token) v/video v/sign-in))]]
   (if (|> :alert)
     (let [[type text] (|> :alert)]
       [:div {:class [type "box"] :on-click #(<| :alert nil)} text]))
   (cond
     (|> :token) [:div [menu] [(|> :page)]]
     (= (|> :page) v/sign-up) [(|> :page)]
     :else [v/sign-in])])

(defn init! [] (d/render [main-page] (.getElementById js/document "app")))
