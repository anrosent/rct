# rct

A library that helps you write modern React components in Clojurescript however you like. 

`rct` is designed to get out of your way. You can build your React app from CLJS functions as components, vectors as Hiccup-style templates, and native components without a single call to the library. You'll only need to call `as-component` or `as-element` when you're _providing_ a Component or Element to native code.

`rct` is also designed to give you control when you want it. You can extend the `ReactComponent` protocol if you want to define class-based components using CLJS maps, for instance. And if you don't like Hiccup vector templates, you can implement `ReactElement` for some other data type. You can even extend the Hiccup renderer with custom tags using the `render-hiccup` multimethod.

## Rationale

There are a handful of existing libraries for writing React components in Clojurescript, like [Reagent](https://github.com/reagent-project/reagent), [Rum](https://github.com/tonsky/rum), and [Om](https://github.com/omcljs/om). All of these provide features for defining React components using DSLs and managing reactive state. The closest relative of this library is probably [hx](https://github.com/Lokeh/hx) - both `hx` and `rct` try to give the user the best tool for writing modern React components, and explicitly leave state management out of scope. The main differentiating factor for `rct` is that the API is smaller (only used at boundary providing to native code) and more extensible (via protocols). 

## Stateful Components

React 16.8 brought us [Hooks](https://reactjs.org/docs/hooks-intro.html), which let you access the all the rich state and lifecycle APIs from functional components. This greatly improves the ergonomics for developers looking to build stateful React apps in idiomatic Clojure. `rct` provides wrappers to some of these in `rct.hooks`. 

## Examples

### Hello World
```clojure
(ns rct.example.hello-world
    (:require ["react-dom" :as react-dom]
              [rct.core :refer [as-element]]))

(defn hello []
   [:p {:style {:color "blue"}} "Hello World"]) 

(react-dom/render
    (as-element [hello])
    (.-body js/document))
```

### Function Component using Props
```clojure
(defn function-component [{:keys [message]}]
   [:p {:style {:color "blue"}} message])

(react-dom/render
    (as-element [function-component {:message "Hi, Function Component!"}])
    (.-body js/document))
```

### Stateful Component using React Hook
```clojure
(ns rct.example.counter
    (:require ["react-dom" :as react-dom]
              [rct.core :refer [as-element]]
              [rct.hooks :refer [use-state]]))

(defn counter [{:keys [init]}]
  (let [[v set-v] (use-state init)]
      [:<>
       [:p {} (str "Count: " v)]
       [:button {:onClick #(-> v inc set-v)} "Click to Increment"]]))

(react-dom/render 
    (r/as-element [counter {:init 0}]) 
    (.-body js/document))
```

#### Feedback?

Feel free to email, find me on Clojurians (@anson), or post an issue :) I think this could probably reach a very useful stable state with some more polish on the Hooks wrappers and a `ReactComponent` impl that lets you write class-based components as CLJS maps.

## License

Copyright © 2020 Anson Rosenthal

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
