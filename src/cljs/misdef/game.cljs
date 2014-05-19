(ns misdef.game)

;;
;; Game state:
;;

(defonce game (atom nil))

;;
;; Object ID:
;;

(let [id (atom 0)]
  (defn next-object-id []
    (swap! id inc)))

;;
;; Game update:
;;

(defmulti update-object (fn [_ object] (:type object)))
(defmethod update-object :default [g _]
  g)

;;
;; Rendering:
;;

(defmulti render-object (fn [_ object] (:type object)))
(defmethod render-object :default [_ _])
