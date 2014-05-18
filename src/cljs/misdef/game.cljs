(ns misdef.game)

;;
;; Game state:
;;

(defonce game (atom nil))

;;
;; Game update:
;;

(defmulti update-object (fn [_ object] (:type object)))

(defmethod update-object :default [_ _])

;;
;; Rendering:
;;

(defmulti render-object (fn [_ object] (:type object)))

(defmethod render-object :default [_ _])
