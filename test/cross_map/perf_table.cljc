(ns cross-map.perf-table
  (:require [cross-map.table :as ct
             :refer [cross-table cross conj-row assoc-row
                     t-cross-table conj-row! assoc-row!]]
            [criterium.core :as cri :refer [bench benchmark]]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true]))
  #?(:clj (:import java.lang.Math)))


;;; Much of this is replicated from the perf namespace

;; TODO: How do I add new entities?... who knows?
(def ^:dynamic *idx*
  "The current index being used."
  nil)

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

;;; Systems!!
(defsys update-p
  #{:Velocity :Position}
  [{:keys [Velocity] :as e}]
  (update e :Position v+ Velocity))

(defsys update-v
  #{:Velocity :Acceleration}
  [{:keys [Acceleration] :as e}]
  (update e :Velocity v+ Acceleration))

(defsys update-a
  #{:Angle :Spin}
  [{:keys [Spin] :as e}]
  (update e :Angle + Spin))

(defsys update-o
  #{:Angle :Orientation}
  [{:keys [Angle] :as e}]
  (assoc e :Orientation [(Math/cos Angle) (Math/sin Angle)]))

(defsys update-none
  [:Angle]
  [e]
  e)

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
  {:Position [0.0 0.0]
   :Velocity [(rand) (rand)]
   :Acceleration [(rand) (rand)]
   :Angle (rand Math/PI)
   :Orientation [1.0 0.0]
   :Spin (rand)})


(defn entities-into-table
  [es]
  (reduce conj-row (cross-table) es))

(defn entities-into-table!
  [es]
  (reduce conj-row! (t-cross-table) es))

(defn run-systems
  "Run the systems in the order specified over
  the cross-table specified."
  ([cm] (run-systems system-order cm))
  ([order cm]
   (reduce (fn [acc f]
             (reduce (fn [acc' [i e]]
                       (assoc-row acc' i (f e)))
                     acc
                     (cross acc (get-profile f))))
           cm
           order)))

(defn run-systems!
  "Run the systems in the order specified over
  the cross-table specified."
  ([cm] (run-systems! system-order cm))
  ([order cm]
   (reduce (fn [acc f]
             (reduce (fn [acc' [i e]]
                       (assoc-row! acc' i (f e)))
                     acc
                     (cross acc (get-profile f))))
           cm
           order)))

(defn test-run
  [num-entities sys-order bench?]
  (let [ents (entities-into-table (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems sys-order ents))
      (run-systems sys-order ents))))

(defn test-run!
  [num-entities sys-order bench?]
  (let [ents (entities-into-table! (repeatedly num-entities new-entity))]
    (if bench?
      (bench (run-systems! sys-order ents))
      (run-systems! sys-order ents))))
