(ns cross-map.perf
  "In this namespace, there are performance tests
  for the cross map.  The use case is modeled after
  the expected use case for the SPECS system."
  (:require [cross-map.core :refer [cross-map cross-cols]]
            [cross-map.util :refer [kvp]]
            #?(:clj [clojure.test :as t]
                :cljs [cljs.test :as t :include-macros true])
             #?(:clj [criterium.core :as cri :refer [bench benchmark]]))
  #?(:clj (:import java.lang.Math)))


;;;; Here, the entities will have components, and
;;;; several functions will operate on cross-sections
;;;; of the entities and components.

(defn v+
  "Add the numbers in two vectors"
  [v1 & vs]
  (with-meta 
    (apply mapv + v1 vs)
    (meta v1)))

(def component-types
  "Just a set of what we consider component types."
  #{:Position
    :Velocity
    :Acceleration 
    :Angle        ; Angle unit is facing
    :Orientation  ; Unit vector for angle
    :Spin         ; Angular velocity
    })

(defmacro defsys
  "Define a system function with component profile.
  This function will take a row as input."
  [nme profile & body]
  `(def ~nme (-> (fn ~@body)
                 (with-meta {:profile ~profile}))))

;; Update position
(defsys update-p
  #{:Velocity :Position}
  [[id r]]
  [id
   (update r :Position #(mapv + % (:Velocity r)))])

;; Update velocity
(defsys update-v
  #{:Velocity :Acceleration}
  [[id r]]
  [id
   (update r :Velocity #(mapv + % (:Acceleration r)))])

(defsys update-a
  #{:Angle :Spin}
  [[id r]]
  [id (update r :Angle v+ (:Spin r))])

(defsys update-o
  #{:Angle :Orientation}
  [[id r]]
  [id (let [a (nth (:Angle r) 0)
            sa (Math/sin a)
            ca (Math/cos a)]
        (assoc r :Orientation
               [ca sa]))])

(defn get-profile
  [f]
  (-> f meta :profile))

(def system-order
  "Defining an execution order for the systems."
  [update-v
   update-a
   update-p
   update-o
   ])

(defn new-entity
  "Make a random new entity."
  []
  [(java.util.UUID/randomUUID)
   {:Position [0.0 0.0]
    :Velocity [(rand) (rand)]
    :Acceleration [(rand) (rand)]
    :Angle [(rand Math/PI)]
    :Orientation [1.0 0.0]
    :Spin [(rand)]}])

;; TODO: Should a function like this
;; be included in cross-map.core?
(defn entity-pairs
  [[id entity]]
  (map (fn [[k v]]
         [[id k] v])
       entity))

(defn entities-to-cross-map
  "Takes a list of entities and transforms it into an
  entity-component cross-map."
  [entities]
  (transduce (mapcat entity-pairs)
             conj (cross-map)
             entities))

(defn run-systems
  "Run the systems in the order specified over
  the cross-map specified."
  ([cm] (run-systems system-order cm))
  ([order cm]
   (reduce (fn [acc f]
             (->> (get-profile f)
                  (cross-cols acc)
                  (into cm (mapcat (comp entity-pairs f)))))
    cm order)))

(defn test-run
  [num-entities bench?]
  (let [ents (entities-to-cross-map (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems ents))
      (run-systems ents))))


;;;; These functions are all named with a final quote '
;;;; This should be pronounced as "prime"
;;;; Since the above-mentioned update strategy is slower
;;;; than expected, these functions will return only single components
;;;; or lists of components.

(def component-types
  "Just a set of what we consider component types."
  #{:Position
    :Velocity
    :Acceleration 
    :Angle        ; Angle unit is facing
    :Orientation  ; Unit vector for angle
    :Spin         ; Angular velocity
    })

(defn ctype
  [o]
  (-> o meta :ctype))

(defn component?
  "Determine if this object is a valid component."
  [c]
  (-> c meta :ctype component-types))

(defn component-list?
  "Determine if this object is a component list."
  [l]
  (-> l meta :ctype (= :clist)))

(defn cpt
  "Turn an object into a component of the specified type."
  [t o]
  (with-meta o (assoc (meta o) :ctype t)))

(defn clist
  "Crreate a component list."
  [& cpts]
  (with-meta cpts {:ctype :clist}))

;; Return vals of the following systems are of the form:
;; [id c], where c is a component or component list.


(defsys update-p'
  #{:Velocity :Position}
  [[id r]]
  (kvp id (cpt :Position (v+ (:Velocity r) (:Position r)))))

(defsys update-v'
  #{:Velocity :Acceleration}
  [[id r]]
  (kvp id (cpt :Velocity (v+ (:Velocity r) (:Acceleration r)))))

(defsys update-a'
  #{:Angle :Spin}
  [[id r]]
  (kvp id (cpt :Angle (v+ (:Angle r) (:Spin r)))))

(defsys update-o'
  #{:Angle :Orientation}
  [[id r]]
  (kvp id (cpt :Orientation
               (let [a (nth (:Angle r) 0)]
                 [(Math/cos a) (Math/sin a)]))))

(def system-order'
  [update-v'
   update-a'
   update-p'
   update-o'
   ])

(defn run-systems'
  ([cm] (run-systems' system-order' cm))
  ([order cm]
   (reduce (fn [acc f]
             (->> (get-profile f)
                  (cross-cols acc)
                  (into acc (comp (mapcat ;; TODO: Conditional map transducer??
                                   (fn [ent] (let [[id res] (f ent)]
                                               (cond
                                                 (component-list? res)
                                                 (map #(-> [[id (ctype %)] %]))
                                                 (component? res) (list res)
                                                 :else nil))))))))
           cm
           order)))

(defn run-systems-single'
  "Applies to single components only."
  ([cm] (run-systems-single' system-order' cm))
  ([order cm]
   (reduce (fn [acc f]
             (into acc (mapcat f) (cross-cols acc (get-profile f))))
           cm order)))
;; LEFTOFF: How to do it??
;; Also, transients?

(defn test-run'
  [num-entities bench?]
  (let [ents (entities-to-cross-map (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems' ents))
      (run-systems' ents))))

(defn test-run-single'
  [num-entities bench?]
  (let [ents (entities-to-cross-map (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems-single' ents))
      (run-systems-single' ents))))
