(ns cross-map.hist-map
  "This namespace defines the hist-map datatype.
  This acts just like a normal map, but keeps track
  of assoc/dissoc'd keys in its metadata."

  (:require [cross-map.util :as u
             :refer [new-uuid
                     #?(:clj Err)]])

  #?(:cljs
     (:require-macros [cross-map.util :as u
                       :refer [Err]]))
  
  #?(:clj
     (:import (clojure.lang IPersistentMap
                            IPersistentSet
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
              (java.util Map
                         UUID)
              (java.io Serializable))))

(defprotocol IHistMap
  (id [this]  "Get the UUID of this map.")
  (adds [this] "Get the additions performed on this map.")
  (rems [this] "Get the removals performed on this map."))

(defprotocol IPersistentHistMap
  (clear [this] "Resets additions and removals.  The map will also get a new ID."))

(defprotocol ITransientHistMap
  (clear! [this] "Resets additions and removals.  The map will also get a new ID."))

;;; A map implementation that stores assoc'd and dissoc'd
;;; keys in its metadata, so that diffs aren't required.
;;; Metadata can also be cleared.
#?(:clj
   (deftype PersistentHistMap
       [^IPersistentMap mainMap
        ^IPersistentSet additions
        ^IPersistentSet removals
        ^UUID           instanceId]

     Object
     (toString [this]
       (.toString mainMap))
     
     IObj
     (meta [this]
       (assoc (meta mainMap)
              ::additions additions
              ::removals removals
              ::id instanceId))
     (withMeta [this m]
       (PersistentHistMap. (with-meta mainMap m)
                           additions
                           removals
                           instanceId))

     IHistMap
     (id [this] instanceId)
     (adds [this] additions)
     (rems [this] removals)
     
     IPersistentHistMap
     (clear [this]
       (PersistentHistMap. mainMap
                           (empty additions)
                           (empty removals)
                           (new-uuid)))
     
     ILookup                         ; Implements get
     (valAt [this k] (get mainMap k))
     (valAt [this k not-found] (get mainMap k not-found))

     IFn
     (invoke [this k] (get mainMap k))
     (invoke [this k not-found] (get mainMap k not-found))

     IPersistentMap
     (count [this] (count mainMap))
     (assoc [this k v]
       (PersistentHistMap. (assoc mainMap k v)
                           (conj additions k)
                           (disj removals k)
                           instanceId))
     (without [this k]               ; Implements dissoc
       (PersistentHistMap. (dissoc mainMap k)
                           (disj additions k)
                           (if (contains? mainMap k)
                             (conj removals k)
                             removals)
                           instanceId))
     (empty [this]
       (PersistentHistMap. (empty mainMap)
                           (empty additions)
                           (into removals (keys mainMap))
                           instanceId))
     (cons [this kvp]                ; Implements conj
       (if (map? kvp)
         (into this kvp)
         (let [[k v] kvp]
           (.assoc this k v))))
     (equiv [this o]
       (= mainMap o))
     (hashCode [this]
       (.hashCode mainMap))
     (equals [this o]
       (or (identical? this o)
           (.equals mainMap o)))
     (containsKey [this k]           ; Implements contains?
       (contains? mainMap k))
     (entryAt [this k]
       (when-let [v (.valAt this k)]
         (MapEntry. k v)))
     (seq [this] (.seq mainMap))

          MapEquivalence

     Serializable ; Functionality is automatically inherited here
     
     Iterable
     (iterator [this] (SeqIterator. (seq this)))))


;;;; API functions
(defn hist-map
  "Create a hist-map from key-value pairs passed in.

  A hist map starts with a unique ID and keeps a history
  of additions and removals in its metadata, under
  :cross-map.hist-map/additions and :cross-map.hist-map/removals.
  
  The clear function can be used to reset the history of the map,
  after which it gains a new ID."
  [& {:as kvps}]
  (into (PersistentHistMap. {} #{} #{} (new-uuid))
        kvps))

(defn map->hist-map
  "Convert a normal map to a hist map.  All keys will
  be treated as new keys and added to the addition history.

  If the map is already a hist-map, it is returned with
  no modification."
  [m]
  (if (satisfies? IPersistentHistMap m)
    m
    (PersistentHistMap. m
                        (set (keys m))
                        #{}
                        (new-uuid))))

(defn same?
  "Compare the UUID's of any number of maps to see if they're
  the same hist-map that hasn't been cleared.

  Non hist-maps can be passed in, and they will qualify as
  the same since they both have a nil ID."
  ([m1 m2]
   (let [id1 (-> m1 meta ::id)
         id2 (-> m2 meta ::id)] 
     (= id1 id2)))
  ([m1 m2 & ms]
   (apply = (map #(-> % meta ::id)
                 (cons m1 (cons m2 ms))))))
