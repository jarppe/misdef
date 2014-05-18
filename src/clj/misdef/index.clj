(ns misdef.index
  (:require [hiccup.page :refer [html5 include-js]]
            [garden.core :refer [css]]
            [misdef.env :refer [dev?]]))

(defn index-content []
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:title "Missile Defence"]
          [:style {:type "text/css"}
           (css {:pretty-print? dev?}
                [:html :body :canvas {:width             "100%"
                                      :height            "100%"
                                      :color             "#CCC"
                                      :background-color  "#333"
                                      :padding           "0px"
                                      :margin            "0px"}]
                [:body {:display "table"}]
                [:#c {:display "none"}]
                [:#loading {:font-size         "48px"
                            :font-family       "sans-serif"
                            :text-align        "center"
                            :display           "table-cell"
                            :vertical-align    "middle" }])]]
         [:body
          [:div#loading "Loading..."]
          [:canvas#c]]
         (include-js "js/misdef.js")))
