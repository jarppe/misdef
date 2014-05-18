(ns misdef.game.explosion
  (:require [misdef.util :as util]
            [misdef.game :as game]))

(enable-console-print!)

(def explosion-color {:friend "rgb(32,255,32)"
                      :foe    "rgb(255,32,32)"})

(def explosion-velocity 0.08)
(def explosion-age 1000)

(defn explosion [x y affiliation ts]
  {:id           (game/next-object-id)
   :type         :explosion    
   :affiliation  :friend
   :created      ts
   :x            x
   :y            y})

(defmethod game/update-object :explosion [{:keys [ts] :as g} {:keys [created x y] :as o}]
  (let [age (- ts created)]
    (if (> age explosion-age)
      (update-in g [:objects] dissoc (:id o))
      g)))

(defmethod game/render-object :explosion [{:keys [ctx ts]} {:keys [created x y affiliation]}]
  (let [age   (- ts created)
        r     (* explosion-velocity age)]
    (doto ctx
      (aset "strokeStyle" (explosion-color affiliation))
      (.beginPath)
      (.arc x y r 0 util/pi2)
      (.fill))))
