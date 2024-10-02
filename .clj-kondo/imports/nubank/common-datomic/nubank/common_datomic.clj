(ns nubank.common-datomic
  (:require [clj-kondo.hooks-api :as hooks]))

(defn let-entities
  [{:keys [node]}]
  (let [[config db-symbols bindings & body] (rest (:children node))
        db-bindings (reduce #(concat %1 [%2 (hooks/token-node :dont-care)]) [] (:children db-symbols))
        new-bindings (vec (concat [(hooks/token-node '_datomic-config#) config] db-bindings (:children bindings)))]
    {:node (with-meta
             (hooks/list-node
              (concat
               [(hooks/token-node 'let)
                (hooks/vector-node new-bindings)]
               body))
             (meta node))}))
