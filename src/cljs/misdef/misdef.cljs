(ns misdef.misdef
  (:require [figwheel.client :as fw :include-macros true]
            [misdef.game :refer [game]]
            [misdef.render :refer [step click keyb]]))

(enable-console-print!)

(def schedule (or (.-requestAnimationFrame js/window)
                  (.-mozRequestAnimationFrame js/window)
                  (.-webkitRequestAnimationFrame js/window)
                  (.-msRequestAnimationFrame js/window)
                  (fn [f] (.setTimeout js/window f 16))))

(defn run []
  (step)
  (schedule run))

(defn set-display [id value]
  (-> js/document
      (.getElementById id)
      (.-style)
      (.-display)
      (set! value)))

(defn main []
  (println "Here we go!")
  (let [canvas (.getElementById js/document "c")]
    (reset! game {:canvas canvas})
    (.addEventListener canvas "click" click false)
    (.addEventListener js/document "keypress" keyb false))
  (set-display "loading" "none")
  (set-display "c" "table-cell")
  (schedule run)
  (println "Ready!"))

(-> js/window .-onload (set! main))

(fw/watch-and-reload
  :jsload-callback (fn [] (print "reloaded")))
