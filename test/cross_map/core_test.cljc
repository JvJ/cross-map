(ns cross-map.core-test
  (:require [clojure.test :refer :all]
            [cross-map.core :refer :all]
            [criterium.core :refer [bench]]))


;;;; A test-map that we can use for cross-referencing!
(def alphabet (map char (range (int \a) (inc (int \z)))))

(defn gen-grid-pairs
  "Generate a sequence of grid entries of num-rows by num-cols,
  and randomly select num-keep of then.

  Keep in mind that num-cols won't go to more than 26, since
  the columns are labelled by the letters of the alphabet."
  [num-rows num-cols num-keep]
  (->> (for [row (range num-rows)
             col (->> alphabet
                      (map str)
                      (take num-cols))]
         [[row (keyword col)] (keyword (str row "_" col))])
       (shuffle)
       (take num-keep)))

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
