(ns cross-map.perf-baseline
  "Some general performance tests to compare others against."
  (:require [criterium.core :as cri :refer [bench benchmark]]
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
