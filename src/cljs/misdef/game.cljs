(ns misdef.game
  (:require-macros [misdef.core :refer [on-event]]))

;;
;; Game state:
;;

(defonce game (atom nil))

;;
;; Utils:
;;

(defn window-size []
  [(.-innerWidth js/window) (.-innerHeight js/window)])

(def pi2 (* Math/PI 2))

(defn get-time []
  (.getTime (js/Date.)))

;;
;; Game update:
;;

(defn update-tick [{:keys [tick ts fps] :as g}]
  (let [now     (get-time)
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

(defmulti render-object (fn [_ object] (:type object)))

(defmethod render-object :missile [{ctx :ctx} {:keys [launch target]}]
  (doto ctx
    (aset "strokeStyle" "rgb(255,192,128)")
    (.beginPath)
    (.moveTo (:x launch) (:y launch))
    (.lineTo (:x target) (:y target))
    (.stroke)
    (.closePath)))

(defn render-objects [{objects :objects :as g}]
  (doseq [o objects]
    (render-object g o)))

(defn render [{:keys [ctx] :as g}]
  (let [[width height] (window-size)]
    (doto (assoc g :width  width
                   :height height)
      (clean-canvas)
      (show-fps)
      (render-objects))
    g))

;;
;; Game life-cycle:
;;

(defn step []
  (->> @game
       (update)
       (render)
       (reset! game))
  nil)

(defn init [cb]
  (let [canvas (.getElementById js/document "c")]
    (reset! game {:canvas   canvas
                  :ctx      (.getContext canvas "2d")
                  :tick     0
                  :ts       (get-time)
                  :objects  []})
    (on-event canvas "click"
      (let [x (.-clientX e)
            y (.-clientY e)
            [width height] (window-size)]
        (swap! game update-in [:objects] conj {:type        :missile    
                                               :created     (:ts @game)
                                               :affiliation :friend
                                               :launch      {:x (/ width 2) :y height}
                                               :target      {:x x :y y}})))
    (on-event js/document "keypress"
      (swap! game assoc :objects []))
    (cb)))
