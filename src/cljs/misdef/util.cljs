(ns misdef.util)

(def pi2 (* Math/PI 2))

(defn get-time []
  (.getTime (js/Date.)))

(defn window-size []
  [(.-innerWidth js/window) (.-innerHeight js/window)])

(defn prevent-default [e]
  (.preventDefault e)
  (.stopPropagation e)
  e)

(defn rgb->color [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn rgba->color [r g b a]
  (str "rgba(" r "," g "," b "," a ")"))

(defn color-with-alpha [[r g b] a]
  (str "rgba(" r "," g "," b "," a ")"))

(defn random
  ([]
    (.random js/Math))
  ([n]
    (* n (random))))
