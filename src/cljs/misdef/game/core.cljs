(ns misdef.core)

;;
;; Game state:
;;

(defonce game (atom nil))

;;
;; Game update:
;;

;;
;; Rendering:
;;

(defmulti render-object (fn [_ object] (:type object)))
