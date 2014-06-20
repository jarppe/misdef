(defproject misdef "0.1.0-SNAPSHOT"
  :description "Missile Defence game"
  :url "http://github.com/jarppe/misdef"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202" :scope "provided"]
                 [http-kit "2.1.16"]
                 [ring/ring-core "1.2.2"]
                 [ring/ring-devel "1.2.2"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [metosin/ring-http-response "0.4.0"]
                 [garden "1.1.6" :scope "provided"]
                 [prismatic/dommy "0.1.2" :scope "provided"]]
  :source-paths ["src/clj" "src/cljs"]
  :profiles {:dev {:dependencies [[midje "1.6.2"]]
                   :plugins [[lein-cljsbuild "1.0.3"]]
                   :cljsbuild {:builds {:client {:notify-command ["growlnotify" "-n" "cljsbuild" "-m"]}}}}
             :uberjar {:source-paths ["src/clj" "src/main"]
                       :main misdef.main
                       :aot [misdef.main]
                       :cljsbuild {:builds {:client {:compiler {:optimizations :advanced
                                                                :elide-asserts true
                                                                :pretty-print false}}}}}}
  :cljsbuild {:builds {:client {:source-paths ["src/cljs"]
                                :compiler {:output-to "resources/public/misdef.js"
                                           :output-dir "target/js/out"
                                           :optimizations :whitespace
                                           :pretty-print true}}}}
  :repl-options {:init-ns misdef.server}
  :uberjar-name "misdef.jar"
  :min-lein-version "2.3.4")
