(ns cross-map.core
  "Defines operations on the cross-map data structure."

  (:require [cross-map.util :as u
             :refer [pair? dissoc-in vmemo]])
  
  ;; Clojure-specific imports of java interfaces
  ;; to implement
  #?(:clj
     (:import (clojure.lang IPersistentMap
                            ILookup
                            IObj
                            MapEquivalence
                            IFn
                            SeqIterator
                            MapEntry
                            IEditableCollection
                            ITransientMap
                            Counted)
              (java.util Map)
              (java.io Serializable))))

;;; Forward declaration of a constructor proxy.
;;; Necessary since deftypes can'e be forward-declared.
(declare transient-cross-map)

(defprotocol ICrossMap
  "This protocol must be implemented by all cross-maps."
  
  (row [this r-key] "A map representing a single row.  A map of {col->element}.")
  (col [this c-key] "A map representing a single column.  A map of {row->element}.")
  (rows [this] "A map representing all rows.  A map of {row->{col->element}}.")
  (cols [this] "A map representing all colums.  A map of {col->{row->element}}")
  (crossIndexRows [this r-keys opts] "Full-map cross-section by row-first.")
  (crossIndexCols [this c-keys opts] "Full-map cross-section by col-first.")
  (crossIndex [this r-keys c-keys opts] "Cross section of elements in all specified rows and all specified columns."))

(defn- cross-index-helper
  "crossIndexRows and crossIndexColumns are the same if you switch
  around instances of rowIdx and colIdx.  Pass in true to rows? to
  iterate over rows.  False to iterate over columns.

  Explanation of the keyword params:
  (TODO)"
  [this rows? selected-keys
   {:keys [every any keys-only vals-only] :as opts}]
  (let [_ (and any every
               (throw (Exception.
                       (if rows?
                         ":any-row and :every-row cannot both be specified."
                         ":any-col and :every-col cannot both be specified."))))
        opts (disj opts :any :every)
        _ (if (or (and keys-only vals-only)
                  (and keys-only vals-only))
            (throw (Exception. (str "Invalid key/value options: " opts))))
        opts (disj opts :keys-only :vals-only)
        _ (if-not (empty? opts)
            (throw (Exception. (str "Unsupported options: " opts))))

        rowIdx (.rowIdx this)
        colIdx (.colIdx this)

        ;; The primary and secondary iteration dimensions - row or column
        [pri sec] (if rows? [rowIdx colIdx] [colIdx rowIdx])
        
        ;; Default options
        no-keys (empty? selected-keys)
        every (and (not no-keys)
                   (or every (not any)))
        selected-keys (or (seq selected-keys) (keys pri))
        kv-mode (or keys-only vals-only)
        

        ;; List predicate for checking valid entries
        valid? (cond every every?
                     :else some)
        

        
        ;; Pairs of [pri-key sec-key]
        ps-keys (cond
                  ;; If we're going by entries, things are a little more
                  ;; straightforward
                  ;; Select smallest pri for "every"
                  every (let [min-pk (apply min-key #(count (pri %))
                                            (or (seq selected-keys)
                                                (keys pri)))]
                          (for [sk (keys (pri min-pk))]
                            [min-pk sk]))
                  ;; Select all pri for any
                  :else (for [pk selected-keys
                              sk (keys (pri pk))]
                          [pk sk]))

        ;; An accumulator, for efficiency.  Only used in "any" case
        tags-v (if-not every (volatile! (transient #{})))]
    (for [[pk sk] ps-keys 
          :when (and (or (and tags-v (not (@tags-v sk)))
                         every)
                     (valid? (sec sk) selected-keys))
          :let [_ (and tags-v (vswap! tags-v conj! sk))]]
      (case kv-mode
        :keys-only sk
        :vals-only (sec sk)
        [sk (sec sk)]))))

;;;; CLJ Implementation
#?(:clj
   (deftype PersistentCrossMap
       [^IPersistentMap mainMap
        ^IPersistentMap rowIdx
        ^IPersistentMap colIdx]

     Object
     (toString [this]
       (.toString mainMap))

     ILookup ; Implements get behaviour
     (valAt [this ky]
       (get mainMap ky))
     (valAt [this ky not-found]
       (get mainMap ky not-found))
     
     IPersistentMap
     (count [this]
       (count mainMap))
     
     ;; Implements assoc behavior
     (assoc [this ky value]
       (if-let [[r c] (and (pair? ky) ky)]
         ;; Then
         (PersistentCrossMap. (assoc mainMap [r c] value)
                              (assoc-in rowIdx [r c] value)
                              (assoc-in colIdx [c r] value))
         ;; Else
         (PersistentCrossMap. (assoc mainMap ky value)
                              rowIdx
                              colIdx)))

     ;; Implements dissoc behavior
     (without [this ky]
       (if-let [[r c] (and (pair? ky) ky)]
         ;; Then
         (PersistentCrossMap. (dissoc mainMap [r c])
                              (dissoc-in rowIdx [r c])
                              (dissoc-in colIdx [c r]))
         ;; Else
         (PersistentCrossMap. (dissoc mainMap ky)
                              rowIdx
                              colIdx)))
     
     (empty [this]
       (PersistentCrossMap. (empty mainMap) (empty rowIdx) (empty colIdx)))

     ;; Implements conj behaviour
     (cons [this kvp]
       (if (map? kvp)
         (into this kvp)
         (let [[k v] kvp]
           (.assoc this k v))))

     ;; Equivalence, hashing, etc. is
     ;; determined by the main map only
     (equiv [this o]
       (= mainMap o))
     
     (hashCode [this]
       (.hashCode mainMap))

     (equals [this o]
       (or (identical? this o)
           (.equals mainMap o)))

     ;; Implements contains? behaviour
     (containsKey [this item]
       (contains? mainMap item))

     ;; Returns a map entry for key
     (entryAt [this k]
       (when-let [v (.valAt this k)]
         (MapEntry. k v)))

     ;; Lazy sequence of map entries (MapEntry)
     ;; In cljs, they are vectors
     (seq [this] (.seq mainMap))

     
     IFn ; So that this can be called as a function
     (invoke [this k] (.valAt this k))
     (invoke [this k not-found] (.valAt this k not-found))

     IObj ; For metadata - metadata includes row and col idx
     (meta [this] (assoc (meta mainMap)
                         ::row-idx rowIdx
                         ::col-idx colIdx))
     (withMeta [this m]
       (PersistentCrossMap. (with-meta mainMap m)
                            rowIdx colIdx))

     ICrossMap ; Implementing the protocol defined above
     (row [this r-key] (rowIdx r-key))
     (col [this c-key] (colIdx c-key))
     (rows [this] rowIdx)
     (cols [this] colIdx)
     (crossIndexRows [this r-keys
                      {:keys [any every
                              any-row every-row
                              keys-only vals-only] :as opts}]
       (cross-index-helper this true r-keys
                           (-> opts
                               (disj :any-row :every-row)
                               (conj (and any-row :any)
                                     (and every-row :every))
                               (disj nil))))
     (crossIndexCols [this c-keys
                         {:keys [any every
                                 any-col every-col
                                 keys-only vals-only] :as opts}]
       (cross-index-helper this false c-keys
                           (-> opts
                               (disj :any-col :every-col)
                               (conj (and any-col :any)
                                     (and every-col :every))
                               (disj nil))))
     
     (crossIndex [this r-keys c-keys
                  {:keys [any-row every-row
                          any-col every-col
                          keys-only vals-only
                          by-rows by-cols] :as opts}]
       (let [;; LEFTOFF: Error checking params, and continue

             ;; Doing a quick little memoization with volatiles
             ;; instead of atoms.

             checkfn (fn [f coll]
                       (let [v (volatile! (transient {}))]
                         (fn [k]
                           (or (@v k)
                               (let [ret (f k coll)]
                                 (vswap! assoc! k ret))))))
             [pri sec] (if by-rows [rowIdx colIdx] [colIdx rowIdx] )
             valid-row? (checkfn (if every-row every? some)
                                 rows)
             valid-col? (checkn (if every-row every? some)
                                rows)]))
     
     #_IEditableCollection ; For turning this into a transient structure
     #_(asTransient [this]
       (transient-cross-map (transient mainMap)
                            (reduce (fn [acc [k v]]
                                      (assoc! acc k (transient v)))
                                    (transient rowIdx)
                                    rowIdx)
                            (reduce (fn [acc [k v]]
                                      (assoc! acc k (transient v)))
                                    (transient colIdx)
                                    colIdx)))
     
     MapEquivalence

     Serializable ; Functionality is automatically inherited here
     
     Iterable
     (iterator [this] (SeqIterator. (seq this))) 
    
     Map ; To work as a java map
     (size [this] (count mainMap))
     (isEmpty [this] (empty? mainMap))
     (containsValue [this v] (some (partial = v) (vals this)))
     (get [this k] (.valAt this k))
     (put [this k v] (throw (UnsupportedOperationException.)))
     (remove [this k] (throw (UnsupportedOperationException.)))
     (putAll [this m] (throw (UnsupportedOperationException.)))
     (clear [this] (throw (UnsupportedOperationException.)))
     (keySet [this] (set (keys this)))
     (values [this] (vals this))
     (entrySet [this] (set this))))

;;;; CLJS Implementation
#?(:cljs
   (deftype PersistentCrossMap
       [mainMap
        rowIdx
        colIdx]

     Object ; Basic JS object functionality
     (toString [this] (.toString mainMap))

     ;;; The subsequent Object methods are based on methods
     ;;; marked as experimental in cljs.core/PersistentHashMap.
     ;;; Unsure of the reliability/stability of these
     (equiv [this other] (-equiv this other))
     (keys [this] (es6-iterator (keys mainMap)))
     (entries [this] (es6-entries-iterator (seq mainMap)))
     (values [this (es6-iterator (vals mainMap))])
     (has [this k] (contains? mainMap k))
     (get [this k not-found] (get mainMap k not-found))
     (forEach [this f] (doseq [[k v] mainMap] (f v k)))

     ICloneable
     (-clone [this] (PersistentCrossMap. mainMap rowIdx colIdx))

     IIterable
     (-iterator [this] (-iterator mainMap))

     IWithMeta
     (-with-meta [this meta]
       (PersistentCrossMap. (with-meta mainMap meta) rowIdx colIdx))

     IMeta ; Metadata includes row and col idx
     (-meta [this] (assoc (meta mainMap) ::row-idx rowIdx ::col-idx colIdx))

     ICollection
     (-conj [this entry]
       (if-let [[k v] (and (pair? entry) entry)]
         (-assoc this k v) ; then
         (into this entry))) ; else

     IEmptyableCollection
     (-empty [this]
       (PersistentCrossMap. (empty mainMap)
                            (empty rowIdx)
                            (empty colIdx)))

     IEquiv
     (-equiv [this other] (-equiv mainMap other))

     IHash
     (-hash [this] (-hash mainMap))

     ISeqable
     (-seq [this] (-seq mainMap))

     ICounted
     (-count [this] (-count mainMap))

     ILookup
     (-lookup [this k] (-lookup mainMap k))
     (-lookup [this k not-found] (-lookup mainMap k not-found))

     IAssociative
     (-assoc [this k v]
       ;; If the key is a pair, it is cross-indexed.
       ;; Otherwise, it]s just put in the map as normal
       (if-let [[r c] (and (pair? k) k)]
         (PersistentCrossMap. (assoc mainMap k v)
                              (assoc-in rowIdx [r c] v)
                              (assoc-in colIdx [c r] v))
         (PersistentCrossMap. (assoc mainMap k v)
                              rowIdx
                              colIdx)))
     
     (-contains-key? [this k] (-contains-key? mainMap k))

     IMap
     (-dissoc [this k]
       (if-let [[r c] (and (pair? k) k)]
         (PersistentCrossMap. (dissoc mainMap [r c])
                              (dissoc-in rowIdx [r c])
                              (dissoc-in colIdx [c r]))
         (PersistentCrossMap. (dissoc mainMap ky)
                              rowIdx
                              colIdx)))

     IKVReduce ; Provides reduce-kv functionality
     (-kv-reduce [this f init] (-kv-reduce mainMap f init))

     IFn
     (-invoke [this k] (-invoke mainMap k))
     (-invoke [this k not-found] (-invoke mainMap k not-found))

     #_IEditableCollection
     #_(-as-transient [this])

     ))

;;;; CLJ Transient Implementation
#?(:clj
   (deftype TransientCrossMap
       [^ITransientMap ^:volatile-mutable !mainMap
        ^ITransientMap ^:volatile-mutable !rowIdx
        ^ITransientMap ^:volatile-mutable !colIdx]))

;;;; CLJS Transient Implementation

;;;; Forward-declared constructor proxies.
;;;; Necessary since deftypes can't be forward-declared.
(defn- transient-cross-map
  [!mainMap !rowIdx !colIdx]
  (TransientCrossMap. !mainMap !rowIdx !colIdx))

;;;; API - shared across platforms
(defn cross-r
  "Cross-section of a cross-map by rows.  A sequence of all
  elements that are in all specified rows.  These are key-value
  pairs of [[row col]->Element]."
  [cm r-keys]
  (crossIndex cm r-keys () #{}))

(defn cross-c
  "Cross-section of a cross-map by columns.  A sequence of all
  elements that are in all specified columns.  These are key-value
  pairs of [[row col]->Element]."
  [cm c-keys]
  (crossIndex cm () c-keys #{}))

(defn cross-map
  "Create a cross-map with any number of entries."
  [& {:as kvps}]
  (into (PersistentCrossMap. {} {} {})
        kvps))

