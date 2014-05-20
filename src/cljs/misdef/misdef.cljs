(ns misdef.misdef
  (:require [dommy.attrs :refer [add-class! remove-class!]]
            [misdef.game.run :as g])
  (:require-macros [dommy.macros :refer [sel1]]))

(enable-console-print!)

(def schedule (or (.-requestAnimationFrame js/window)
                  (.-mozRequestAnimationFrame js/window)
                  (.-webkitRequestAnimationFrame js/window)
                  (.-msRequestAnimationFrame js/window)
                  (fn [f] (.setTimeout js/window f 16))))

(defn run []
  (schedule run)
  (g/step))

(defn game-ready []
  (add-class! (sel1 :#loading) "hidden")
  (remove-class! (sel1 :#c) "hidden")
  (schedule run)
  (println "Ready!"))

(-> js/window .-onload (set! (partial g/init game-ready)))
