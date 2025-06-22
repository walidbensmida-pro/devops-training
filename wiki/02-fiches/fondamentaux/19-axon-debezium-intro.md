# Axon Framework & Debezium : Introduction Simple

## 1. Axon Framework

- **Axon** est un framework Java open source pour construire des applications basées sur les architectures **CQRS** et **Event Sourcing**.
- Il facilite la gestion des commandes, des événements, des agrégats et des projections.
- Il fournit des outils pour la distribution des messages, la persistance des événements, la gestion des sagas (processus longs), etc.
- Utilisé pour des applications complexes où la séparation lecture/écriture et la traçabilité des changements sont importantes.

**Exemple d’utilisation :**
- Tu veux faire du CQRS/Event Sourcing en Java/Spring sans tout coder à la main.
- Axon gère pour toi la publication, le stockage et la distribution des événements.

**Ressources :**
- [Axon Framework Documentation](https://docs.axoniq.io/reference-guide/)

---

## 2. Debezium

- **Debezium** est un outil open source de **Change Data Capture (CDC)**.
- Il permet de capter en temps réel les changements (insert/update/delete) dans une base de données (MySQL, PostgreSQL, MongoDB, etc.) et de les publier dans Kafka.
- Pratique pour synchroniser des bases, alimenter des systèmes temps réel, ou faire de l’event sourcing à partir d’une base existante.

**Exemple d’utilisation :**
- Tu veux que chaque modification dans ta base soit envoyée dans Kafka pour alimenter d’autres microservices ou des vues de lecture.
- Debezium s’installe comme un connecteur Kafka Connect, pas besoin de modifier ton application.

**Ressources :**
- [Debezium Documentation](https://debezium.io/documentation/)

---

## 3. À retenir pour l’entretien

- **Axon** : facilite CQRS/Event Sourcing en Java, gère commandes/événements/projections.
- **Debezium** : capte les changements en base et les publie dans Kafka (CDC).
- Les deux sont utiles pour des architectures orientées événements, mais à des niveaux différents (Axon côté application, Debezium côté base de données).
