(ns hello-datomic.modeling
  (:require [datomic.api :as d]
            [hello-datomic.setup :refer [hello-datomic-conn]]))

(defn install-person-and-likes
  "install person and likes attributes"
  [connection]
  @(d/transact connection [
                           {:db/ident       :person/first-name
                            :db/valueType   :db.type/string
                            :db/cardinality :db.cardinality/one
                            :db/doc         "A person's name"
                            :db/unique      :db.unique/identity}

                           {:db/ident       :person/last-name
                            :db/valueType   :db.type/string
                            :db/cardinality :db.cardinality/one
                            :db/doc         "A person's last name"
                            :db/unique      :db.unique/identity}

                           {
                            :db/ident       :likes/drink
                            :db/valueType   :db.type/string
                            :db/doc         "A person's fav drink"
                            :db/cardinality :db.cardinality/one
                            }

                           {
                            :db/ident       :likes/food
                            :db/valueType   :db.type/string
                            :db/doc         "A person's fav food"
                            :db/cardinality :db.cardinality/one
                            }]))

(defn load-data
  "load data into installation of likes and person"
  [connection]
  @(d/transact connection [
                           {:person/first-name "Helena"
                            :person/last-name  "Almeida"
                            :likes/food        "pizza"
                            :likes/drink       "beer"}

                           {:person/first-name "Alice"
                            :person/last-name  "Campos"
                            :likes/food        "sushi"
                            :likes/drink       "wine"}

                           {:person/first-name "Laura" :person/last-name "Ferreira" :likes/food "pizza" :likes/drink "water"}
                           {:person/first-name "Miguel" :person/last-name "Melo" :likes/food "pizza" :likes/drink "water"}
                           {:person/first-name "Arthur" :person/last-name "Ramos" :likes/food "tacos" :likes/drink "beer"}
                           {:person/first-name "Noah" :person/last-name "Silva" :likes/food "curry" :likes/drink "beer"}
                           ]))



(def conn (hello-datomic-conn))
(install-person-and-likes conn)
(load-data conn)

(println "resultado amigos que curtem pizza"
         (d/q '[:find ?n
                :where [?e :likes/food "pizza"]
                [?e :person/first-name ?n]]
              (d/db conn)))

(println "resultado amigos com first-name e last-name que gostam de pizza"
         (d/q '[:find ?first-name ?last-name :where
                [?e :likes/food "pizza"]
                [?e :person/first-name ?first-name]
                [?e :person/last-name ?last-name]]
              (d/db conn)))

(println "resultado de amigos que gostam de pizza e cerveja"
         (d/q '[:find ?first-name :where
                [?e :likes/food "pizza"]
                [?e :likes/drink "beer"]
                [?e :person/first-name ?first-name]]
              (d/db conn)))

(println "resultado de amigos que gostam de pizza ou cerveja"
         (d/q '[:find ?first-name :where
                (or
                 [?e :likes/food "pizza"]
                 [?e :likes/drink "beer"])
                [?e :person/first-name ?first-name]]
              (d/db conn)))