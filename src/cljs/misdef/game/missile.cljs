(ns misdef.game.missile
  (:require [misdef.util :refer [random rgb->color] :as util]
            [misdef.game :as game]
            [misdef.game.explosion :as explosion]))

(enable-console-print!)

(def missile-color {:friend (rgb->color 32 255 32)
                    :foe    (rgb->color 255 32 32)})

(def missile-velocity {:friend 0.5
                       :foe    0.1})

(defn launch-defence-missile [e]
  (let [[width height]  (util/window-size)
        id  (game/next-object-id)
        x   (- (.-clientX e) (/ width 2))
        y   (- height (.-clientY e))]
    (swap! game/game assoc-in [:objects id] {:id           id
                                             :type         :missile    
                                             :created      (:ts @game/game)
                                             :affiliation  :friend
                                             :sx           0
                                             :sy           0
                                             :len          (Math/sqrt (+ (* x x) (* y y)))
                                             :angle        (Math/atan2 y x)
                                             :x            0
                                             :y            0})))

(defn launch-attack-missile [{:keys [width height ts] :as g}]
  (let [id    (game/next-object-id)
        w2    (/ width 2)
        sx    (- (random width) w2)
        tx    (- (random width) w2)
        dx    (- sx tx)
        adx   (Math/abs dx)
        len   (Math/sqrt (+ (* adx adx) (* height height)))
        angle (+ (Math/atan2 height dx) Math/PI)]
    (assoc-in g [:objects id] {:id           id
                               :type         :missile    
                               :created      ts
                               :affiliation  :foe
                               :sx           sx
                               :sy           height
                               :len          len
                               :angle        angle
                               :x            sx
                               :y            height})))

(defn detonation [g ts {:keys [id len affiliation x y]}]
  (-> g
      (update-in [:objects] dissoc id)
      (explosion/explosion x y affiliation ts)))

(defn destroyed [{:keys [score] :as g} id]
  (-> g
      (update-in [:objects] dissoc id)
      (assoc :score (+ score 1000))))

(defmethod game/update-object :missile [{:keys [ts] :as g} {:keys [id created angle sx sy len affiliation] :as o}]
  (let [age   (- ts created)
        dist  (* (missile-velocity affiliation) age)
        x     (+ sx (* dist (Math/cos angle)))
        y     (+ sy (* dist (Math/sin angle)))]
    (cond
      (> dist len) (detonation g ts o)
      (and (= affiliation :foe) (explosion/inside-defence-explosion? g x y)) (destroyed g id)
      :else (update-in g [:objects id] assoc :x x :y y))))

(defmethod game/render-object :missile [{:keys [ctx]} {:keys [affiliation sx sy x y]}]
  (doto ctx
    (aset "strokeStyle" (missile-color affiliation))
    (.beginPath)
    (.moveTo sx sy)
    (.lineTo x y)
    (.stroke)))
