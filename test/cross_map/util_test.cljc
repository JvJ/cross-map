(ns cross-map.util-test
  (:require [cross-map.util :as u
             :refer [mintersection munion]]
            [clojure.set :as cst
             :refer [union intersection]]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true])))

(def ^:dynamic *map-max-size* 100)
(def ^:dynamic *map-min-size* 10)
(def ^:dynamic *num-maps* 100)

(t/deftest map-ops-test
  (let [nums (vec (range *map-size*))
        sets (mapv (fn [_]
                    (->> nums
                         (shuffle)
                         (take (+ *map-min-size*
                                  (rand-int (- *map-max-size*
                                               *map-min-size*))))
                         (set)))
                   (range *num-maps*))
        maps (mapv (fn [s] (reduce #(assoc %1 %2 %2) {} s)) sets)

        set-parts1 (partition-all 1 sets)
        map-parts1 (partition-all 1 maps)
        set-parts2 (partition-all 2 sets)
        map-parts2 (partition-all 2 maps)
        set-parts3 (partition-all 3 sets)
        map-parts3 (partition-all 3 maps)]

    ;;; munion
    (t/is (= (map #(apply union %) set-parts1)
             (map #(set (keys (apply munion %))) map-parts1)))

    (t/is (= (map #(apply union %) set-parts2)
             (map #(set (keys (apply munion %))) map-parts2)))

    (t/is (= (map #(apply union %) set-parts3)
             (map #(set (keys (apply munion %))) map-parts3)))

    (t/is (= (apply union sets)
             (set (keys (apply munion maps)))))

    ;;; mintersection
    (t/is (= (map #(apply intersection %) set-parts1)
             (map #(set (keys (apply mintersection %))) map-parts1)))

    (t/is (= (map #(apply intersection %) set-parts2)
             (map #(set (keys (apply mintersection %))) map-parts2)))

    (t/is (= (map #(apply intersection %) set-parts3)
             (map #(set (keys (apply mintersection %))) map-parts3)))

    (t/is (= (apply intersection sets)
             (set (keys (apply mintersection maps)))))))

(defn test-ns-hook
  []
  (map-ops-test))
