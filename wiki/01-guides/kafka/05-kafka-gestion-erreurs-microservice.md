# Kafka & Microservices : Gérer les Erreurs (Spring Boot, Kubernetes)

## 1. Pourquoi la gestion des erreurs est cruciale ?

- Un message mal formé ou une panne peut bloquer tout un flux Kafka.
- En microservices, une erreur non gérée peut impacter plusieurs applications.
- Sur Kubernetes, il faut éviter que les pods crashent en boucle à cause d'un seul message.

---

## 2. Types d'erreurs courantes

- **Erreur de désérialisation** (mauvais format, schéma incompatible)
- **Erreur métier** (validation, données incohérentes)
- **Erreur technique** (base de données, réseau, timeout)
- **Erreur temporaire** (service indisponible, latence)

---

## 3. Stratégies de gestion des erreurs

### a. Retry (réessai automatique)

- **Spring Kafka** permet de réessayer automatiquement un message en cas d'échec.
- Utilise les propriétés `maxAttempts`, `backOff`, etc.

```yaml
spring:
  kafka:
    listener:
      retry:
        enabled: true
        max-attempts: 3
        backoff:
          interval: 2000 # ms
```

### b. Dead Letter Topic (DLT)

- Si un message échoue après tous les essais, il est envoyé dans un topic spécial (DLT).
- Permet d'isoler les messages "poison" sans bloquer le flux principal.

```java
@KafkaListener(topics = "mon-topic", errorHandler = "customErrorHandler")
public void listen(String message) { ... }

@Bean
public DeadLetterPublishingRecoverer recoverer(KafkaTemplate<Object, Object> template) {
    return new DeadLetterPublishingRecoverer(template);
}
```

- Avec Spring Boot 2.7+ et `@RetryableTopic`, c'est encore plus simple :

```java
@RetryableTopic(
  attempts = "3",
  backoff = @Backoff(delay = 2000),
  dltTopicSuffix = ".DLT"
)
@KafkaListener(topics = "mon-topic")
public void listen(String message) { ... }
```

### c. Gestion des erreurs de désérialisation

- Utilise les ErrorHandlingDeserializer de Spring Kafka pour éviter le crash du consumer.

```yaml
spring:
  kafka:
    consumer:
      value-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
      properties:
        spring.deserializer.value.delegate.class: org.apache.kafka.common.serialization.StringDeserializer
```

- Les erreurs sont envoyées dans un DLT ou loguées.

### d. Logging & Monitoring

- Loggue toutes les erreurs (Stacktrace, clé, offset, etc.).
- Utilise des outils comme Prometheus, Grafana, ELK pour surveiller les erreurs.
- Sur Kubernetes, configure les probes (readiness/liveness) pour éviter les redémarrages en boucle.

---

## 4. Bonnes pratiques Spring Boot + Kubernetes

- **Toujours utiliser un DLT** pour ne jamais bloquer le flux.
- **Limiter les retries** pour éviter de saturer le cluster.
- **Monitorer les DLT** : traiter ou alerter sur les messages en erreur.
- **Externaliser la configuration** (`application.yml`, variables d'env Kubernetes).
- **Utiliser les probes** pour que les pods ne redémarrent pas sur une simple erreur métier.
- **Centraliser les logs** (ELK, Loki, etc.) pour faciliter l'analyse.

---

## 5. Exemple complet (Spring Boot)

```java
@Slf4j
@Service
public class MonConsumer {

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000), dltTopicSuffix = ".DLT")
    @KafkaListener(topics = "commande")
    public void consommeCommande(String message) {
        try {
            // Traitement métier
        } catch (Exception e) {
            log.error("Erreur lors du traitement du message: {}", message, e);
            throw e; // Pour déclencher le retry/DLT
        }
    }
}
```

---

## 6. Sur Kubernetes

- **Variables d'environnement** pour configurer Kafka (brokers, topics, retries, etc.).
- **Probes** pour éviter les redémarrages inutiles :
  - Readiness probe : vérifie la connexion à Kafka.
  - Liveness probe : vérifie que l'app ne boucle pas sur une erreur fatale.
- **Ressources** : allouer assez de mémoire pour éviter les OOM lors de gros volumes d'erreurs.

---

## 7. À retenir

- Ne jamais bloquer le flux Kafka à cause d'un seul message.
- Toujours isoler les messages en erreur (DLT).
- Monitorer et traiter les DLT régulièrement.
- Adapter la stratégie de retry selon le type d'erreur (métier vs technique).
- Sur Kubernetes, bien configurer les probes et la gestion des logs.

---

**Ressources utiles** :
- [Spring Kafka - Error Handling](https://docs.spring.io/spring-kafka/docs/current/reference/html/#error-handling)
- [Spring Kafka - Retryable Topics](https://docs.spring.io/spring-kafka/docs/current/reference/html/#retryable-topic)
- [Kafka Streams Error Handling](https://kafka.apache.org/34/documentation/streams/developer-guide/handling-errors)
- [Kubernetes Probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)
