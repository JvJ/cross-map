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
(def num-removed 10)

(defn make-test-entry
  [i l]
  (keyword (str (name l) "_" i)))

(let [test-pairs (for [i test-rows
                       l test-cols]
                   [[i l] (make-test-entry i l)])]

  (def test-cmap (into (cross-map) test-pairs))
  (def test-map (into {} test-pairs)))

(defn- cross-test-helper
  "Helper function that should produce the same output
  as cross."
  [cmap row-keys col-keys every-row? every-col?]
  (let [[every-row? row-keys] (if (and every-row? (empty? row-keys))
                                [false (set (map first (keys cmap)))]
                                [every-row? row-keys])
        [every-col? col-keys] (if (and every-col? (empty? col-keys))
                                [false (set (map second (keys cmap)))]
                                [every-col? col-keys])
        valid-row? (if every-row? every? some)
        valid-col? (if every-col? every? some)]
    (->> cmap
         (group-by (comp first key) ,,,)
         (map (fn [[k v]]
                [k (transduce
                    (comp (map (fn [[[r c] vv]] [c vv]))
                          (filter #(some ($ = (ky %)) col-keys)))
                    conj {} v)]) ,,,)
         (filter #(valid-col? (vl %) col-keys) ,,,)
         (mapcat (fn [[r v]] (map (fn [[c vv]] [[r c] vv]) v)) ,,,)
         (group-by (comp second ky) ,,,)
         (map (fn [[k v]]
                [k (transduce
                    (comp (map (fn [[[r c] vv]] [r vv]))
                          (filter #(some ($ = (ky %)) row-keys)))
                    conj {} v)]) ,,,)
         (filter #(valid-col? (vl %) row-keys) ,,,) 
         (mapcat (fn [[c v]] (map (fn [[r vv]] [[r c] vv]) v)) ,,,)
         (set ,,,))))

(defn- contiguous-by?
  "Maps f to every element of the column, and determines if all
  subsequences where f produces the same result have contiguous indices.
  
  For example:
  (contiguous-by first [[1,:a],[1,:b],[2,:a],[2,:b]]) => true
  (contiguous-by second [[1,:a],[1,:b],[2,:a],[2,:b]]) => false

  In the above example, the first elements are: [1,1,2,2] and
  the second elements are [:a,:b,:a,:b].  Matching elements are
  not contiguous in the second one."
  [f coll]
  (->> coll
       (partition-by f)
       (map (<| f first))
       (frequencies)
       (every? #(= 1 (val %)))))

(deftest
  ^{:doc "Testing cross operations according to constant values.
See tasks.org for the source tables."}
  const-cross-test
  (let [removals (list [2,:c],[2,:d],[2,:e],[4,:f],[6,:b],[7,:c],
                       [7,:f],[3,:j],[4,:j],[5,:j],[6,:j])
        test-cmap (apply dissoc test-cmap removals)
        row-keys [3,4,5,6]
        col-keys [:c,:d,:e]]
    (do
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys))
             (set (cross-rows test-cmap row-keys :every-row))
             ;; We expect entire columns that match, even if some keys
             ;; are not asked for
             (set #{[:a {0 :a_0, 1 :a_1, 2 :a_2, 3 :a_3, 4 :a_4, 5 :a_5, 6 :a_6, 7 :a_7, 8 :a_8, 9 :a_9}],
                    [:c {0 :c_0, 1 :c_1, 3 :c_3, 4 :c_4, 5 :c_5, 6 :c_6, 8 :c_8, 9 :c_9}],
                    [:d {0 :d_0, 1 :d_1, 3 :d_3, 4 :d_4, 5 :d_5, 6 :d_6, 7 :d_7, 8 :d_8, 9 :d_9}],
                    [:e {0 :e_0, 1 :e_1, 3 :e_3, 4 :e_4, 5 :e_5, 6 :e_6, 7 :e_7, 8 :e_8, 9 :e_9}],
                    [:g {0 :g_0, 1 :g_1, 2 :g_2, 3 :g_3, 4 :g_4, 5 :g_5, 6 :g_6, 7 :g_7, 8 :g_8, 9 :g_9}],
                    [:h {0 :h_0, 1 :h_1, 2 :h_2, 3 :h_3, 4 :h_4, 5 :h_5, 6 :h_6, 7 :h_7, 8 :h_8, 9 :h_9}]
                    [:i {0 :i_0, 1 :i_1, 2 :i_2, 3 :i_3, 4 :i_4, 5 :i_5, 6 :i_6, 7 :i_7, 8 :i_8, 9 :i_9}]})))
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row))
             #{[:a {0 :a_0, 1 :a_1, 2 :a_2, 3 :a_3, 4 :a_4, 5 :a_5, 6 :a_6, 7 :a_7, 8 :a_8, 9 :a_9}],
               [:b {0 :b_0, 1 :b_1, 2 :b_2, 3 :b_3, 4 :b_4, 5 :b_5, 7 :b_7, 8 :b_8, 9 :b_9}],
               [:c {0 :c_0, 1 :c_1, 3 :c_3, 4 :c_4, 5 :c_5, 6 :c_6, 8 :c_8, 9 :c_9}],
               [:d {0 :d_0, 1 :d_1, 3 :d_3, 4 :d_4, 5 :d_5, 6 :d_6, 7 :d_7, 8 :d_8, 9 :d_9}],
               [:e {0 :e_0, 1 :e_1, 3 :e_3, 4 :e_4, 5 :e_5, 6 :e_6, 7 :e_7, 8 :e_8, 9 :e_9}],
               [:f {0 :f_0, 1 :f_1, 2 :f_2, 3 :f_3, 5 :f_5, 6 :f_6, 8 :f_8, 9 :f_9}]
               [:g {0 :g_0, 1 :g_1, 2 :g_2, 3 :g_3, 4 :g_4, 5 :g_5, 6 :g_6, 7 :g_7, 8 :g_8, 9 :g_9}],
               [:h {0 :h_0, 1 :h_1, 2 :h_2, 3 :h_3, 4 :h_4, 5 :h_5, 6 :h_6, 7 :h_7, 8 :h_8, 9 :h_9}]
               [:i {0 :i_0, 1 :i_1, 2 :i_2, 3 :i_3, 4 :i_4, 5 :i_5, 6 :i_6, 7 :i_7, 8 :i_8, 9 :i_9}]}))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-rows test-cmap row-keys :keys-only))
             (set (cross-rows test-cmap row-keys :every-row :keys-only))
             #{:a, :c, :d, :e, :g, :h, :i}))
      ;; Keys-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :keys-only))
             #{:a, :b, :c, :d, :e, :f, :g, :h, :i}))

      ;; Vals-only
      ;; Default/every-row
      (is (= (set (cross-rows test-cmap row-keys :vals-only))
             (set (cross-rows test-cmap row-keys :every-row :vals-only))
             (set (list
                   {0 :a_0, 1 :a_1, 2 :a_2, 3 :a_3, 4 :a_4, 5 :a_5, 6 :a_6, 7 :a_7, 8 :a_8, 9 :a_9},
                   {0 :c_0, 1 :c_1, 3 :c_3, 4 :c_4, 5 :c_5, 6 :c_6, 8 :c_8, 9 :c_9},
                   {0 :d_0, 1 :d_1, 3 :d_3, 4 :d_4, 5 :d_5, 6 :d_6, 7 :d_7, 8 :d_8, 9 :d_9},
                   {0 :e_0, 1 :e_1, 3 :e_3, 4 :e_4, 5 :e_5, 6 :e_6, 7 :e_7, 8 :e_8, 9 :e_9},
                   {0 :g_0, 1 :g_1, 2 :g_2, 3 :g_3, 4 :g_4, 5 :g_5, 6 :g_6, 7 :g_7, 8 :g_8, 9 :g_9},
                   {0 :h_0, 1 :h_1, 2 :h_2, 3 :h_3, 4 :h_4, 5 :h_5, 6 :h_6, 7 :h_7, 8 :h_8, 9 :h_9}
                   {0 :i_0, 1 :i_1, 2 :i_2, 3 :i_3, 4 :i_4, 5 :i_5, 6 :i_6, 7 :i_7, 8 :i_8, 9 :i_9}))))

      ;; Verify row metadata
      (is (= (set (map (<| :col meta) (cross-rows test-cmap row-keys :vals-only)))
             (set (map (<| :col meta) (cross-rows test-cmap row-keys :every-row :vals-only)))
             #{:a :c :d :e :g :h :i}))
      
      ;; Vals-only
      ;; Any-row
      (is (= (set (cross-rows test-cmap row-keys :any-row :vals-only))
             #{{0 :a_0, 1 :a_1, 2 :a_2, 3 :a_3, 4 :a_4, 5 :a_5, 6 :a_6, 7 :a_7, 8 :a_8, 9 :a_9},
               {0 :b_0, 1 :b_1, 2 :b_2, 3 :b_3, 4 :b_4, 5 :b_5, ,,,,,,, 7 :b_7, 8 :b_8, 9 :b_9},
               {0 :c_0, 1 :c_1, ,,,,,,, 3 :c_3, 4 :c_4, 5 :c_5, 6 :c_6, ,,,,,,, 8 :c_8, 9 :c_9},
               {0 :d_0, 1 :d_1, ,,,,,,, 3 :d_3, 4 :d_4, 5 :d_5, 6 :d_6, 7 :d_7, 8 :d_8, 9 :d_9},
               {0 :e_0, 1 :e_1, ,,,,,,, 3 :e_3, 4 :e_4, 5 :e_5, 6 :e_6, 7 :e_7, 8 :e_8, 9 :e_9},
               {0 :f_0, 1 :f_1, 2 :f_2, 3 :f_3, ,,,,,,, 5 :f_5, 6 :f_6, ,,,,,,, 8 :f_8, 9 :f_9},
               {0 :g_0, 1 :g_1, 2 :g_2, 3 :g_3, 4 :g_4, 5 :g_5, 6 :g_6, 7 :g_7, 8 :g_8, 9 :g_9},
               {0 :h_0, 1 :h_1, 2 :h_2, 3 :h_3, 4 :h_4, 5 :h_5, 6 :h_6, 7 :h_7, 8 :h_8, 9 :h_9},
               {0 :i_0, 1 :i_1, 2 :i_2, 3 :i_3, 4 :i_4, 5 :i_5, 6 :i_6, 7 :i_7, 8 :i_8, 9 :i_9}})))
    ;; Cross-cols
    (do
      ;; Default/every col
      (is (= (set (cross-cols test-cmap col-keys))
             (set (cross-cols test-cmap col-keys :every-col))
             #{[0 {:a :a_0, :b :b_0, :c :c_0, :d :d_0, :e :e_0, :f :f_0, :g :g_0, :h :h_0, :i :i_0, :j :j_0}]
               [1 {:a :a_1, :b :b_1, :c :c_1, :d :d_1, :e :e_1, :f :f_1, :g :g_1, :h :h_1, :i :i_1, :j :j_1}]
               ;; 2 -- not present
               [3 {:a :a_3, :b :b_3, :c :c_3, :d :d_3, :e :e_3, :f :f_3, :g :g_3, :h :h_3, :i :i_3, ,,,,,,,}]
               [4 {:a :a_4, :b :b_4, :c :c_4, :d :d_4, :e :e_4, ,,,,,,,, :g :g_4, :h :h_4, :i :i_4, ,, ,,,,}]
               [5 {:a :a_5, :b :b_5, :c :c_5, :d :d_5, :e :e_5, :f :f_5, :g :g_5, :h :h_5, :i :i_5, ,,,,,,,}]
               [6 {:a :a_6, ,,,,,,,, :c :c_6, :d :d_6, :e :e_6, :f :f_6, :g :g_6, :h :h_6, :i :i_6, ,,,,,,,}]
               ;; 7 -- not present
               [8 {:a :a_8, :b :b_8, :c :c_8, :d :d_8, :e :e_8, :f :f_8, :g :g_8, :h :h_8, :i :i_8, :j :j_8}]
               [9 {:a :a_9, :b :b_9, :c :c_9, :d :d_9, :e :e_9, :f :f_9, :g :g_9, :h :h_9, :i :i_9, :j :j_9}]}))

      ;; Any-col
      (is 
       (= (set (cross-cols test-cmap col-keys :any-col))
          #{[0 {:a :a_0, :b :b_0, :c :c_0, :d :d_0, :e :e_0, :f :f_0, :g :g_0, :h :h_0, :i :i_0, :j :j_0}]
               [1 {:a :a_1, :b :b_1, :c :c_1, :d :d_1, :e :e_1, :f :f_1, :g :g_1, :h :h_1, :i :i_1, :j :j_1}]
               ;; 2 -- not present
               [3 {:a :a_3, :b :b_3, :c :c_3, :d :d_3, :e :e_3, :f :f_3, :g :g_3, :h :h_3, :i :i_3, ,,,,,,,}]
               [4 {:a :a_4, :b :b_4, :c :c_4, :d :d_4, :e :e_4, ,,,,,,,, :g :g_4, :h :h_4, :i :i_4, ,, ,,,,}]
               [5 {:a :a_5, :b :b_5, :c :c_5, :d :d_5, :e :e_5, :f :f_5, :g :g_5, :h :h_5, :i :i_5, ,,,,,,,}]
               [6 {:a :a_6, ,,,,,,,, :c :c_6, :d :d_6, :e :e_6, :f :f_6, :g :g_6, :h :h_6, :i :i_6, ,,,,,,,}]
               [7 {:a :a_7, :b :b_7, ,,,,,,,, :d :d_7, :e :e_7, ,,,,,,,, :g :g_7, :h :h_7, :i :i_7, :j :j_7}]
               [8 {:a :a_8, :b :b_8, :c :c_8, :d :d_8, :e :e_8, :f :f_8, :g :g_8, :h :h_8, :i :i_8, :j :j_8}]
               [9 {:a :a_9, :b :b_9, :c :c_9, :d :d_9, :e :e_9, :f :f_9, :g :g_9, :h :h_9, :i :i_9, :j :j_9}]}))

      ;; Keys-only
      ;; Default/every row
      (is (= (set (cross-cols test-cmap col-keys :keys-only))
             (set (cross-cols test-cmap col-keys :every-col :keys-only))
             #{0 1 3 4 5 6 8 9}))
      ;; Keys-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :keys-only)))
          #{0 1 3 4 5 6 7 8 9})

      ;; Vals-only
      ;; Default/every-col
      (is (= (set (cross-cols test-cmap col-keys :vals-only))
             (set (cross-cols test-cmap col-keys :every-col :vals-only))
             #{{:a :a_0, :b :b_0, :c :c_0, :d :d_0, :e :e_0, :f :f_0, :g :g_0, :h :h_0, :i :i_0, :j :j_0}
               {:a :a_1, :b :b_1, :c :c_1, :d :d_1, :e :e_1, :f :f_1, :g :g_1, :h :h_1, :i :i_1, :j :j_1}
               ;; 2 -- not present
               {:a :a_3, :b :b_3, :c :c_3, :d :d_3, :e :e_3, :f :f_3, :g :g_3, :h :h_3, :i :i_3, ,,,,,,,}
               {:a :a_4, :b :b_4, :c :c_4, :d :d_4, :e :e_4, ,,,,,,,, :g :g_4, :h :h_4, :i :i_4, ,, ,,,,}
               {:a :a_5, :b :b_5, :c :c_5, :d :d_5, :e :e_5, :f :f_5, :g :g_5, :h :h_5, :i :i_5, ,,,,,,,}
               {:a :a_6, ,,,,,,,, :c :c_6, :d :d_6, :e :e_6, :f :f_6, :g :g_6, :h :h_6, :i :i_6, ,,,,,,,}
               ;; 7 -- not present
               {:a :a_8, :b :b_8, :c :c_8, :d :d_8, :e :e_8, :f :f_8, :g :g_8, :h :h_8, :i :i_8, :j :j_8}
               {:a :a_9, :b :b_9, :c :c_9, :d :d_9, :e :e_9, :f :f_9, :g :g_9, :h :h_9, :i :i_9, :j :j_9}}))
      ;; Vals-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :vals-only))
             #{{:a :a_0, :b :b_0, :c :c_0, :d :d_0, :e :e_0, :f :f_0, :g :g_0, :h :h_0, :i :i_0, :j :j_0}
               {:a :a_1, :b :b_1, :c :c_1, :d :d_1, :e :e_1, :f :f_1, :g :g_1, :h :h_1, :i :i_1, :j :j_1}
               ;; 2 -- not present
               {:a :a_3, :b :b_3, :c :c_3, :d :d_3, :e :e_3, :f :f_3, :g :g_3, :h :h_3, :i :i_3, ,,,,,,,}
               {:a :a_4, :b :b_4, :c :c_4, :d :d_4, :e :e_4, ,,,,,,,, :g :g_4, :h :h_4, :i :i_4, ,, ,,,,}
               {:a :a_5, :b :b_5, :c :c_5, :d :d_5, :e :e_5, :f :f_5, :g :g_5, :h :h_5, :i :i_5, ,,,,,,,}
               {:a :a_6, ,,,,,,,, :c :c_6, :d :d_6, :e :e_6, :f :f_6, :g :g_6, :h :h_6, :i :i_6, ,,,,,,,}
               {:a :a_7, :b :b_7, ,,,,,,,, :d :d_7, :e :e_7, ,,,,,,,, :g :g_7, :h :h_7, :i :i_7, :j :j_7}
               {:a :a_8, :b :b_8, :c :c_8, :d :d_8, :e :e_8, :f :f_8, :g :g_8, :h :h_8, :i :i_8, :j :j_8}
               {:a :a_9, :b :b_9, :c :c_9, :d :d_9, :e :e_9, :f :f_9, :g :g_9, :h :h_9, :i :i_9, :j :j_9}})))
    ;; Full Cross
    (do
      ;; Default/every-row & every-col
      (is (= (set (cross test-cmap row-keys col-keys))
             (set (cross test-cmap row-keys col-keys :every-row :every-col))
             #{[[3,:c] :c_3],[[3,:d] :d_3],[[3,:e] :e_3],
               [[4,:c] :c_4],[[4,:d] :d_4],[[4,:e] :e_4],
               [[5,:c] :c_5],[[5,:d] :d_5],[[5,:e] :e_5],
               [[6,:c] :c_6],[[6,:d] :d_6],[[6,:e] :e_6]}))
      (let [;; DIssociating this key so that the results differ
            test-cmap (dissoc test-cmap [4,:d])]
        (is (= (set (cross test-cmap row-keys col-keys :every-row :any-col))
               #{[[3,:c] :c_3],[[3,:e] :e_3],
                 [[4,:c] :c_4],[[4,:e] :e_4],
                 [[5,:c] :c_5],[[5,:e] :e_5],
                 [[6,:c] :c_6],[[6,:e] :e_6]}))
        (is (= (set (cross test-cmap row-keys col-keys :any-row :every-col))
               #{[[3,:c] :c_3],[[3,:d] :d_3],[[3,:e] :e_3],
                 [[5,:c] :c_5],[[5,:d] :d_5],[[5,:e] :e_5],
                 [[6,:c] :c_6],[[6,:d] :d_6],[[6,:e] :e_6]}))
        (is (= (set (cross test-cmap row-keys col-keys :any-row :any-col))
               #{[[3,:c] :c_3],[[3,:d] :d_3],[[3,:e] :e_3],
                 [[4,:c] :c_4],,,,,,,,,,,,,,,[[4,:e] :e_4],
                 [[5,:c] :c_5],[[5,:d] :d_5],[[5,:e] :e_5],
                 [[6,:c] :c_6],[[6,:d] :d_6],[[6,:e] :e_6]}))))
    ;; Row/col ordering
    ;; Since these ae hash maps, there is no particular order
    ;; However, we are guaranteed to have certain rows or columns
    ;; grouped contiguously, depending on the options we pass in.
    (do
      ;; By-rows.  We have to test for no ordering options and by-rows, since
      ;; the default should be by-rows
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only :by-rows)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only :by-rows)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only :by-rows)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only :by-rows)]
        (is (not-empty res))
        (is (contiguous-by? first res)))
      ;; By-cols
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only :by-cols)]
        (is (not-empty res))
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only :by-cols)]
        (is (not-empty res))
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only :by-cols)]
        (is (not-empty res))
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only :by-cols)]
        (is (not-empty res))
        (is (contiguous-by? second res))))))

(deftest
  ^{:doc "Test the cross map according to a cross-map with random dissociations."}
  rand-cross-test
  (let [;; Randomly dissociate a few entries
        removals (take num-removed (shuffle (keys test-cmap)))
        test-cmap (apply dissoc test-cmap removals)
        row-keys (take 2 (shuffle test-rows))
        col-keys (take 2 (shuffle test-cols))
        _ (println "rand test removals: " removals)
        _ (println "row-keys: " row-keys)
        _ (println "col-keys: " col-keys)]
    
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
                       (map ky)))))
      ;; Keys-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :keys-only))
             (set (->> test-cmap
                       rows 
                       (filter #(some (second %) col-keys))
                       (map ky)))))

      ;; Vals-only
      ;; Default/every-col
      (is (= (set (cross-cols test-cmap col-keys :vals-only))
             (set (cross-cols test-cmap col-keys :every-col :vals-only))
             (set (->> test-cmap
                       rows
                       (filter #(every? (second %) col-keys))
                       (map vl)))))
      ;; Vals-only
      ;; Any-col
      (is (= (set (cross-cols test-cmap col-keys :any-col :vals-only))
             (set (->> test-cmap
                       rows 
                       (filter #(some (second %) col-keys))
                       (map val))))))
    ;; Full Cross
    (do
      ;; Default/every-row & every-col
      (is (= (set (cross test-cmap row-keys col-keys))
             (set (cross test-cmap row-keys col-keys :every-row :every-col))
             (cross-test-helper test-cmap row-keys col-keys true true)))
      (is (= (set (cross test-cmap row-keys col-keys :every-row :any-col))
             (cross-test-helper test-cmap row-keys col-keys true false)))
      (is (= (set (cross test-cmap row-keys col-keys :any-row :every-col))
             (cross-test-helper test-cmap row-keys col-keys false true)))
      (is (= (set (cross test-cmap row-keys col-keys :any-row :any-col))
             (cross-test-helper test-cmap row-keys col-keys false false))))

    ;; Contiguity
    (do
      ;; By-rows.  We have to test for no ordering options and by-rows, since
      ;; the default should be by-rows
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only :by-rows)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only :by-rows)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only :by-rows)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only)]
        (is (contiguous-by? first res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only :by-rows)]
        (is (contiguous-by? first res)))
      ;; By-cols
      (let [res (cross test-cmap row-keys col-keys :every-row :every-col :keys-only :by-cols)]
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :every-row :any-col :keys-only :by-cols)]
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :every-col :keys-only :by-cols)]
        (is (contiguous-by? second res)))
      (let [res (cross test-cmap row-keys col-keys :any-row :any-col :keys-only :by-cols)]
        (is (contiguous-by? second res))))))

;;; Confirm that cross-maps support all map operations
(deftest map-test)

;;; Confirm that additions/removals are properly reflected
;;; In the row/col indices, and that "rows" and "cols" match
;;; metadata counterparts
(deftest indexing-test)

(defn test-ns-hook
  []
  (const-cross-test)
  (rand-cross-test))
