(ns cross-map.core-test
  (:require [clojure.test :refer :all]
            [cross-map.core :refer :all]
            [cross-map.util :refer :all]
            [criterium.core :as cri :refer [bench]]))

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
  [row-seq col-seq num-rows num-cols fraction-keep]
  (->> (for [row (take num-rows row-seq)
             col (take num-cols col-seq)]
         [[row col] (keyword (str row "_" col))])
       (shuffle)
       (take (int (* (- 1 fraction-keep)
                     (* num-rows num-cols))))))

(defn proxy-cross
  "An inefficient filtering operation that will produce
  the same output as other cross operations.
  Key mode can either be :keys, :vals, :entries, :row-maps,
  or :col-maps."
  [cmap row-keys col-keys every-row? every-col?
   key-mode]
  (let [map-mode (case key-mode
                   :row-maps :row
                   :col-maps :col
                   nil)

        key-mode (if map-mode
                   :keys
                   key-mode)

        row-check (if every-col? every? some)
        col-check (if every-row? every? some)
        
        iter-rows (cond
                    (and every-row?
                         (empty? row-keys)) (->> cmap
                                                 keys
                                                 (filter pair?)
                                                 (map first)
                                                 set)
                    :else row-keys)
        
        iter-cols (cond
                    (and every-col?
                         (empty? col-keys)) (->> cmap
                                                 keys
                                                 (filter pair?)
                                                 (map second)
                                                 set)
                    :else col-keys)

        ret (for [r iter-rows
                  ;; LEFTOFF: Double-crossing ain't working right!!
                  :when (row-check #(contains? cmap [r %]) col-keys)
                  c iter-cols
                  :when (and (col-check #(contains? cmap [% c]) row-keys)
                             (cmap [r c]))]
              (case key-mode
                    :keys [r c]
                    :vals (cmap [r c])
                    :entries (find cmap [r c])))]
    (case map-mode
      :row (->> ret
                (group-by first)
                (map (fn [[r pairs]]
                       [r (reduce (fn [acc [r c]]
                                    (assoc acc c (cmap [r c])))
                                  {}
                                  pairs)])))
      :col (->> ret
                (group-by second)
                (map (fn [[c pairs]]
                       [c (reduce (fn [acc [r c]]
                                    (assoc acc r (cmap [r c])))
                                  {}
                                  pairs)])))
      ret)))

(deftest cross-test
  ;; We're doing a *big* test here.  Assuming
  ;; 10,000 rows with 100 cols.(TODO: This is more for benchmarking)
  
  ;; We'll construct it such that the first 10 cols
  ;; have a high chance of being present, and
  ;; the remaining 90 have a low chance of that.
  ;; This will best simulate the ECS use case that
  ;; cross-maps were designed for.
  (let [syms (repeatedly gensym)
        init-syms (take 2 syms)
        rest-syms (take 8 (drop 2 syms))
        idxs (range 15)
        ;; First 10 columns have a 90% chance of being present
        init-pairs (gen-grid-pairs idxs init-syms 15 2 9/10)
        ;; Next 90 columns have a 10% chance of being present
        rest-pairs (gen-grid-pairs idxs rest-syms 15 10 1/10)

        all-pairs (concat init-pairs rest-pairs)

        ;; The cross map will be tested against a normal map
        test-cmap (into (cross-map) all-pairs)
        test-map (into {} all-pairs)

        opts (for [any-row   [nil :any-row]
                   every-row [nil :every-row]
                   any-col   [nil :any-col]
                   every-col [nil :every-col]
                   keys-only [nil :keys-only]
                   vals-only [nil :vals-only]
                   by-rows   [nil :by-rows]
                   by-cols   [nil :by-cols]
                   r-keys?   [false true]
                   c-keys?   [false true]]
               [any-row every-row any-col every-col
                keys-only vals-only by-rows by-cols
                r-keys? c-keys?])
        
        ;; Iterate through all combinations of options
        ;; LEFTOFF: Maybe this isn't the best testing strategy?
        tests (for [[any-row   
                     every-row 
                     any-col   
                     every-col 
                     keys-only 
                     vals-only 
                     by-rows   
                     by-cols   
                     r-keys?   
                     c-keys?] opts  
                    :let [;; Random keys
                          r-keys (if r-keys?
                                   (take 3 (shuffle idxs))
                                   ())
                          c-keys (if c-keys?
                                   (concat (take 3 (shuffle init-syms))
                                           (take 3 (shuffle rest-syms)))
                                   ())

                          every-row? (boolean every-row)
                          every-col? (boolean every-col)
                          key-mode (or (and keys-only :keys)
                                       (and vals-only :vals))
                          args (set
                                (filter boolean
                                        [any-row every-row any-col every-col
                                         keys-only vals-only by-rows by-cols]))


                          op   #(set (apply cross test-cmap r-keys c-keys %))
                          r-op #(set (apply cross-rows test-cmap r-keys %))
                          c-op #(set (apply cross-cols test-cmap c-keys %))
                          proxy-op   #(set (proxy-cross test-map r-keys c-keys
                                                  every-row? every-col?
                                                  (or key-mode :entries)))
                          proxy-r-op #(set (proxy-cross test-map r-keys ()
                                                  every-row? true
                                                  (or key-mode :col-maps)))
                          proxy-c-op #(set (proxy-cross test-map () c-keys
                                                  true every-col?
                                                  (or key-mode :row-maps)))]]
                (cond
                  ;; These options cannot co-exist
                  (or (and any-row every-row)
                      (and any-col every-col)
                      (and keys-only vals-only)
                      (and by-rows by-cols))
                  (do (is (thrown? Exception (op args)))
                      ;; Row and col options can't go together
                      (and (or any-row every-row)
                           (or any-col every-col)
                           (is (thrown? Exception (r-op args)))
                           (is (thrown? Exception (c-op args)))))

                  :else 
                  (do (is (= (proxy-op) (op args)))
                      (is (= (proxy-r-op) (r-op args)))
                      (is (= (proxy-c-op) (c-op args))))))]
    (doseq [t (take 10 tests)]
      nil)
    (println (take 10 opts))))

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
