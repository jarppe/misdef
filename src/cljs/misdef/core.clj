(ns misdef.core)

(defmacro on-event [element-id event-name & body]
  `(let [dom-element# (if (string? ~element-id)
                        (.getElementById js/document ~element-id)
                        ~element-id)]
     (if-not dom-element# (throw (str "Can't find DOM element by ID \"" ~element-id "\"")))
     (.addEventListener
       dom-element#
       ~event-name
       (fn [~'e]
         (.preventDefault ~'e)
         (.stopPropagation ~'e)
         ~@body
         nil)
       false)))
