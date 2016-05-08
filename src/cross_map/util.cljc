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

(defn ky [[k _ :as kv]]
  "Improved version of core/key that works on vectors."
  {:pre [(= (count kv) 2)]}
  k)

(defn vl [[_ v :as kv]]
  "Improved version of core/val that works on vectors."
  {:pre [(= (count kv) 2)]}
  v)

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
