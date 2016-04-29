(ns cross-map.core-test
  (:require [clojure.test :refer :all]
            [cross-map.core :refer :all]
            [criterium.core :refer [bench]]))

(def ^:dynamic *benchmarking* false)

(defmacro with-benchmarking
  [& body]
  `(binding [*benchmarking* true]
     ~@body))

;;;; A test-map that we can use for cross-referencing!
(def alphabet (map char (range (int \a) (inc (int \z)))))

(defn gen-grid-pairs
  "Generate a sequence of grid entries of num-rows by num-cols,
  and randomly select num-keep of then.

  Keep in mind that num-cols won't go to more than 26, since
  the columns are labelled by the letters of the alphabet."
  [row-seq col-seq num-rows num-cols num-keep]
  (->> (for [row (take num-rows row-seq)
             col (take num-cols col-seq)]
         [[row col] (keyword (str row "_" col))])
       (shuffle)
       (take num-keep)))

(deftest cross-test
  ;; LEFTOFF: Develop extensive tests!!
  (let [;; We're doing a *big* test here.  Assuming
        ;; 10,000 rows with 100 cols
        syms (repeatedly gensym)
        test-syms (take 10 syms)
        _ (println "startin out with test-sims: " test-syms)
        pairs (gen-grid-pairs (range) syms 10000 100 300000)
        _ (println "bein cool")
        cmap (into (cross-map) pairs)
        _ (println "map is here with " (count cmap) " elements")
        col-cross (cross-cols cmap (take 2 test-syms))]
    (println "Cross count: " (count col-cross))))

(defn volatile-test
  "Just checking that running pmap on lazy
  sequences that use volatile variables."
  [num-syms bench?]
  (letfn [(idx-zip [coll]
            (let [i (volatile! -1)]
              (for [c coll]
                [c (vswap! i inc)])))
          
          (dummy-func [[sym n]]
            (let [[_ s] (re-find #"G__(\d)+" (name sym))
                  m (read-string s)]
              (reduce + (range (* n m)))))]
    (let [syms (repeatedly num-syms gensym)
          sym-idx (idx-zip syms)
          p-sums (pmap dummy-func sym-idx)
          sums (map dummy-func sym-idx)
          ]
      (when bench?
        (bench (reduce conj [] p-sums))
        (bench (reduce conj [] sums)))
      (is (= p-sums
             sums)))))
