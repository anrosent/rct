(ns examples
  (:require
    ["react-dom" :as react-dom]
    [rct.core :refer [as-element]]
    [rct.hooks :refer [use-state]]))

;; Just a string!
(def hello-world "Hello, World!")

(def hello-world-hiccup [:p {:style {:color "blue"}} "Hello, World! I'm a hiccup template."])

(defn function-component [{:keys [message]}]
  [:p {} message])

(defn stateful-component [{:keys [init]}]
  (let [[v set-v] (use-state init)]
    [:<>
     [function-component {:message (str v)}]
     [:button {:onClick #(-> v inc set-v)} "Increment"]]))

(defn example
  [{:keys [name children]}]
  [:<>
   [:h2 {} name]
   children])

(defn root []
  [:<>
   [:h1 {} "Examples"]

   [example {:name "Hello World"}
    hello-world]

   [example {:name "Hello, World! (hiccup template)"}
    hello-world-hiccup]

   [example {:name "Function Component"}
    [function-component {:message "I'm a function component"}]]

   [example {:name "Stateful Component (using a React Hook)"}
    [stateful-component {:init 0}]]])

(defn start []
  (react-dom/render
    (as-element [root])
    (.getElementById js/document "app")))
