(ns rct.hooks
  (:require
    ["react" as React]))

; *******
; Standard React Hooks
; *******
(defn- use-hook [hook]
  (fn [& args]
    (js->clj (->> args
                  (map clj->js)
                  (apply hook))
             :keywordize-keys true)))

(def use-state (use-hook (.-useState React)))
(def use-effect (use-hook (.-useEffect React)))
(def use-context (use-hook (.-useContext React)))

(defn create-context [defaultval]
  (.createContext React defaultval))

(defn with-context [context value c]
  (let [provider (.-Provider context)]
    (fn [props]
      [provider {:value value} [c props]])))
