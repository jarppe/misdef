(ns misdef.game.missile
  (:require [misdef.util :as util]
            [misdef.game :as game]))

(enable-console-print!)

(defn launch-defence-missile [e]
  (let [[width height]  (util/window-size)
        id  (game/next-object-id)
        x   (- (.-clientX e) (/ width 2))
        y   (- height (.-clientY e))]
    (swap! game/game update-in [:objects] assoc id {:id           id
                                                    :type         :missile    
                                                    :created      (:ts @game/game)
                                                    :affiliation  :friend
                                                    :len          (Math/sqrt (+ (* x x) (* y y)))
                                                    :angle        (Math/atan2 y x)})))

(def missile-color {:friend "rgb(32,255,32)"
                    :foe    "rgb(255,32,32)"})

(defmethod game/update-object :missile [{:keys [ts] :as g} {:keys [created len] :as o}]
  (let [age   (- ts created)
        dist  (* missile-velocity age)]
    (if (> dist len)
      (update-in g [:objects] dissoc (:id o))
      g)))

(def missile-velocity 0.1)

(defmethod game/render-object :missile [{:keys [ctx ts]} {:keys [created angle len affiliation]}]
  (let [age   (- ts created)
        dist  (* missile-velocity age)
        x     (* dist (Math/cos angle))
        y     (* dist (Math/sin angle))]
    (doto ctx
      (aset "strokeStyle" (missile-color affiliation))
      (.beginPath)
      (.moveTo 0 0)
      (.lineTo x y)
      (.stroke))))
