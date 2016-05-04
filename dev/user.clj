(ns user
  (:require [figwheel-sidecar.system :as sys]
            [com.stuartsierra.component :as component]))

(def system
  (component/system-map
   :figwheel-system (sys/figwheel-system (sys/fetch-config))))

(defn start-sys []
  (alter-var-root #'system component/start))

(defn start-fig []
  (start-sys)
  (sys/cljs-repl (:figwheel-system system)))
