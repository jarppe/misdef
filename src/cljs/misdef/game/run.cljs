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
  (reduce game/update-object g (vals objects)))

(defn update [g]
  (-> g
      update-tick
      update-objects))

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

(defn object-priority [object]
  0)

(defn render-objects [{:keys [objects ctx width height] :as g}]
  (doto ctx
    (.save)
    (.translate (/ width 2) height)
    (.scale 1 -1))
  (doto ctx
    (aset "strokeStyle" "rgb(255,192,128)")
    (.beginPath)
    (.arc 0 0 100 0 Math/PI)
    (.stroke)
    (.beginPath)
    (.arc 0 100 50 (/ Math/PI 4) (* 3 (/ Math/PI 4)))
    (.stroke))
  (doseq [o (->> objects vals (sort-by object-priority))]
    (.save ctx)
    (game/render-object g o)
    (.restore ctx))
  (.restore ctx))

(defn render [{:keys [ctx] :as g}]
  (let [[width height] (util/window-size)]
    (.save ctx)
    (doto (assoc g :width  width
                   :height height)
      (clean-canvas)
      (show-fps)
      (render-objects))
    (.restore ctx)
    g))

;;
;; Game life-cycle:
;;

(defn keypress [e]
  (swap! game/game assoc :objects {}))

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
                       :objects  {}})
    (dommy/listen! canvas :click (comp missile/launch-defence-missile util/prevent-default))
    (dommy/listen! js/document :keypress (comp keypress util/prevent-default))
    (ready)))
