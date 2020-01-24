(ns examples
  (:require
    ["react-dom" :as react-dom]
    [rct.core :refer [as-element]]))

(defn big-word
  [{:keys [text]}]
  [:p {} text])

(defn foob
  [{:keys [children header]}]
  [:<>
   [:h1 {} header]
   children
   [:h1 {} "Footer"]])

(defn root []
  (let [children (vec (map #(vector :p {} (str %)) (range 10)))]
    [:<>
     [foob {:header "Header boi"} children]
     [big-word {:text "Hello"}]
     [big-word {:text "World!"}]]))

(defn start []
  (react-dom/render
    (as-element [root])
    (.getElementById js/document "app")))
