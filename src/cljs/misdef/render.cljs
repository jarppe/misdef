(ns misdef.render
  (:require-macros [misdef.core :refer [defevt]])
  (:require [misdef.game :refer [game]]))

(enable-console-print!)

(defn canvas-size []
  [(.-innerWidth js/window) (.-innerHeight js/window)])

(def pi2 (* Math/PI 2))

;;
;; User events:
;;

(defevt click
  (let [x (.-clientX e)
        y (.-clientY e)
        [width height] (canvas-size)]
    (swap! game update-in [:objects] conj {:type        :missile    
                                           :created     (:tick @game)
                                           :affiliation :friend
                                           :launch      {:x (/ width 2) :y height}
                                           :target      {:x x :y y}})))

(defevt keyb
  (swap! game assoc :objects [] :tick 0))

;;
;; Game update:
;;

(defn update-tick [{:keys [tick tick-time fps] :as g}]
  (let [now     (.getTime (js/Date.))
        fps     (if (zero? (mod tick 10))
                  (Math/round (/ 1000.0 (- now tick-time)))
                  fps)]
    (assoc g :tick       (inc tick)
             :tick-time  now
             :fps        fps)))

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
  (set! (.-width canvas) width) 
  (set! (.-height canvas) height)
  (set! (.-fillStyle ctx) "rgb(32,32,32)")
  (.fillRect ctx 0 0 width height))

(defn show-fps [{:keys [ctx width fps]}]
  (set! (.-textAlign ctx) "center")
  (set! (.-textBaseline ctx) "top")
  (set! (.-font ctx) "18px sans-serif")
  (set! (.-fillStyle ctx) "rgba(32,255,32,0.4)")
  (.fillText ctx (str "Missile Defence (" fps ")") (/ width 2) 2))

(defmulti render-object (fn [_ object] (:type object)))

(defmethod render-object :missile [{ctx :ctx} {:keys [launch target]}]
  (set! (.-strokeStyle ctx) "rgb(255,192,128)")
  (doto ctx
    (.beginPath)
    (.moveTo (:x launch) (:y launch))
    (.lineTo (:x target) (:y target))
    (.stroke)))

(defn render-objects [{objects :objects :as g}]
  (doseq [o objects]
    (render-object g o)))

(defn render [{:keys [canvas] :as g}]
  (let [ctx             (.getContext canvas "2d")
        [width height]  (canvas-size)]
    (doto (assoc g :ctx    ctx
                   :width  width
                   :height height)
      (clean-canvas)
      (show-fps)
      (render-objects))
    g))

;;
;; Game step:
;;

(defn step []
  (->> @game
       (update)
       (render)
       (reset! game))
  nil)
