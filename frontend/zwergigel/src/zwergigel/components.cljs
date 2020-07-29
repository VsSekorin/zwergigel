(ns zwergigel.components)

(defn button [text action] [:button {:on-click action} text])

(defn a-input [type value map]
  [:input (merge map {:type type :value @value :on-change #(reset! value (-> % .-target .-value))})])