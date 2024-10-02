(ns nubank.common-core
  (:require [clj-kondo.hooks-api :as hooks]
            [clojure.string :as string]))

(defn common-date-parser-node [node]
  (let [children (rest (:children node))]
    (doseq [child children]
      (when-let [value (and (hooks/string-node? child)
                            (hooks/sexpr child))]
        (when (string/includes? value "Y")
          (hooks/reg-finding! (merge {:message "Avoid 'Y' date notation, use 'y': https://www.juandebravo.com/2015/04/10/java-yyyy-date-format/"
                                      :type :avoid-upper-case-year-notation}
                                     (meta node))))))))

(defn local-date-time->string [{:keys [node]}]
  (common-date-parser-node node))

(defn local-date->string [{:keys [node]}]
  (common-date-parser-node node))

(defn local-time->string [{:keys [node]}]
  (common-date-parser-node node))

(defn string->local-date [{:keys [node]}]
  (common-date-parser-node node))

(defn string->local-time [{:keys [node]}]
  (common-date-parser-node node))

(defn string->year-month [{:keys [node]}]
  (common-date-parser-node node))

(defn year-month->string [{:keys [node]}]
  (common-date-parser-node node))
