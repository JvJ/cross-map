(ns cross-map.core-test
  (:require [clojure.test :refer :all]
            [cross-map.core :refer :all]))


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
