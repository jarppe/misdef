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

(defn explosion [{:keys [score] :as g} x y affiliation ts]
  (let [id (game/next-object-id)]
    (-> g
        (assoc-in [:objects id] {:id           id
                                :type         :explosion    
                                :affiliation  affiliation
                                :created      ts
                                :x            x
                                :y            y
                                :age          0
                                :r            0})
        (assoc :score (- score 2000)))))

(defn defence-explosion? [{:keys [type affiliation]}]
  (and (= type :explosion) (= affiliation :friend)))

(defn inside? [px py {:keys [x y r]}]
  (let [dx   (- px x)
        dy   (- py y)
        dist (Math/sqrt (+ (* dx dx) (* dy dy)))]
    (< dist r)))

(defn inside-defence-explosion? [g x y]
  (some (partial inside? x y) (filter defence-explosion? (-> g :objects vals))))

(defmethod game/update-object :explosion [{:keys [ts] :as g} {:keys [id created] :as o}]
  (let [age (- ts created)
        r   (* explosion-velocity age)]
    (if (> age explosion-die)
      (update-in g [:objects] dissoc id)
      (update-in g [:objects id] assoc :age age :r r))))

(defn explosion-alpha [age]
  (let [n (if (< age explosion-age)
            0.4
            (- 0.4 (* 0.4 (/ (- age explosion-age) explosion-glow))))]
    (if (pos? n) n 0)))

(defmethod game/render-object :explosion [{:keys [ctx ts]} {:keys [x y r age affiliation]}]
  (doto ctx
    (aset "strokeStyle" (apply util/rgb->color (explosion-color affiliation)))
    (aset "fillStyle" (util/color-with-alpha (explosion-color affiliation) (explosion-alpha age)))
    (.beginPath)
    (.arc x y r 0 util/pi2)
    (.fill)
    (.stroke)))
