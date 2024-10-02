(ns hello-datomic.setup
  (:require [datomic.api :as d]))

(defn hello-datomic-conn
  "Create a connection to an anonymous, in-memory database"
  []
  (let [uri "datomic:mem://hello-datomic"]
    (d/delete-database uri)
    (d/create-database uri)
    (d/connect uri)))

(def connection (hello-datomic-conn))

connection

@(d/transact connection [{:db/ident :person/first-name
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/doc "A person's name"
                          :db/unique :db.unique/identity}])

@(d/transact connection [
                         {:person/first-name "Helena"}
                         {:person/first-name "Maria"}
                         {:person/first-name "Pedro"}
                         {:person/first-name "Joaquim"}
                         {:person/first-name "Monica"}
                         {:person/first-name "Natan"}])

(println (d/q '[:find ?n
                :where [?e :person/first-name ?n]]
              (d/db connection)))

@(d/transact connection [{:db/ident :person/last-name
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/doc "A person's last name"
                          }])


@(d/transact connection [{:person/first-name "Manoela" :person/last-name "De Freitas"}])


(println (d/q '[:find ?e
                :where
                [?e :person/first-name]]
              (d/db connection)))

; pegando o primeiro id do set que retorna e adicionando um
; last name ao objeto desse primeiro id

(let [setPeople (d/q '[:find ?e
                       :where
                       [?e :person/first-name]]
                     (d/db connection))
      idFirstPerson (ffirst setPeople)]
  @(d/transact connection [{:db/id idFirstPerson
                            :person/last-name "Silva"}])
  (println "resultado" (d/q '[:find (pull ?e [*]) :where [?e :person/last-name "Silva"]] (d/db connection))))
