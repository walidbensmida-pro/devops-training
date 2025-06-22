# Guide Kafka pour Débutant (Spring Boot, Kafka, Kafka Streams)

## Sommaire
- [Guide Kafka pour Débutant (Spring Boot, Kafka, Kafka Streams)](#guide-kafka-pour-débutant-spring-boot-kafka-kafka-streams)
  - [Sommaire](#sommaire)
  - [1. Introduction à Kafka](#1-introduction-à-kafka)
    - [Qu'est-ce que Kafka ?](#quest-ce-que-kafka-)
    - [Pourquoi utiliser Kafka ?](#pourquoi-utiliser-kafka-)
    - [Différence avec RabbitMQ](#différence-avec-rabbitmq)
  - [2. Concepts de base](#2-concepts-de-base)
    - [2.1 Architecture Kafka](#21-architecture-kafka)
    - [2.2 Partitionnement](#22-partitionnement)
    - [2.3 Sérialisation des messages](#23-sérialisation-des-messages)
      - [JSON (Simple mais verbose)](#json-simple-mais-verbose)
      - [Avro (Recommandé en production)](#avro-recommandé-en-production)
        - [Configuration Avro](#configuration-avro)
  - [3. Mise en place locale avec Docker](#3-mise-en-place-locale-avec-docker)
    - [Docker Compose minimal](#docker-compose-minimal)
  - [4. Spring Boot Integration](#4-spring-boot-integration)
    - [4.1 Configuration (`application.yml`)](#41-configuration-applicationyml)
    - [4.2 Producer Service](#42-producer-service)
    - [4.3 Consumer Service](#43-consumer-service)
    - [4.4 Kafka Streams](#44-kafka-streams)
  - [5. Kafka Streams Avancé](#5-kafka-streams-avancé)
    - [5.1 KStream vs KTable](#51-kstream-vs-ktable)
      - [KStream](#kstream)
      - [KTable](#ktable)
    - [5.2 Topologie](#52-topologie)
      - [Concepts](#concepts)
      - [Exemple de topologie complexe](#exemple-de-topologie-complexe)
    - [5.3 State Stores](#53-state-stores)
      - [Qu'est-ce qu'un State Store ?](#quest-ce-quun-state-store-)
      - [Configuration](#configuration)
      - [Accès au State Store](#accès-au-state-store)

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

### 2.1 Architecture Kafka
- **Broker** : Serveur Kafka qui stocke les messages.
- **Topic** : Canal de communication nommé où les messages sont publiés.
- **Partition** : Sous-division d'un topic pour la scalabilité.
- **Producer** : Application qui publie des messages.
- **Consumer** : Application qui lit les messages.

### 2.2 Partitionnement
- Les messages sont répartis dans les partitions selon la clé (hash(key) % nb_partitions).
- Permet le parallélisme et la tolérance aux pannes.

### 2.3 Sérialisation des messages

#### JSON (Simple mais verbose)
- Facile à lire et à déboguer.
- Peu performant pour de gros volumes.

#### Avro (Recommandé en production)
- Format binaire compact, schéma fort, évolutif.
- Nécessite un schéma `.avsc` et (souvent) un Schema Registry.

##### Configuration Avro
- Dépendance : `kafka-avro-serializer` (Confluent)
- Repository : `https://packages.confluent.io/maven/`
- Génération des classes avec le plugin Avro Maven.
- Utilisation du Schema Registry pour la gestion des schémas.

## 3. Mise en place locale avec Docker

### Docker Compose minimal
```yaml
version: '3'
services:
  zookeeper:
    image: bitnami/zookeeper:latest
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
    ports:
      - "2181:2181"
      
  kafka:
    image: bitnami/kafka:latest
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper

  schema-registry:
    image: confluentinc/cp-schema-registry:latest
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: kafka:9092
      SCHEMA_REGISTRY_LISTENERS: http://0.0.0.0:8082
    ports:
      - "8082:8082"
    depends_on:
      - kafka

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    ports:
      - "8080:8080"
    environment:
      - KAFKA_CLUSTERS_0_NAME=local
      - KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9092
    depends_on:
      - kafka
```

## 4. Spring Boot Integration

### 4.1 Configuration (`application.yml`)
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: io.confluent.kafka.serializers.KafkaAvroSerializer
    properties:
      "schema.registry.url": http://localhost:8082
```

### 4.2 Producer Service
```java
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, ContractAvro> avroKafkaTemplate;

    // Pour messages simples
    public void sendMessage(String topic, String key, String message) {
        stringKafkaTemplate.send(topic, key, message);
    }

    // Pour messages Avro
    public void sendContract(String topic, String key, ContractAvro contract) {
        avroKafkaTemplate.send(topic, key, contract);
    }
}
```

### 4.3 Consumer Service
```java
@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "mon-topic")
    public void listen(ContractAvro contract) {
        // Traitement du message
    }
}
```

### 4.4 Kafka Streams
```java
@Configuration
public class KafkaStreamsConfig {
    @Bean
    public KStream<String, ContractAvro> processStream(StreamsBuilder builder) {
        return builder.stream("input-topic", 
            Consumed.with(Serdes.String(), contractAvroSerde()))
            .filter((key, contract) -> contract.getAmount() > 1000)
            .to("output-topic");
    }
}
```

## 5. Kafka Streams Avancé

### 5.1 KStream vs KTable

#### KStream
- Flux de données immuable (chaque événement est traité individuellement).
- Idéal pour le traitement d'événements, le filtrage, le mapping.

```java
KStream<String, Long> stream = builder.stream("input-topic");
```

#### KTable
- Vue matérialisée d'un flux (clé unique, dernière valeur connue).
- Idéal pour les jointures, les agrégations, les états.

```java
KTable<String, Long> table = builder.table("input-topic",
    Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("store-name")
        .withKeySerde(Serdes.String())
        .withValueSerde(Serdes.Long()));
```

### 5.2 Topologie

#### Concepts
- La topologie Kafka Streams est un graphe d'opérateurs (source, map, filter, join, sink, etc.).
- Chaque opérateur transforme ou route les messages.

#### Exemple de topologie complexe
```java
@Bean
public KStream<String, ContractAvro> buildTopology(StreamsBuilder builder) {
    // Source
    KStream<String, ContractAvro> sourceStream = builder
        .stream("input-topic", Consumed.with(Serdes.String(), contractAvroSerde()));

    // Branch (séparation du flux)
    Map<String, KStream<String, ContractAvro>> branches = sourceStream
        .split()
        .branch((key, contract) -> contract.getAmount() > 1000,
            Branched.withConsumer(high -> high.to("high-value-topic")))
        .branch((key, contract) -> true,
            Branched.withConsumer(normal -> normal.to("normal-value-topic")));

    // Agrégation avec KTable
    KTable<String, Long> countByKey = sourceStream
        .groupByKey()
        .count(Materialized.as("counts-store"));

    // Publication des résultats
    countByKey.toStream().to("counts-topic");

    return sourceStream;
}
```

### 5.3 State Stores

#### Qu'est-ce qu'un State Store ?
- Un magasin d'état local pour stocker des agrégats, des compteurs, etc.
- Utilisé pour les opérations d'agrégation, de jointure, etc.

#### Configuration
```java
@Bean
public KStream<String, Long> stateStoreExample(StreamsBuilder builder) {
    // Création du state store
    StoreBuilder<KeyValueStore<String, Long>> countStoreBuilder = 
        Stores.keyValueStoreBuilder(
            Stores.persistentKeyValueStore("counts"),
            Serdes.String(),
            Serdes.Long());
    
    // Ajout du store
    builder.addStateStore(countStoreBuilder);

    // Utilisation dans la topologie
    return builder.stream("input-topic")
        .process(() -> new CustomProcessor(), "counts");
}
```

#### Accès au State Store
```java
public class CustomProcessor extends Processor<String, Long> {
    private KeyValueStore<String, Long> store;

    @Override
    public void init(ProcessorContext context) {
        store = context.getStateStore("counts");
    }

    @Override
    public void process(String key, Long value) {
        // Lecture/écriture dans le store
        Long count = store.get(key);
        store.put(key, count == null ? 1 : count + 1);
    }
}
```

---

**Bonnes pratiques**
- Toujours versionner les schémas Avro.
- Utiliser le Schema Registry en production.
- Privilégier Avro pour la performance et la robustesse.
- Utiliser KTable pour les agrégations et les jointures.
- Utiliser les State Stores pour les besoins d'état local.

**Troubleshooting**
- `MissingSourceTopicException` : le topic source n'existe pas.
- `MismatchedInputException` : problème de sérialisation/désérialisation (vérifier le format attendu).
- Problème de dépendance Confluent : ajouter le repository Confluent dans le `pom.xml`.

---

**Ressources utiles**
- [Documentation Kafka](https://kafka.apache.org/documentation/)
- [Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
- [Spring Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [Kafka Streams](https://kafka.apache.org/documentation/streams/)
