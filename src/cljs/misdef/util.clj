(ns misdef.util)

(defmacro with-tx [ctx & body]
  `(doto ~ctx
     (.save)
     ~@body
     (.restore)))
