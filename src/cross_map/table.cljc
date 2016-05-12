(ns cross-map.table
  "This namespace defines the cross-table datatype.
  It implements an optimized subset of cross-map.
  Specifically, its cross operations is similar to
  cross-cols with :all-cols specified.
  
  The cross-table is implemented as a vector, so row
  keys are integers, and are assigned automatically by
  the table.  Callers have no control over it.

  For the transient implementation, the vector of rows
  is a transient vector, but the rows themselves are
  immutable maps."
  (:require [clojure.set :as cst
             :refer [union intersection]]
            [cross-map.util :as u
             :refer [sassoc ssconj sconj #?(:clj Err)
                     kvp pair?]]
            [cross-map.hist-map :as hm
             :refer [hist-map map->hist-map same?
                     adds rems clear]])

  #?(:cljs
     (:require-macros [cross-map.util :as u
                       :refer [Err]]))

  #?(:clj
     (:import (clojure.lang PersistentList
                            IPersistentMap
                            IPersistentSet
                            IPersistentVector
                            ILookup
                            IObj
                            MapEquivalence
                            IFn
                            SeqIterator
                            MapEntry
                            IEditableCollection
                            ITransientMap
                            ITransientVector
                            Counted)
              (java.util Map)
              (java.io Serializable))))

(defprotocol ICrossTable
  "Anything trying to be a cross-table must implement
  these operations."
  (cross [this col-keys] "Returns a lazy sequence iterating over each row that contains all columns.")
  (row [this ^Long idx] "Returns the row at the specified index.")
  (col-idx [this] [this k] "Returns the column index.  If a key is specified, returns the index for that particular column."))

(defprotocol IPersistentCrossTable
  (conj-row [this r]
    "Attach a row to this table at the next available index.
Note: This could be an index that contains a nil table.")
  (assoc-row [this rk r]
    "Associate an entire row into the table at the specified index.")
  (dissoc-row [this rk]
    "Dissociate a row from the table, setting "))

(defprotocol ITransientCrossTable
  (conj-row! [this r])
  (assoc-row! [this rk r])
  (dissoc-row! [this rk]))

#?(:clj
   (deftype PersistentCrossTable
       [^IPersistentVector rowVec
        ^IPersistentMap    colIdx
        ^PersistentList    idxStack]

     Object
     (toString [this]
       (.toString rowVec))

     IObj
     (meta [this]
       (assoc (meta rowVec)
              ::idx-stack
              idxStack))
     (withMeta [this m]
       (PersistentCrossTable. (with-meta rowVec m)
                              colIdx
                              idxStack))
     
     ICrossTable
     (cross [this col-keys]
       (let [col-sets (mapv colIdx col-keys)
             iter-set (apply intersection col-sets)]
         (for [i iter-set
               :let [rvi (rowVec i)]
               :when rvi]
           (kvp i rvi))))
     (row [this idx]
       (nth rowVec idx))
     (col-idx [this] colIdx)
     (col-idx [this k] (colIdx k))
     
     IPersistentCrossTable
     (conj-row [this r]
       (let [;; Get the next available index
             idx (peek idxStack)
             ;; Pop it fom the stack
             ;; If the stack is empty, the default becomes
             ;; the end of the array
             stk (if idx (pop idxStack) idxStack)

             ;; Set idx to end of array if nil
             idx (or idx (count rowVec))

             ;; Convert r to a hist-map if it isn't already
             r (map->hist-map r)
             adts (adds r)
             rmvs (rems r)
             
             ;; modify row and col indices accordingly
             new-colIdx (reduce (fn [acc k]
                                  (update acc k disj idx))
                                colIdx
                                rmvs)
             
             new-colIdx (reduce (fn [acc k]
                                  (update acc k ssconj idx))
                                new-colIdx
                                adts)
            
             ;; We associate the row, clearing it first
             ;; We only wat to keep track of diffs between
             ;; assocs/conjs
             new-rowVec (assoc rowVec idx (clear r))]
         (PersistentCrossTable. new-rowVec
                                new-colIdx
                                stk)))

     (assoc-row [this rk r]
       (let [r (map->hist-map r)
             current-r (if (< rk (count rowVec))
                         (rowVec rk)
                         nil)
             ;; Additions and removals must be treated differently
             ;; if these maps do not havethe same history.
             [adts rmvs] (if (same? r current-r)
                           [(adds r) (rems r)]
                           [(keys r) (keys current-r)])
             
             new-colIdx (reduce (fn [acc k]
                                  (update acc k disj rk))
                                colIdx
                                rmvs)
             
             new-colIdx (reduce (fn [acc k]
                                  (update acc k ssconj rk))
                                new-colIdx
                                adts)

             new-rowVec (assoc rowVec rk (clear r))]
         (PersistentCrossTable. new-rowVec
                                new-colIdx
                                idxStack)))

     (dissoc-row [this rk]
       (let [;; Since we can't dissoc in the middle of a vector,
             ;; we instead set the row to nil
             new-rowVec (assoc rowVec rk nil)
             _ (println "Mark 1")
             ;; We remove all keys in the dissoc'd map
             rmvs (keys (rowVec rk))
             _ (println "Mark 2, rmvs: "rmvs)
             ;; We have a new space open for the next conj-row
             stk (conj idxStack rk)
             _ (println "Mark 3, stk: "stk)

             new-colIdx (reduce (fn [acc k]
                                  (update acc k disj rk))
                                colIdx
                                rmvs)
             
             _ (println "New colIdx: " new-colIdx)]
         (PersistentCrossTable. new-rowVec
                                new-colIdx
                                stk)))))

;;; Transient version
#?(:clj
   (deftype TransientCrossTable
       [^IPersistentVector ^:volatile-mutable !rowVec
        ^ITransientMap     ^:volatile-mutable !colIdx
        ^PersistentList    ^:volatile-mutable idxStack]

     Object
     (toString [this]
       (.toString !rowVec))
     
     ICrossTable
     (cross [this col-keys]
       (let [ ;; Get the column with the smallest number of entries
             col-sets (mapv !colIdx col-keys)
             iter-set (apply intersection col-sets)]
         (for [i iter-set
               :let [rvi (!rowVec i)]
               :when rvi]
           (kvp i rvi))))
     (row [this idx]
       (nth !rowVec idx))

     ;; WARNING: These are transient maps!!!
     (col-idx [this] !colIdx)
     (col-idx [this k] (!colIdx k))
     
     ITransientCrossTable
     (conj-row! [this r]
       (let [ ;; Get the next available index
             idx (peek idxStack)
             ;; Pop it fom the stack
             ;; If the stack is empty, the default becomes
             ;; the end of the array
             stk (if idx (pop idxStack) idxStack)

             ;; Set idx to end of array if nil
             idx (or idx (count !rowVec))
             
             ;; Convert r to a hist-map if it isn't already
             r (map->hist-map r)
             adts (adds r)
             rmvs (rems r)
             
             ;; modify row and col indices accordingly
             !new-colIdx (reduce (fn [acc k]
                                   (assoc! acc k
                                           (disj (acc k) idx)))
                                 !colIdx
                                 rmvs)
             
             !new-colIdx (reduce (fn [acc k]
                                   (assoc! acc k
                                           (sconj (acc k) idx)))
                                 !new-colIdx
                                 adts)
             
             ;; We associate the row, clearing it first
             ;; We only wat to keep track of diffs between
             ;; assocs/conjs
             !new-rowVec (assoc! !rowVec idx (clear r))]
         (set! !rowVec !new-rowVec)
         (set! !colIdx !new-colIdx)
         (set! idxStack stk)
         this))

     (assoc-row! [this rk r]
       (let [r (map->hist-map r)
             current-r (if (< rk (count !rowVec))
                         (!rowVec rk)
                         nil)
             ;; Additions and removals must be treated differently
             ;; if these maps do not havethe same history.
             [adts rmvs] (if (same? r current-r)
                           [(adds r) (rems r)]
                           [(keys r) (keys current-r)])
             
             !new-colIdx (reduce (fn [acc k]
                                   (update! acc k disj rk))
                                 !colIdx
                                 rmvs)
             
             !new-colIdx (reduce (fn [acc k]
                                   (update! acc k ssconj rk))
                                 !new-colIdx
                                 adts)

             !new-rowVec (assoc! !rowVec rk (clear r))]
         (set! !rowVec !new-rowVec)
         (set! !colIdx !new-colIdx)
         this))

     (dissoc-row! [this rk]
       (let [ ;; Since we can't dissoc in the middle of a vector,
             ;; we instead set the row to nil
             !new-rowVec (assoc! !rowVec rk nil)
             ;; We remove all keys in the dissoc'd map
             rmvs (keys (!rowVec rk))
             ;; We have a new space open for the next conj-row
             stk (conj idxStack rk)

             !new-colIdx (reduce (fn [acc k]
                                   (update! acc k disj rk))
                                 !colIdx
                                 rmvs)]
         (set! !rowVec !new-rowVec)
         (set! !colIdx !new-colIdx)
         (set! idxStack stk)
         this))))

(defn cross-table
  "Create a cross-table from the provided rows.
  They will be associated with indices 0..n-1, where
  n is the numver of rows provided."
  [& rows]
  (reduce conj-row
          (PersistentCrossTable. []
                                 {}
                                 ())
          rows))

(defn t-cross-table
  [& rows]
  (reduce conj-row!
          (TransientCrossTable. (transient [])
                                (transient {})
                                ())
          rows))
