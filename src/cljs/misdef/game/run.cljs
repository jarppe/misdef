(ns misdef.game.run
  (:require [dommy.core :as dommy]
            [misdef.game :as game]
            [misdef.util :as util]
            [misdef.game.missile :as missile])
  (:require-macros [dommy.macros :refer [sel1]]))

;;
;; Game update:
;;

(defn update-tick [{:keys [tick ts fps] :as g}]
  (let [now     (util/get-time)
        fps     (if (zero? (mod tick 10))
                  (Math/round (/ 1000.0 (- now ts)))
                  fps)]
    (assoc g :tick  (inc tick)
             :ts    now
             :fps   fps)))

(defn update-objects [{objects :objects :as g}]
  (doseq [o objects]
    (game/update-object g o)))

(defn update [g]
  (-> g
      update-tick
      update-objects)
  g)

;;
;; Rendering:
;;

(defn clean-canvas [{:keys [ctx canvas width height]}]
  (doto canvas
    (aset "width" width)
    (aset "height" height))
  (doto ctx
    (aset "fillStyle" "rgb(32,32,32)")
    (.fillRect 0 0 width height)))

(defn show-fps [{:keys [ctx width fps]}]
  (doto ctx
    (aset "textAlign" "center")
    (aset "textBaseline" "top")
    (aset "font" "18px sans-serif")
    (aset "fillStyle" "rgba(32,255,32,0.4)")
    (.fillText (str "Missile Defence (" fps ")") (/ width 2) 2)))

(defn render-objects [{objects :objects :as g}]
  (doseq [o objects]
    (game/render-object g o)))

(defn render [{:keys [ctx] :as g}]
  (let [[width height] (util/window-size)]
    (doto (assoc g :width  width
                   :height height)
      (clean-canvas)
      (show-fps)
      (render-objects))
    g))

;;
;; Game life-cycle:
;;

(defn keypress [e]
  (swap! game/game assoc :objects []))

(defn step []
  (->> @game/game
       (update)
       (render)
       (reset! game/game))
  nil)

(defn init [ready]
  (let [canvas (sel1 :#c)]
    (reset! game/game {:canvas   canvas
                       :ctx      (.getContext canvas "2d")
                       :tick     0
                       :ts       (util/get-time)
                       :objects  []})
    (dommy/listen! canvas :click (comp missile/launch-defence-missile util/prevent-default))
    (dommy/listen! js/document :keypress (comp keypress util/prevent-default))
    (ready)))
