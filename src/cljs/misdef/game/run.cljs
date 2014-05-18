(ns misdef.game.run
  (:require [dommy.core :as dommy]
            [misdef.core :as core]
            [misdef.util :as util])
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
  g)

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

(defmethod core/render-object :missile [{ctx :ctx} {:keys [launch target]}]
  (doto ctx
    (aset "strokeStyle" "rgb(255,192,128)")
    (.beginPath)
    (.moveTo (:x launch) (:y launch))
    (.lineTo (:x target) (:y target))
    (.stroke)
    (.closePath)))

(defn render-objects [{objects :objects :as g}]
  (doseq [o objects]
    (core/render-object g o)))

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

(defn click [e]
  (let [x (.-clientX e)
        y (.-clientY e)
        [width height] (util/window-size)]
    (swap! core/game update-in [:objects] conj {:type        :missile    
                                                :created     (:ts @core/game)
                                                :affiliation :friend
                                                :launch      {:x (/ width 2) :y height}
                                                :target      {:x x :y y}})))

(defn keypress [e]
  (swap! core/game assoc :objects []))

(defn step []
  (->> @core/game
       (update)
       (render)
       (reset! core/game))
  nil)

(defn init [ready]
  (let [canvas (sel1 :#c)]
    (reset! core/game {:canvas   canvas
                       :ctx      (.getContext canvas "2d")
                       :tick     0
                       :ts       (util/get-time)
                       :objects  []})
    (dommy/listen! canvas :click (comp click util/prevent-default))
    (dommy/listen! js/document :keypress (comp keypress util/prevent-default))
    (ready)))
