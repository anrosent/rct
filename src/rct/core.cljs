(ns rct.core
  (:require
    ["react" :as React]))

;; Open protocol for representing React Elements as data! 
(defprotocol ReactElement
  (as-element [this]))

;; Open protocol for implementing React Components! 
(defprotocol ReactComponent
  (as-component [this]))

;; ***********************************
;; Default Protocol impls for simple data
;; ***********************************
(declare props->clj create-element)

(extend-protocol ReactElement
  default ;; for text children
  (as-element [this] this))

(extend-protocol ReactComponent
  function
  (as-component [this]
    (cond

      ;; JS function components
      (some? (some-> this (.-prototype) (.-render))) this

      ;; Clojure function components
      :else (comp as-element this props->clj)))

  MultiFn 
  (as-component [this] 
    (comp as-element this props->clj))

  object ;; Native class components
  (as-component [this] this)

  Keyword ;; Keywords look nicer than strings for native components
  (as-component [this] (name this))

  default ;; Native components (sometimes Strings or Symbols)
  (as-component [this] this))

;; ***********************************
;; Private Helpers
;; ***********************************

;; Convert all props except `children` to clj data
;; (children are eagerly rendered into ReactElements, so don't mess with them)
(defn- props->clj
  [props]
  (let [children (.-children props)]
    (-> props
        (js->clj :keywordize-keys true)
        (assoc :children children))))

;; Create native React Element
(defn- create-element
  ([t]
   (create-element t {} []))
  ([t props]
   (create-element t props []))
  ([t props children]
   (let [createElement (.-createElement React)]
     (if (empty? children)
       (createElement t (clj->js props))
       (apply createElement t (clj->js props) (clj->js children))))))

;; ***********************************
;; Hiccup Template Extension (since it is so common and useful)
;; ***********************************

;; Extension point for parsing vector templates!
(defmulti render-hiccup (fn [[tag props & children]] tag))

;; Extend protocol to dispatch to Hiccup template renderer
(extend-protocol ReactElement
  cljs.core/PersistentVector
  (as-element [hiccup-template]
    (render-hiccup hiccup-template)))

(defmethod render-hiccup :default
  [[tag props & children :as args]]
  (cond 

    ;; Template is a vector of element templates
    ;; e.g. When dynamically rendering a collection of children
    (vector? tag) (vec (map as-element args))

    ;; Usually we'll be rendering [tag props ...children]
    :else (create-element (as-component tag) props (map as-element (filter some? children)))))

;; Renders a React Fragment (no props, only children)
(defmethod render-hiccup :<>
  [[tag & children]]
  (render-hiccup (vec (list* (.-Fragment React) {} children))))
