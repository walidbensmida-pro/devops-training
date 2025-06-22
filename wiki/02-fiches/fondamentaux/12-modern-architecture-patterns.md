# Architecture Moderne : Patterns et Best Practices

📑 **Sommaire**
1. [Styles d'Architecture](#1-styles-darchitecture)
2. [Patterns modernes](#2-patterns-modernes)
3. [Best Practices](#3-best-practices)
4. [Questions d'entretien](#4-questions-dentretien)
5. [Exemples pratiques](#5-exemples-pratiques)

## 1. Styles d'Architecture

### Monolithique
- Un seul déploiement
- Simple à développer et débugger
- Difficile à scaler
- Adapté aux petites applications

### Microservices
- Services indépendants
- Scalabilité fine
- Complexité distribuée
- Adapté aux grandes équipes

### Event-Driven
- Communication asynchrone
- Découplage fort
- Scalabilité naturelle
- Complexité de monitoring

## 2. Patterns Essentiels

### API Gateway
```yaml
# Exemple avec Spring Cloud Gateway
spring:
  cloud:
    gateway:
      routes:
        - id: user_service
          uri: lb://user-service
          predicates:
            - Path=/users/**
```

### Circuit Breaker
```java
@CircuitBreaker(name = "userService")
public User getUser(String id) {
    return userClient.getUser(id);
}
```

### Service Discovery
```yaml
# Exemple avec Eureka
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

## 3. Communication Inter-Services

### Synchrone (REST)
- Simple à implémenter
- Couplage temporel
- Latence en cascade
- OpenAPI/Swagger pour documentation

### Asynchrone (Events)
- Découplé
- Scalable
- Complexe à débugger
- Kafka/RabbitMQ comme broker

### GraphQL
- Requêtes flexibles
- Évite l'over-fetching
- Learning curve plus forte

## 4. Persistence Patterns

### CQRS
- Séparation lecture/écriture
- Modèles optimisés
- Complexité accrue
- Cohérence éventuelle

### Event Sourcing
- Audit complet
- Reconstruction d'états
- Performance en lecture
- Volume de données

## 5. Resilience Patterns

### Retry Pattern
```java
@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
public void operation() {
    // Opération qui peut échouer
}
```

### Bulkhead Pattern
```java
@Bulkhead(name = "userService", fallbackMethod = "fallback")
public User getUser(String id) {
    return userClient.getUser(id);
}
```

### Cache-Aside
```java
@Cacheable(value = "users", key = "#id")
public User getUser(String id) {
    return userRepository.findById(id);
}
```

## 6. Questions d'Entretien Classiques

### Sur l'Architecture
Q: Monolithe vs Microservices ?
R: Dépend du contexte :
- Taille de l'équipe
- Besoins en scalabilité
- Time-to-market
- Complexité acceptable

### Sur la Communication
Q: REST vs Events ?
R: Trade-offs :
- Synchrone vs Asynchrone
- Couplage vs Indépendance
- Simplicité vs Scalabilité

### Sur la Persistance
Q: CQRS, quand l'utiliser ?
R: Considérer quand :
- Lectures/écritures asymétriques
- Besoins de scaling différents
- Modèles de données distincts

## 7. Points à Connaître

### Monitoring
- Distributed tracing (Jaeger/Zipkin)
- Metrics (Prometheus)
- Logs centralisés (ELK)
- Health checks

### Security
- OAuth2/OIDC
- API Gateway security
- Service-to-service auth
- Secret management

### Deployment
- Blue/Green
- Canary releases
- Feature toggles
- Infrastructure as Code

## 8. Best Practices

### À Faire
- API versioning
- Circuit breakers
- Rate limiting
- Documentation as code
- Tests d'intégration
- Monitoring complet

### À Éviter
- Couplage synchrone excessif
- Transactions distribuées
- Single point of failure
- Configuration en dur

## 9. Toolkit Modern Java

### Frameworks
- Spring Boot
- Spring Cloud
- Quarkus
- Micronaut

### Outils
- Docker
- Kubernetes
- Terraform
- Prometheus/Grafana

### Testing
- TestContainers
- Gatling
- Chaos Monkey
- Contract Testing
