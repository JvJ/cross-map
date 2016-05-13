(defproject com.jvj/cross-map "0.1.0-SNAPSHOT"
  :description "Two-dimensional map with efficient cross-section capabilities."
  :url "https://github.com/JvJ/cross-map"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]]

  :source-paths ["src"]

  :plugins [[lein-environ "1.0.0"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  
  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.2"]
                             [lein-figwheel "0.5.0-4"]
                             [lein-environ "1.0.0"]]
                   :dependencies [[reloaded.repl "0.2.1"]
                                  [criterium "0.4.4"]
                                  [lein-figwheel "0.5.0-4"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [compojure "1.4.0"]
                                  [com.stuartsierra/component "0.3.1"]
                                  [acyclic/squiggly-clojure "0.1.4"]
                                  [net.mikera/core.matrix "0.52.0"]]
                   
                   :source-paths ["src" "dev"]

                   :figwheel {:nrepl-port 7888
                              :css-dirs ["resources/public/css"]}
                   
                   :cljsbuild {:builds [{:source-paths ["src" "dev"]
                                         :figwheel true
                                         :compiler {:output-to "resources/public/js/compiled/cross_map.js"
                                                    :output-dir "resources/public/js/compiled/out"
                                                    :optimizations :none
                                                    :recompile-dependents true
                                                    :source-map-timestamp true}}
                                        {:id "min"
                                         :source-paths ["src"]
                                         :compiler {:output-to "resources/public/js/compiled/cross_map.js"
                                                    :main schoolsofsorcery.core
                                                    :optimizations :advanced
                                                    :pretty-print false}}]}
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   ;; Type dependencies
                   :env {:squiggly {:checkers [:eastwood]
                                    :eastwood-exclude-linters [:unlimited-use]}}}})
