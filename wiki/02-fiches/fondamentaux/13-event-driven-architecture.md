# Architecture événementielle pour les pressés

📑 **Sommaire**
1. [Concepts Clés (FAQ entretien)](#1-concepts-clés-faq-entretien)
2. [Patterns Essentiels](#2-patterns-essentiels)
3. [Exemples de code](#3-exemples-de-code)
4. [Avantages et inconvénients](#4-avantages-et-inconvénients)
5. [Questions d'entretien](#5-questions-dentretien)

## 1. Concepts Clés (FAQ entretien)

### Event-Driven c'est quoi ?
- Architecture où les composants communiquent via des événements
- Découplage fort entre producteurs et consommateurs
- Asynchrone par nature
- Scalabilité et résilience améliorées

### Patterns Essentiels
- **Event Sourcing** : 
  - Stocke l'état comme séquence d'événements
  - Permet de reconstruire l'état à tout moment
  - Parfait pour l'audit et le debug

```java
// Exemple Event Sourcing simple
public class AccountAggregate {
    private BigDecimal balance;
    private List<AccountEvent> events = new ArrayList<>();

    public void deposit(BigDecimal amount) {
        events.add(new DepositEvent(amount));
        apply(events.get(events.size() - 1));
    }

    private void apply(AccountEvent event) {
        if (event instanceof DepositEvent) {
            this.balance = this.balance.add(((DepositEvent) event).getAmount());
        }
    }
}
```

- **CQRS** (Command Query Responsibility Segregation) :
  - Sépare les opérations de lecture et d'écriture
  - Permet d'optimiser chaque côté indépendamment
  - Souvent utilisé avec Event Sourcing

```java
// Côté Command (écriture)
@Service
public class OrderCommandService {
    public void createOrder(CreateOrderCommand cmd) {
        // Valide, crée l'ordre, émet l'événement
        eventBus.publish(new OrderCreatedEvent(cmd.getOrderId()));
    }
}

// Côté Query (lecture)
@Service
public class OrderQueryService {
    public OrderDTO getOrder(String orderId) {
        // Lit depuis une vue optimisée pour la lecture
        return orderReadRepository.findById(orderId);
    }
}
```

## 2. Implementation Pratique

### Avec Spring Cloud Stream + Kafka
```java
@Service
public class OrderProcessor {
    @Bean
    public Consumer<Message<OrderEvent>> processOrder() {
        return message -> {
            OrderEvent event = message.getPayload();
            // Traitement asynchrone
        };
    }
}
```

### Avec Spring Events (in-memory)
```java
@Service
public class OrderService {
    private final ApplicationEventPublisher publisher;

    public void createOrder(Order order) {
        // Logique métier
        publisher.publishEvent(new OrderCreatedEvent(order));
    }
}

@Component
public class OrderEventListener {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Réaction asynchrone
    }
}
```

## 3. Questions d'entretien typiques

### Q: Différence entre Event-Driven et Request-Response ?
R: 
- Event-Driven : asynchrone, découplé, meilleure scalabilité
- Request-Response : synchrone, couplé, plus simple à débugger
- Choix selon besoins : temps réel vs cohérence forte

### Q: Quand utiliser Event Sourcing ?
R: 
- Besoin d'audit trail complet
- Reconstruction d'états passés nécessaire
- Débit d'écriture > débit de lecture

### Q: Challenges de l'Event-Driven ?
- Complexité de débogage
- Cohérence éventuelle à gérer
- Ordre des événements à garantir
- Idempotence à assurer

## 4. Patterns avancés

### Saga Pattern
Pour transactions distribuées :
```java
@Service
public class OrderSaga {
    public void startOrderSaga(CreateOrderCommand cmd) {
        try {
            // 1. Créer ordre
            // 2. Vérifier paiement
            // 3. Réserver stock
            // 4. Confirmer
        } catch (Exception e) {
            // Compensation : annuler les étapes précédentes
        }
    }
}
```

### Outbox Pattern
Pour fiabilité des événements :
```java
@Transactional
public void createOrder(Order order) {
    // 1. Sauver l'ordre
    orderRepository.save(order);
    
    // 2. Sauver l'événement dans l'outbox
    outboxRepository.save(new OutboxEvent(
        "OrderCreated", 
        order.getId()
    ));
}
```

## 5. Best Practices & Pièges

### À faire
- Toujours définir une stratégie de versioning des événements
- Implémenter l'idempotence côté consumer
- Monitorer la latence et les dead letters
- Avoir un bon système de tracking des événements

### À éviter
- Trop d'événements différents
- Logique métier dans les événements
- Dépendances synchrones dans un flow événementiel
- Oublier la gestion des erreurs

## 6. Points différenciants en entretien

1. Parler de votre expérience avec :
   - Gestion des retries
   - Monitoring des événements
   - Tests des flows événementiels
   - Stratégies de versioning

2. Mentionner les outils :
   - Kafka / RabbitMQ
   - Debezium pour CDC
   - Spring Cloud Stream
   - OpenTelemetry pour le tracing

3. Montrer que vous comprenez :
   - Les compromis CAP
   - La cohérence éventuelle
   - Les patterns de compensation
   - Le monitoring distribué
