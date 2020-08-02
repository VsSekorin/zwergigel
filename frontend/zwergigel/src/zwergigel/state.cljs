(ns zwergigel.state
  (:require [reagent.core :refer [atom]]))

(defonce current (atom {:token nil
                        :page nil
                        :video nil
                        :alert nil}))

(defn <| [k v] (swap! current assoc k v))

(defn |> [key] (key @current))
