(defproject misdef "0.1.0-SNAPSHOT"
  :description "Missile Defence game"
  :url "http://github.com/jarppe/misdef"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :clojurescript? true
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2202" :scope "provided"]
                 [figwheel "0.1.2-SNAPSHOT" :scope "provided"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.2-SNAPSHOT"]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/misdef.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :optimizations :none
                                   :pretty-print true
                                   :source-map true
                                   :notify-command ["growlnotify" "-n" "cljsbuild" "-m"]}}]}
  :figwheel {:http-server-root "public"
             :port 8080}
  :uberjar-name "misdef.jar"
  :min-lein-version "2.0.0")
