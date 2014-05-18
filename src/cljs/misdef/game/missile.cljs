(ns misdef.game.missile
  (:require [misdef.util :as util]
            [misdef.game :as game]))

(defn launch-defence-missile [e]
  (let [x (.-clientX e)
        y (.-clientY e)
        [width height] (util/window-size)]
    (swap! game/game update-in [:objects] conj {:type        :missile    
                                                :created     (:ts @game/game)
                                                :affiliation :friend
                                                :launch      {:x (/ width 2) :y height}
                                                :target      {:x x :y y}})))

(defmethod game/render-object :missile [{ctx :ctx} {:keys [launch target]}]
  (doto ctx
    (aset "strokeStyle" "rgb(255,192,128)")
    (.beginPath)
    (.moveTo (:x launch) (:y launch))
    (.lineTo (:x target) (:y target))
    (.stroke)
    (.closePath)))
