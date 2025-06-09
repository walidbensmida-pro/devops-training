# Guide Kafka pour Débutant (Spring Boot, Kafka, Kafka Streams)

## 1. Introduction à Kafka

### Qu'est-ce que Kafka ?
Kafka est une plateforme de streaming distribuée qui permet de publier, stocker et consommer des flux de messages en temps réel. Elle est conçue pour être rapide, scalable et tolérante aux pannes.

### Pourquoi utiliser Kafka ?
- Pour connecter des applications entre elles de façon asynchrone.
- Pour traiter de gros volumes de données en temps réel.
- Pour garantir la persistance et la relecture des messages.

### Différence avec RabbitMQ
- **Kafka** : Idéal pour le streaming, l'analytics temps réel, la persistance longue durée, l'agrégation de données.
- **RabbitMQ** : Idéal pour la distribution de tâches, la communication transactionnelle, les microservices synchrones.

## 2. Concepts de base
- **Broker** : Serveur Kafka qui stocke et distribue les messages.
- **Topic** : Canal nommé où les messages sont publiés et lus.
- **Producer** : Application qui envoie des messages dans un topic.
- **Consumer** : Application qui lit les messages d'un topic.
- **Partition** : Division d'un topic pour la scalabilité et le parallélisme.
- **Offset** : Position d'un message dans une partition.
- **Consumer Group** : Groupe de consumers qui se partagent la lecture d'un topic.

## 3. Installation et démarrage local
- Utilisation de Docker Compose pour lancer Zookeeper, Kafka et Kafka UI.
- Configuration des variables d'environnement pour le développement local (voir docker-compose.yml).
- Utilisation de Kafka UI pour visualiser les topics et messages.

## 4. Utilisation de Kafka avec Spring Boot
### a. Producer/Consumer classiques
- Utilisation de KafkaTemplate pour envoyer des messages.
- Utilisation de @KafkaListener pour consommer les messages.
- Exemple d'API REST pour publier et lire les messages.

### b. Kafka Streams et KStream
- Kafka Streams permet de traiter les flux de messages en temps réel (filtrage, transformation, agrégation).
- KStream est un flux de données provenant d'un topic.
- Déclaration d'une topologie de stream dans une classe de configuration.
- Différence avec le consumer classique : on peut chaîner des opérations complexes sur le flux.

## 5. Cas d'usage pédagogiques
- Publier un message simple et le consommer.
- Publier plusieurs messages et observer le parallélisme.
- Utiliser Kafka Streams pour agréger des résultats (ex : compter le nombre de messages par clé).
- Simuler une erreur ou une partition pour observer la résilience.

## 6. Concepts avancés
- State Store : stockage local pour l'agrégation et la gestion d'état dans Kafka Streams.
- Changelog Topic : sauvegarde automatique de l'état pour la résilience.
- Idempotence : garantir que chaque message est traité une seule fois.
- Finalisation asynchrone : déclencher une action quand toutes les sous-tâches d'un job sont terminées.

## 7. Bonnes pratiques et pièges à éviter
- Toujours surveiller les offsets et les consumer groups.
- Bien configurer les partitions pour le parallélisme.
- Utiliser les state stores et changelog topics pour la résilience.
- Tester la reprise après crash (redémarrage de l'appli).

## 8. Ressources pour aller plus loin
- [Documentation officielle Kafka](https://kafka.apache.org/documentation/)
- [Spring Kafka Reference](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [Kafka Streams Quickstart](https://kafka.apache.org/36/documentation/streams/quickstart)
- [Kafka UI](https://github.com/provectus/kafka-ui)

---

Ce guide est évolutif : complète-le avec tes propres tests, schémas, et retours d'expérience au fil de ta montée en compétence !
