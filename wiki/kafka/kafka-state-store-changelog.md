# Kafka Streams : State Store, KTable et Changelog (Explications pour débutant)

## 1. C'est quoi un State Store ?

Un **state store** est comme un carnet de notes local à ton application Kafka Streams. Il sert à **mémoriser** des informations (compteurs, totaux, dernières valeurs, etc.) pendant le traitement des messages.

- Quand tu veux compter, additionner, ou garder la dernière valeur pour chaque clé, tu as besoin d'un state store.
- Le state store est **local** (rapide) et **sauvegardé** automatiquement dans un topic Kafka spécial appelé **changelog**.

## 2. C'est quoi un Changelog ?

Le **changelog** est un topic Kafka où Kafka Streams écrit tout ce qui change dans le state store.
- Si ton application s'arrête, Kafka Streams relit le changelog pour **reconstruire** le carnet (state store).
- Ça rend ton application **résiliente** : tu ne perds jamais l'état !

## 3. KStream, KTable et State Store : la différence

- Un **KStream** est comme un toboggan : chaque message passe, mais rien n'est mémorisé.
- Un **KTable** est une **vue logique** (table clé/valeur) qui représente le dernier état connu pour chaque clé.
- **Derrière chaque KTable, il y a un state store** : c'est le “carnet” local qui mémorise la valeur pour chaque clé.
- **KTable = la table que tu utilises dans ton code (vue d’état)**
- **State store = le carnet local qui stocke les données de la KTable**
- Quand tu fais une agrégation ou un count, Kafka Streams crée automatiquement un state store pour ta KTable.
- Le state store est sauvegardé dans un changelog Kafka pour la résilience.

## 4. Exemple imagé pour enfant de 10 ans

Imagine que tu es un facteur qui reçoit des enveloppes (messages Kafka) :

- **KStream** :
  - Tu ouvres chaque enveloppe, tu lis la fiche, tu la jettes. Tu ne gardes rien en mémoire.
  - Si on te demande "combien de fiches vertes as-tu vu ?", tu ne sais pas répondre !

- **KStream + State Store** :
  - Tu ouvres chaque enveloppe, tu lis la fiche.
  - Si la fiche est verte, tu ajoutes +1 dans ton carnet (state store) pour ce client.
  - Si on te demande "combien de fiches vertes pour le client X ?", tu regardes dans ton carnet et tu sais répondre !
  - Si tu perds ton carnet, pas grave : tu as une photocopie (changelog) dans une boîte (topic Kafka) et tu peux tout retrouver.

- **KTable** :
  - C'est comme un tableau Excel où tu as la dernière valeur pour chaque client (clé).
  - Ce tableau s'appuie sur un state store pour stocker les données localement.

## 5. Exemple de code simple

```java
// KStream sans state store : juste un toboggan
stream.foreach((key, value) -> System.out.println("Message reçu : " + value));

// KStream avec state store : on compte les messages par clé
KTable<String, Long> counts = stream
    .groupByKey()
    .count(Materialized.as("mon-carnet-de-comptes"));

// On peut maintenant interroger le state store pour chaque clé !
```

## 6. Résumé visuel

```
[Topic Kafka] --> [KStream] --> [KTable (vue d'état)] <--> [State Store (carnet local)] <--> [Changelog (photocopie dans Kafka)]
```

- **KStream** : flux d'événements, rien n'est mémorisé.
- **KTable** : vue d'état, dernière valeur par clé.
- **State Store** : carnet local pour mémoriser des résultats.
- **Changelog** : sauvegarde du carnet dans Kafka pour ne rien perdre.

---

**À retenir** :
- Utilise un state store dès que tu veux compter, additionner, ou garder un état entre plusieurs messages dans Kafka Streams.
- Le changelog te protège contre les coupures ou les crashs.
- Une KTable s'appuie toujours sur un state store pour fonctionner.
