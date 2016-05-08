(ns cross-map.perf
  "In this namespace, there are performance tests
  for the cross map.  The use case is modeled after
  the expected use case for the SPECS system."
  (:require [cross-map.core :refer [cross-map cross-cols]]
            #?(:clj [clojure.test :as t]
                :cljs [cljs.test :as t :include-macros true])
             #?(:clj [criterium.core :as cri :refer [bench benchmark]]))
  #?(:clj (:import java.lang.Math)))


;;;; Here, the entities will have components, and
;;;; several functions will operate on cross-sections
;;;; of the entities and components.

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

(defsys update-spin
  #{:Angle :Spin}
  [[id r]]
  [id (update r :Angle + (:Spin r))])

(defsys update-orientation
  #{:Angle :Orientation}
  [[id r]]
  [id (let [a (:Angle r)
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
   update-spin
   update-p
   ;update-orientation
   ])

(defn new-entity
  "Make a random new entity."
  []
  [(java.util.UUID/randomUUID)
   {:Position [0.0 0.0]
    :Velocity [(rand) (rand)]
    :Acceleration [(rand) (rand)]
    :Angle (rand Math/PI)
    :Orientation [1.0 0.0]
    :Spin (rand)}])

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
                  (mapcat (comp entity-pairs f))
                  (into cm)))
    cm order)))

(defn test-run
  [num-entities bench?]
  (let [ents (entities-to-cross-map (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems ents))
      (run-systems ents))))
