(ns misdef.core)

(defmacro defevt [handler-name & body]
  `(defn ~handler-name [~'e]
     (.preventDefault ~'e)
     (.stopPropagation ~'e)
     ~@body
     nil))
