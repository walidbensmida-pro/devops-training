# Schéma groupId Kafka (mermaid)

```mermaid
graph TD
    subgraph Topic Kafka
        P0[Partition 0]
        P1[Partition 1]
        P2[Partition 2]
    end

    %% Cas monolithique
    Consumer1[Consumer (groupId: mon-appli)]
    P0 --> Consumer1
    P1 --> Consumer1
    P2 --> Consumer1

    %% Cas microservices (décommente pour illustrer)
    %% ConsumerA[Consumer 1 (groupId: service-commande)]
    %% ConsumerB[Consumer 2 (groupId: service-commande)]
    %% ConsumerC[Consumer 3 (groupId: service-commande)]
    %% P0 --> ConsumerA
    %% P1 --> ConsumerB
    %% P2 --> ConsumerC
```

---

- **Monolithique** : un seul consumer lit toutes les partitions.
- **Microservices** : plusieurs consumers avec le même groupId, chaque partition est assignée à un consumer.
- Change le groupId pour créer une nouvelle "équipe" qui relit tout le topic.
