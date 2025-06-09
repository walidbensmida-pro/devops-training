# Intégration de Kafka dans devops-training

## Introduction

**Qu'est-ce que Kafka ?**
Kafka est une plateforme de streaming distribuée conçue pour gérer des flux de messages en temps réel entre applications. Elle permet de publier, stocker et consommer des messages de façon fiable, rapide et scalable.

**Pourquoi utiliser Kafka ?**
- Kafka est idéal pour transmettre des données entre différents systèmes de manière asynchrone et tolérante aux pannes.
- Il gère de gros volumes de données et permet de traiter les messages en temps réel.
- Kafka conserve l'historique des messages, ce qui permet de relire ou rejouer des événements à tout moment.

**Kafka vs RabbitMQ (et autres brokers)**
- Kafka est optimisé pour le traitement de flux continus et le stockage persistant de messages (log distribué).
- RabbitMQ est un broker de messages orienté file d'attente, adapté aux échanges transactionnels et à la distribution de tâches.
- Kafka est plus adapté aux architectures orientées événements, à l'analytics temps réel, au streaming de données, tandis que RabbitMQ est souvent utilisé pour la communication entre microservices ou la gestion de files de tâches.

**Cas d'utilisation courants de Kafka**
- Collecte de logs et d'événements applicatifs
- Traitement de données en temps réel (analytics, monitoring)
- Intégration de microservices via des événements
- Streaming de données (IoT, capteurs, clickstream)
- Systèmes de messagerie asynchrone à grande échelle

## Suivi des étapes

---

### 1. Préparation de l’environnement
- [x] Installer/démarrer un Kafka local (Docker Compose)

  **Détails réalisés :**
  - Création d'un fichier `docker-compose.yml` dans `acid/docker-compose/` pour lancer Zookeeper et Kafka localement.
  - Problème rencontré : les images `wurstmeister` étaient obsolètes et non compatibles avec Docker moderne (erreur Schema 1).
  - Solution : remplacement par les images Bitnami, d'abord en version `latest` puis en version `3.4.0`, puis finalement en `3.3.2` pour garantir le mode Zookeeper classique.
  - Variables d'environnement utilisées pour Kafka :
    - `KAFKA_BROKER_ID=1` : identifiant unique du broker Kafka (utile si tu as plusieurs instances Kafka dans un cluster)
    - `KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181` : indique à Kafka comment se connecter à Zookeeper (ici, service zookeeper sur le port 2181)
    - `KAFKA_LISTENERS=PLAINTEXT://:9092` : définit sur quel port et protocole Kafka écoute les connexions (ici, en clair sur le port 9092)
    - `KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092` : indique aux clients comment joindre Kafka (ici, via localhost:9092)
    - `ALLOW_PLAINTEXT_LISTENER=yes` : autorise les connexions non sécurisées (pratique pour les tests, à ne pas utiliser en production)
  - Commandes utilisées :
    - `docker-compose up -d` pour démarrer les conteneurs
    - `docker ps` pour vérifier que les conteneurs sont bien "Up"
    - `docker-compose logs kafka` pour diagnostiquer les erreurs de démarrage
  - Explication :
    - Zookeeper est nécessaire pour la gestion du cluster Kafka (coordination, partitions, etc.) dans les versions classiques.
    - Kafka >= 2.8 propose un mode sans Zookeeper (KRaft), mais la majorité des tutos et intégrations Spring Boot utilisent encore Zookeeper pour la simplicité et la compatibilité.

- [x] Ajouter la dépendance Spring Kafka dans le pom.xml

  **Détails réalisés :**
  - Ajout de la dépendance suivante dans le fichier `pom.xml` :
    ```xml
    <dependency>
      <groupId>org.springframework.kafka</groupId>
      <artifactId>spring-kafka</artifactId>
    </dependency>
    ```
  - Cette dépendance permet à Spring Boot de produire et consommer des messages Kafka très simplement, avec des annotations et des configurations automatiques.
  - Après ajout, il faut relancer un `mvn clean install` pour télécharger la librairie et l'intégrer au projet.

### 2. Configuration de l’application
- [x] Ajouter la configuration Kafka dans application.yml

  **Détails réalisés :**
  - Ajout de la section suivante dans `src/main/resources/application.yml` :
    ```yaml
    spring:
      kafka:
        bootstrap-servers: localhost:9092           # Adresse du serveur Kafka local
        consumer:
          group-id: devops-training-group           # Identifiant du groupe de consommateurs
          auto-offset-reset: earliest               # Commencer à lire depuis le début si aucun offset
        producer:
          key-serializer: org.apache.kafka.common.serialization.StringSerializer   # Sérialisation des clés (texte)
          value-serializer: org.apache.kafka.common.serialization.StringSerializer # Sérialisation des valeurs (texte)
    ```
  - **Explications pédagogiques :**
    - `bootstrap-servers` : L’adresse de ton serveur Kafka (ici, celui lancé en local via Docker).
    - `consumer.group-id` : Identifiant du groupe de consommateurs. Tous les consumers avec ce même group-id partagent la lecture des messages d’un topic.
    - `consumer.auto-offset-reset` : Si aucun offset n’est trouvé pour ce groupe, on commence à lire les messages depuis le début du topic.
    - `producer.key-serializer` / `value-serializer` : Indique comment les clés et valeurs des messages sont converties en bytes pour l’envoi. Ici, on utilise des chaînes de caractères (String).
  - Cette configuration permet à Spring Boot de se connecter à ton Kafka local, d’envoyer et de recevoir des messages sous forme de texte, et de gérer les groupes de consommateurs pour le partage des messages.

### 3. Développement Backend
- [x] Créer un service Producer Kafka (envoi de messages)
- [x] Créer un service Consumer Kafka (réception de messages)
- [x] Stocker les messages reçus dans une liste en mémoire (pour affichage)

  **Détails réalisés :**
  - Création de la classe `KafkaProducerService` dans `service/` :
    - Utilise `KafkaTemplate<String, String>` pour envoyer des messages sur un topic Kafka.
    - Méthode `sendMessage(String topic, String message)` pour publier un message.
  - Création de la classe `KafkaConsumerService` dans `service/` :
    - Utilise l'annotation `@KafkaListener` pour écouter les messages du topic `devops-training-topic`.
    - Les messages reçus sont stockés dans une liste synchronisée en mémoire.
    - Méthode `getMessages()` pour récupérer la liste des messages reçus.
  - Ces services permettent de produire et consommer des messages Kafka très simplement dans l'application Spring Boot.

### 4. Développement Frontend
- [x] Créer une page web dédiée (/kafka-demo) via un contrôleur REST
    - [x] Endpoint POST /kafka-demo/publish pour envoyer un message
    - [x] Endpoint GET /kafka-demo/messages pour afficher les messages reçus

  **Détails réalisés :**
  - Création de la classe `KafkaDemoController` :
    - `POST /kafka-demo/publish?message=...` : publie un message sur le topic Kafka
    - `GET /kafka-demo/messages` : retourne la liste des messages reçus (stockés en mémoire)
  - Ces endpoints permettent de tester l'envoi et la réception de messages Kafka depuis un navigateur ou un outil comme Postman/curl.

### 5. Cas d’usage pédagogiques
- [ ] Publier un message simple
- [ ] Publier plusieurs messages
- [ ] (Bonus) Simuler une erreur ou un cas de partition

### 6. Tests et documentation
- [ ] Tester en local
- [ ] Documenter les étapes et concepts clés dans le wiki du projet

---

À chaque étape terminée, coche la case correspondante.
