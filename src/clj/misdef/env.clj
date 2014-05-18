(ns misdef.env)

(def dev?  (nil? (System/getenv "DYNO")))
(def prod? (not dev?))
