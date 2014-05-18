(ns misdef.game.explosion
  (:require [misdef.util :as util]
            [misdef.game :as game]))

(enable-console-print!)

(def explosion-color {:friend [32 255 32]
                      :foe    [255 32 32]})

(def explosion-velocity 0.05)
(def explosion-age 1000)
(def explosion-glow 500)
(def explosion-die (+ explosion-age explosion-glow))

(defn explosion [x y affiliation ts]
  {:id           (game/next-object-id)
   :type         :explosion    
   :affiliation  :friend
   :created      ts
   :x            x
   :y            y})

(defmethod game/update-object :explosion [{:keys [ts] :as g} {:keys [created x y] :as o}]
  (let [age (- ts created)]
    (if (> age explosion-die)
      (update-in g [:objects] dissoc (:id o))
      g)))

(defn explosion-alpha [age]
  (let [n (if (< age explosion-age)
            0.4
            (- 0.4 (* 0.4 (/ (- age explosion-age) explosion-glow))))]
    (if (pos? n) n 0)))

(defmethod game/render-object :explosion [{:keys [ctx ts]} {:keys [created x y affiliation]}]
  (let [age   (- ts created)
        r     (* explosion-velocity age)]
    (doto ctx
      (aset "strokeStyle" (apply util/rgb->color (explosion-color affiliation)))
      (aset "fillStyle" (util/color-with-alpha (explosion-color affiliation) (explosion-alpha age)))
      (.beginPath)
      (.arc x y r 0 util/pi2)
      (.fill)
      (.stroke))))
