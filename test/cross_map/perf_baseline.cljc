(ns cross-map.perf-baseline
  "Some general performance tests to compare others against."
  (:require [criterium.core :as cri :refer [bench benchmark]]
            [cross-map.util :as u :refer [new-uuid]]
            [cross-map.core :as cmc :refer [cross-map cross-cols cols rows]]
            #?(:clj [clojure.test :as t]
                :cljs [cljs.test :as t :include-macros true])))


(defn setnil
  [coll k]
  (assoc coll k nil))

(defn setnil!
  [coll k]
  (assoc! coll k nil))

(defn assock
  [coll k]
  (assoc coll k k))

(defn assock!
  [coll k]
  (assoc! coll k k))

(defn reassoc
  "Tests the efficiency of collections by constantly
  associating and dissociating keys of the coll.

  Add-fn and rem-fn are functions that take a coll and
  a key, and add or remove that key from the coll."
  [coll add-fn rem-fn times key-seq]
  (loop [c coll
         i 0
         [k & ks] key-seq]
    (if (>= i times)
      coll
      (-> coll
          (rem-fn k)
          (add-fn k)
          (recur (inc i) ks)))))
(defn func-map
  [n]
  {:vec   [(vec (range n)) assock setnil]
   :t-vec [(transient (vec (range n))) assock! setnil!]
   :map   [(reduce assock {} (range n)) assock dissoc]
   :t-map [(transient (reduce assock {} (range n))) assock! dissoc!]
   :set   [(reduce conj #{} (range n)) conj disj]
   :t-set [(transient (reduce conj #{} (range n))) conj! disj!]
   :s-set [(reduce conj (sorted-set) (range n)) conj disj]})

(defn run-bench
  [n coll-type & {:as extras}]
  (let [fm (merge (func-map n) extras)
        [coll add-fn rem-fn :as fns] (fm coll-type)
        _ (if-not fns (println "Invalid option: " coll-type))

        nums (vec (repeatedly n #(rand-int n)))]
    (benchmark (reassoc coll add-fn rem-fn n nums) {})))

(defonce results (atom nil))

(defn run-em-all
  ([n] (apply run-em-all n (keys func-map)))
  ([n & types]
   (reset! results
           (reduce (fn [acc t]
                     (assoc acc t (dissoc (run-bench n t) :results)))
                   {}
                   types))
   nil))


;; Testing higher-order preds vs tail-call vs while loops

(def default-probabilities
  {:A 0.9
   :B 0.9
   :C 0.9})

(defonce sets
  (atom nil))

(defn make-sets
  "Given a map of keys-probabilities,
  create a vector of n sets that randomly
  choose whether to include each key based
  on its probability."
  [n ps]
  (mapv (fn [m]
          (reduce-kv (fn [acc k p]
                       (if (< (rand) p)
                         (conj acc k)
                         acc))
                     #{}
                     ps))
        (range n)))

(defn gen-default-set
  [n]
  (reset! sets
          (make-sets
           n
           default-probabilities))
  nil)

;;; The following funcions do a simple operation.
;;; They sum up the number of sets that have all keys.
(defn sum-full-maps-preds
  [ks sets]
  (transduce (comp (filter #(every? % ks))
                   (map (constantly 1)))
             +
             0
             sets))

(defn sum-full-maps-tail
  [ks sets]
  (let [cs (count sets)
        ck (count ks)]
    (loop [i 0
           acc 0]
      (if-not (< i cs)
        acc
        (let [s (nth sets i)]
          (recur (inc i)
                 (+ acc
                    (loop [j 0
                           bl 1]
                      (cond
                        (>= j ck) bl
                        (contains? s (nth ks j)) (recur (inc j) bl)
                        :else 0)))))))))

(defn sum-full-maps-while
  [ks sets]
  (let [cs (count sets)
        ck (count ks)
        i (volatile! 0)
        sm (volatile! 0)]
    (while (< @i cs)
      (let [s (nth sets @i)
            j (volatile! 0)
            bl (volatile! true)]
        (while (and @bl (< @j ck))
          (when-not (contains? s (nth ks @j))
            (vreset! bl false))
          (vreset! j (inc @j)))
        (when @bl (vreset! sm (inc @sm))))
      (vreset! i (inc @i)))
    @sm))


;;; V+ Tests
;;; Implementing floating-point vectors as:
;;; structs, deftype, vector
(defrecord v2r
    [^Double x ^Double y])

(defn rv+
  [^v2r v1 ^v2r v2]
  (v2r. (+ (.x v1) (.x v2))
        (+ (.y v1) (.y v2))))

(deftype v2t
    [^Double x ^Double y])

(defn tv+
  [^v2t v1 ^v2t v2]
  (v2t. (+ (.x v1) (.x v2))
        (+ (.y v1) (.y v2))))

(defn vv+
  [v1 v2]
  (mapv + v1 v2))


(defn testvecs
  "Tests the vector add operations by reducing a
  random list by each of the v+ operations on
  identical sets of random vectors"
  [n]
  (let [nums (repeatedly (* 2 n) rand)
        vpairs (partition 2 nums)
        v2r-list (apply list (map (fn [[x y]] (v2r. x y)) vpairs))
        v2t-list (apply list (map (fn [[x y]] (v2t. x y)) vpairs))
        v2v-list (apply list (map vec vpairs))]
    (println "V2 Record:")
    (bench (reduce rv+ v2r-list))
    (println "V2 Type:")
    (bench (reduce tv+ v2t-list))
    (println "V2 Vector:")
    (bench (reduce vv+ v2v-list))
    (println "Just Doubles:")
    (bench (reduce + nums))))


;;; Testing reassoc on cross-maps
;;; Test columns will be the smaller set, as is usual
(def test-cols-probs
  "The test columns for the cross-map reassoc tests.
  The numbers represent the likelihood of a row having
  this column."
  {:A 1.0 :B 1.0 :C 0.9
   :D 0.5 :E 0.25 :F 0.25 :G 0.1})

(defn new-row
  [idx ps]
  (transduce (filter (fn [[k v]] (< (rand) v)))
             (fn
               ([acc] acc)
               ([acc [k v]]
                (assoc acc [idx k] (keyword (str k "_" idx)))))
             {}
             ps))

(defn gen-test-map
  "For the test rows, the row ID is a uuid."
  ([n] (gen-test-map n test-cols-probs))
  ([n ps]
   (->> (range n)
        (mapcat #(new-row % ps))
        (into (cross-map)))))

(defn cross-reassoc-tests
  "It crosses the cols on all adjacent pairs of elements and
  reassociates thir kvp's into the cross map"
  ([n] (cross-reassoc-tests n test-cols-probs))
  ([n ps]
   (let [parts (vec (partition 2 1 (keys ps)))
         cmap (gen-test-map n ps)]
     ;; Normal
     (println "Persistent: ")
     (bench
      (reduce (fn [acc [c1 c2]]
                (reduce (fn [acc' [idx r]]
                          (-> acc'
                              (dissoc [idx c1])
                              (assoc [idx c1] (r c1))
                              (dissoc [idx c2])
                              (assoc [idx c2] (r c2))))
                        acc
                        (cross-cols acc [c1 c2])))
              cmap
              parts))
     ;; Transient
     (println "Transient: ")
     (bench
      (reduce (fn [acc [c1 c2]]
                (persistent!
                 (reduce (fn [acc' [idx r]]
                           (-> acc'
                               (dissoc! [idx c1])
                               (assoc! [idx c1] (r c1))
                               (dissoc! [idx c2])
                               (assoc! [idx c2] (r c2))))
                         (transient acc)
                         (cross-cols acc [c1 c2]))))
              cmap
              parts)))))

(defn flip-flop-test
  "Testing how quickly a persistent cross-map can
  flip-flop between persistent and transient."
  ([num-flips map-size] (flip-flop-test num-flips map-size test-cols-probs))
  ([num-flips map-size ps]
   (let [cm (gen-test-map map-size ps)
         plain-map (into {} cm)]
     (println "Plain maps:")
     (bench (dotimes [i num-flips]
              (persistent! (transient plain-map))))
     (println "Cross maps:")
     (bench (dotimes [i num-flips]
              (persistent! (transient cm)))))))
