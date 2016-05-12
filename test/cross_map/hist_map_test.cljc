(ns cross-map.hist-map-test
  (:require [clojure.set :as cljset
             :refer [union intersection difference]]
            [cross-map.hist-map :as hm :refer :all]
            #?(:clj [clojure.test :as t :refer [deftest is]]
               :cljs [cljs.test :as t :include-macros true
                      ;; TODO: This may break in cljs.  We'll see...
                      :refer [deftest is]])))


;;; Some very basic tests

(deftest random-assocs

  (let [key-seq (shuffle (range 1000))
        key-set (set key-seq)
        
        init {:acc (hist-map)
              :adts #{}
              :rmvs #{}
              :no-rmvs #{}}

        {:keys [acc adts rmvs no-rmvs] :as results}
        (reduce (fn [{m :acc a :adts
                      r :rmvs nr :no-rmvs}
                     k]
                  (if (>= 0.5 (rand))
                    ;; Then - assoc
                    {:acc (assoc m k k)
                     :adts (conj a k)
                     :rmvs (disj r k)
                     :no-rmvs (disj nr k)}
                    ;; Else - dissoc
                    {:acc (dissoc m k)
                     :adts a
                     :rmvs (if (contains? a k)
                             (conj r k)
                             r)
                     :no-rmvs (if (contains? a k)
                                nr
                                (conj nr k))}))
                init
                key-seq)]

    ;; The maps addiions/removals have to match ours
    (and
     ;; The first two tests need to pass for the others to
     ;; be executed
     (is (= adts (-> acc meta ::hm/additions) (adds acc)))
     (is (= rmvs (-> acc meta ::hm/removals) (rems acc)))

     ;; The no-rmvs can't be in any other sets
     (do
      (is (not (some no-rmvs (union adts rmvs))))

      ;; The map must have maintained the same UUID
      (is (= (-> init :acc meta ::hm/id)
             (-> acc meta ::hm/id)
             (id acc)))
      
      ;; Make sure that all keys are mutually exclusive
      (is (empty? (intersection adts rmvs)))
      (is (empty? (intersection rmvs no-rmvs)))
      (is (empty? (intersection adts no-rmvs)))

      ;; Union must be exactly the same we started with
      (is (= (union adts rmvs no-rmvs) key-set))))))

(defn test-ns-hook
  []
  (random-assocs))
