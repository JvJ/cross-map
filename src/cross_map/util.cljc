(ns cross-map.util
  "Utility functions for cross-map implementation.")

(defmacro $
  "Alias for core/partial. Seven letters makes partial
  application take up too much text."
  [& body]
  `(partial ~@body))

(defmacro <|
  "Alias for core/comp."
  [& body]
  `(comp ~@body))

(defmacro |>
  "Alias for reverse composition."
  [& body]
  `(comp ~@(reverse body)))

(defmacro Err
  "Create an exception in clj or cljs."
  [& body]
  #?(:clj  `(Exception. ~@body)
     :cljc `(js/Error. ~@body)))

(defn new-uuid
  "Generate a random UUID in clojure or clojurescript."
  []
  #?(:clj (java.util.UUID/randomUUID)
     :cljs (random-uuid)))

(defn pair?
  "Utility function that determines whether a value is
  a two-element vector."
  [v]
  (and (vector? v)
       (= (count v) 2)))

(defn kvp
  "Utility for cross-platform key-value-pair creation."
  [k v]
  #?(:clj  (clojure.lang.MapEntry. k v)
     :cljs [k v]))

(defn ky [kv]
  "Improved version of core/key that works on vectors."
  {:pre [(vector? kv) (= (count kv) 2)]}
  (nth kv 0))

(defn vl [kv]
  "Improved version of core/val that works on vectors."
  {:pre [(vector? kv) (= (count kv) 2)]}
  (nth kv 1))

;;; Map versions of set intersections/unions
(defn munion
  "Like merge, but keeps the original entry in
  the case of a conflict."
  ([m1] m1)
  ([m1 m2]
   (transduce (filter #(not (contains? m1 (key %))))
              conj
              m1
              m2))
  ([m1 m2 & ms]
   (reduce munion
           (munion m1 m2)
           ms)))

(defn mintersection
  "Like intersection, but for maps.  Keeps
  original entries in case of a conflict."
  ([m1] m1)
  ([m1 m2]
   (let [m1-smaller? (< (count m1) (count m2))
         m1-new (if m1-smaller? m1 m2)
         m2-new (if m1-smaller? m2 m1)]
     (transduce (comp (map key)
                      (filter #(not (contains? m2-new %))))
                dissoc
                m1-new
                m1-new)))
  ([m1 m2 & ms]
   (reduce mintersection
           (mintersection m1 m2)
           ms)))

;;; Memoization using volatiles.  Avoids the overhead for single-threaded uses.
(defn vmemo [f]
  (let [mem (volatile! {})]
    (fn [& args]
      (or (@mem args)
          (let [ret (apply f args)]
            (vswap! mem ret)
            ret)))))

;;; Map utility functions
(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure.

  Source code copied from clojure.core.incubator, since that library is
  clj-specific, and this should be cross-platform."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

;;; Transient utility functions
(defn nassoc!
  "Like assoc!, but makes new transient maps if
  nil is passed in."
  [coll k v]
  (assoc! (or coll (transient {}))
          k v))

(defn assoc-in!
  "Like assoc-in, but works only on transient maps."
  [m [k & ks] v]
  (if ks
    (assoc! m k (assoc-in! (get m k (transient {})) ks v))
    (assoc! m k v)))

(defn update!
  "Like update, but works only on tansient maps."
  ([m k f]
   (assoc! m k (f (get m k (transient {})))))
  ([m k f x]
   (assoc! m k (f (get m k (transient {})) x)))
  ([m k f x y]
   (assoc! m k (f (get m k (transient {})) x y)))
  ([m k f x y z]
   (assoc! m k (f (get m k (transient {})) x y z)))
  ([m k f x y z & more]
   (apply assoc! m k (f (get m k (transient {}))) x y z more)))

(defn update-in!
  "Like update-in, but works only on transient maps."
  [m [k & ks] f & args]
  (if ks
    (assoc! m k (apply update-in! (get m k (transient {})) ks f args))
    (assoc! m k (apply f (get m k (transient {})) args))))

(defn dissoc-in!
  "Like dissoc-in, but works only on transient maps."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in! nextmap ks)]
        (if (> (count newmap) 0)
          (assoc! m k newmap)
          (dissoc! m k)))
      m)
    (dissoc! m k)))

(defn ssassoc
  "A version of assoc which defaults to a sorted-map
  when associating to nil."
  ([m k v]
   (if m
     (assoc m k v)
     (sorted-map k v)))
  ([m k v & {:as kvs}]
   (reduce-kv ssassoc
              (ssassoc m k v)
              kvs)))

(defn sconj
  "Conj that defaults to a set when conjing to nil."
  ([coll k]
   (conj (or coll #{}) k))
  ([coll k & ks]
   (reduce sconj (sconj coll k) ks)))

(defn ssconj
  "A version of conj that defaults to a sorted-map
  when conjing onto nil."
  ([coll k]
   (conj (or coll (sorted-set)) k))
  ([coll k & ks]
   (reduce ssconj (ssconj coll k) ks)))

(defn sconj!
  "Like sconj, but for transient sets."
  ([coll k]
   (conj! (or coll (transient #{})))))


