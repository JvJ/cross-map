(ns cross-map.core-test
  (:require [clojure.test :refer :all]
            [cross-map.core :refer :all]
            [cross-map.util :refer :all]
            [criterium.core :as cri :refer [bench]]))

(def ^:dynamic *random* false)
(def ^:dynamic *benchmarking* false)

(defmacro with-random
  [& body]
  `(binding [*random* true]
     ~@body)) 

(defmacro with-benchmarking
  [& body]
  `(binding [*benchmarking* true]
     ~@body))

;;;; A test-map that we can use for cross-referencing!
(def alphabet (map char (range (int \a) (inc (int \z)))))

(def test-rows (range 10))
(def test-cols (map (comp keyword str) (take 10 alphabet)))
(def num-removed 5)

(defn make-test-entry
  [i l]
  (keyword (str (name l) "_" i)))

(let [test-pairs (for [i test-rows
                       l test-cols]
                   [[i l] (make-test-entry i l)])]

  (def test-cmap (into (cross-map) test-pairs))
  (def test-map (into {} test-pairs)))

(deftest
  ^{:doc "Testing cross operations according to constant values.
See tasks.org for the source tables."}
  const-cross-test
  ;; LEFTOFF: Replace nil with expected value of operations
  (let [removals (list [2,:c],[2,:e],[4,:f],[6,:b],[7,:c],[7,:f])
        test-cmap (apply disj test-cmap removals)
        row-keys [3,4,5,6]
        col-keys [:c,:d,:e]]
    (do
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys))
             (set (cross-rows test-cmap row-keys :every-row))
             nil))
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row))
             nil))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys :keys-only))
             (set (cross-rows test-cmap row-keys :every-row :keys-only))
             nil))
      ;; Keys-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :keys-only))
             nil))

      ;; Vals-only
      ;; Default/every-row
      (is (= (set (cross-rows test-cmap row-keys :vals-only))
             (set (cross-rows test-cmap row-keys :every-row :vals-only))
             nil))
      ;; Vals-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :vals-only))
             nil)))
    ;; Cross-cols
    (do
      ;; Default/every col
      (is (= (set (cross-cols test-cmap col-keys))
             (set (cross-cols test-cmap col-keys :every-col))
             nil))
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col))
             nil))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-cols test-cmap col-keys :keys-only))
             (set (cross-cols test-cmap col-keys :every-col :keys-only))))
      ;; Keys-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :keys-only))))

      ;; Vals-only
      ;; Default/every-col
      (is (= (set (cross-cols test-cmap col-keys :vals-only))
             (set (cross-cols test-cmap col-keys :every-col :vals-only))
             nil))
      ;; Vals-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :vals-only))
             nil)))
    ;; Full Cross
    (do
      ;; Default/every-row & every-col
      (is (= (set (cross test-cmap row-keys col-keys))
             (set (cross test-cmap row-keys col-keys :every-row :every-col))
             nil)))))

(deftest
  ^{:doc "Test the cross map according to a cross-map with random dissociations."}
  rand-cross-test
  (let [;; Randomly dissociate a few entries
        removals (take num-removed (shuffle (keys test-cmap)))
        test-cmap (apply dissoc test-cmap removals)
        row-keys (take 2 (shuffle test-rows))
        col-keys (take 2 (shuffle test-cols))]

    (with-test-out
      (println "test-cmap: " test-cmap))
    
    ;; Cross-rows
    (do
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys))
             (set (cross-rows test-cmap row-keys :every-row))
             (set (->> test-cmap
                       cols
                       (filter #(every? (second %) row-keys))))))
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row))
             (set (->> test-cmap
                       cols
                       (filter #(some (second %) row-keys))))))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys :keys-only))
             (set (cross-rows test-cmap row-keys :every-row :keys-only))
             (set (->> test-cmap
                       cols
                       (filter #(every? (second %) row-keys))
                       (map key)))))
      ;; Keys-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :keys-only))
             (set (->> test-cmap
                       cols 
                       (filter #(some (second %) row-keys))
                       (map key)))))

      ;; Vals-only
      ;; Default/every-row
      (is (= (set (cross-rows test-cmap row-keys :vals-only))
             (set (cross-rows test-cmap row-keys :every-row :vals-only))
             (set (->> test-cmap
                       cols
                       (filter #(every? (second %) row-keys))
                       (map val)))))
      ;; Vals-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :vals-only))
             (set (->> test-cmap
                       cols 
                       (filter #(some (second %) row-keys))
                       (map val))))))
    ;; Cross-cols
    (do
      ;; Default/every col
      (is (= (set (cross-cols test-cmap col-keys))
             (set (cross-cols test-cmap col-keys :every-col))
             (set (->> test-cmap
                       rows
                       (filter #(every? (second %) col-keys))))))
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col))
             (set (->> test-cmap
                       rows
                       (filter #(some (second %) col-keys))))))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-cols test-cmap col-keys :keys-only))
             (set (cross-cols test-cmap col-keys :every-col :keys-only))
             (set (->> test-cmap
                       rows
                       (filter #(every? (second %) col-keys))
                       (map key)))))
      ;; Keys-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :keys-only))
             (set (->> test-cmap
                       rows 
                       (filter #(some (second %) col-keys))
                       (map key)))))

      ;; Vals-only
      ;; Default/every-col
      (is (= (set (cross-cols test-cmap col-keys :vals-only))
             (set (cross-cols test-cmap col-keys :every-col :vals-only))
             (set (->> test-cmap
                       rows
                       (filter #(every? (second %) col-keys))
                       (map val)))))
      ;; Vals-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :vals-only))
             (set (->> test-cmap
                       rows 
                       (filter #(some (second %) col-keys))
                       (map val)))))
      (report {:type String :value "HEY GUYS!"}))
    ;; Full Cross
    (do
      ;; Default/every-row & every-col
      (is (= (set (cross test-cmap row-keys col-keys))
             (set (cross test-cmap row-keys col-keys :every-row :every-col))
             (set (let [x
                        (->> test-cmap
                             (group-by (comp first key) ,,,)
                             (eduction (comp (map (fn [[k v]]
                                                    (kvp k (transduce
                                                            (comp (map (fn [[[r c] vv]] (kvp c vv)))
                                                                  (filter #(some ($ = (key %)) col-keys)))
                                                            conj {} v))))
                                             (filter #(every? (val %) col-keys))
                                             (mapcat (fn [[r v]] (map (fn [[c vv]] (kvp [r c] vv)) v)))) ,,,)
                             ;; TODO: Just use thread instead of eduction
                             ;; It will be slower, but that's kind of the point of
                             ;; this test code.
                             (group-by (comp second key) ,,,)
                             (eduction (comp (map (fn [[k v]]
                                                    (kvp k (transduce
                                                            (comp (map (fn [[[r c] vv]] (kvp r vv)))
                                                                  (filter #(some ($ = (key %)) row-keys)))
                                                            conj {} v))))
                                             (filter #(every? (val %) row-keys)) 
                                             (mapcat (fn [[c v]] (map (fn [[r vv]] (kvp [r c] vv)) v)))) ,,,))
                        _ (println "row-keys: " row-keys)
                        _ (println "col-keys: " col-keys)
                        _ (println "res: " (set x))]
                    x))))))

  ;; Cross-cols
  )

;;; Confirm that cross-maps support all map operations
(deftest map-test)

;;; Confirm that additions/removals are properly reflected
;;; In the row/col indices, and that "rows" and "cols" match
;;; metadata counterparts
(deftest indexing-test)

(defn test-ns-hook
  []
  
  (rand-cross-test))
