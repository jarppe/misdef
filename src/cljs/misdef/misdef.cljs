(ns misdef.misdef
  (:require [misdef.game :as game]))

(enable-console-print!)

(def schedule (or (.-requestAnimationFrame js/window)
                  (.-mozRequestAnimationFrame js/window)
                  (.-webkitRequestAnimationFrame js/window)
                  (.-msRequestAnimationFrame js/window)
                  (fn [f] (.setTimeout js/window f 16))))

(defn run []
  (schedule run)
  (game/step))

(defn set-display [value id]
  (-> js/document
      (.getElementById id)
      (.-style)
      (.-display)
      (set! value)))

(def hide (partial set-display "none"))
(def show (partial set-display "table-cell"))

(defn main []
  (println "Here we go!")
  (game/init (fn []
               (hide "loading")
               (show "c")
               (schedule run)
               (println "Ready!"))))

(-> js/window .-onload (set! main))
