(ns misdef.main
  (:gen-class))

(defn -main [& args]
  (require 'misdef.server)
  (apply (resolve 'misdef.server/run) args))
