# Kafka : Le groupId expliqué avec des schémas

## 1. Cas monolithique (une seule appli)

```text
   +-------------------------------+
   |   Topic Kafka (3 partitions)  |
   +-------------------------------+
   |  P0  |  P1  |  P2  |
   +-------------------+---+
      |      |      |
      |      |      |
      +------+------+------
                |
           [Consumer]
        (groupId: mon-appli)
```

- Un seul consumer (application) avec un groupId unique (ex : `mon-appli`).
- Kafka envoie tous les messages de toutes les partitions à cette application.
- **Utilisation typique** : appli de logs, traitement séquentiel, batch unique.

## 2. Cas microservices (plusieurs applis, millions de messages)

```text
   +-------------------------------+
   |   Topic Kafka (3 partitions)  |
   +-------------------------------+
   |  P0  |  P1  |  P2  |
   +-------------------+---+
     |      |      |
     |      |      |
 [C1]|  [C2]|  [C3] |
(groupId: service-commande)
```

- Plusieurs consumers (microservices) avec le même groupId (ex : `service-commande`).
- Kafka répartit les partitions entre les consumers : chaque microservice lit une partie des messages.
- **Utilisation typique** : traitement massif, scalabilité, parallélisme, microservices.

## 3. À retenir

- Le groupId, c'est le nom de l'équipe de "facteurs" (consumers) qui se partagent le travail.
- Même groupId = partage automatique des partitions.
- GroupId différent = chaque équipe peut relire tout le courrier depuis le début.

---

*Schémas textuels pour une compréhension rapide sans image.*
