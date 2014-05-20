(ns misdef.game.foe
  (:require [misdef.game.missile :refer [launch-attack-missile]] 
            [misdef.util :refer [random]]))

(defn- foe-missile? [{:keys [type affiliation]}]
  (and (= type :missile)
       (= affiliation :foe)))

(defn update [{:keys [objects] :as g}]
  (let [missile-count (count (filter foe-missile? (vals objects)))
        shoot?        (and (< missile-count 6)
                           (< (random) 0.01))]
     (if shoot?
       (launch-attack-missile g)
       g)))
