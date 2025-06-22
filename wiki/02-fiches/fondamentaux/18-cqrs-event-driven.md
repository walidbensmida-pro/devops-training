# CQRS & Event Driven : Le Lien et l’Implémentation (Java/Spring)

## 1. CQRS : C’est quoi ?

- **CQRS** = Command Query Responsibility Segregation.
- On sépare les opérations de **commande** (écriture, modification) des **requêtes** (lecture).
- Avantage : chaque côté peut évoluer indépendamment (scalabilité, performance, sécurité).

## 2. Event Driven : C’est quoi ?

- **Event Driven** = architecture pilotée par les événements.
- Les changements d’état sont communiqués via des **événements** (ex : "CommandeCréée").
- Les composants réagissent aux événements (asynchrone, découplé).

## 3. Le lien entre CQRS et Event Driven

- **CQRS** et **Event Driven** sont souvent utilisés ensemble :
  - Les commandes modifient l’état (write model).
  - Après modification, un **événement** est publié (ex : "CommandeCréée").
  - Les listeners (ou projections) consomment ces événements pour mettre à jour le modèle de lecture (read model).
- Cela permet d’avoir des lectures optimisées, à jour, et découplées du modèle d’écriture.

## 4. Exemple de flux

1. **Commande** : un utilisateur crée une commande (`CreateOrderCommand`).
2. **Write Model** : la commande est traitée, la commande est enregistrée.
3. **Événement** : un événement `OrderCreatedEvent` est publié.
4. **Read Model** : un listener reçoit l’événement et met à jour une vue de lecture (ex : une table optimisée pour la recherche).
5. **Lecture** : les requêtes lisent directement dans le read model.

## 5. Implémentation simple avec Spring Boot & Kafka

### a. Commande (écriture)

```java
@PostMapping("/commandes")
public void creerCommande(@RequestBody CommandeDto dto) {
    // 1. Traiter la commande (write model)
    Commande commande = commandeService.creer(dto);
    // 2. Publier un événement
    kafkaTemplate.send("commande-events", new CommandeCreeEvent(commande.getId(), ...));
}
```

### b. Listener d’événements (lecture)

```java
@KafkaListener(topics = "commande-events")
public void onCommandeCree(CommandeCreeEvent event) {
    // Mettre à jour le read model (ex: table de recherche, cache, index)
    readModelService.ajouterCommande(event);
}
```

### c. Lecture optimisée

```java
@GetMapping("/commandes")
public List<CommandeReadModel> listerCommandes() {
    return readModelService.listerToutes();
}
```

## 6. Points clés

- **Découplage** : le write model et le read model sont indépendants.
- **Scalabilité** : chaque modèle peut être optimisé/scalé séparément.
- **Eventual Consistency** : la lecture peut être légèrement en retard (asynchrone).
- **Event Sourcing** (optionnel) : on peut stocker tous les événements pour reconstruire l’état.

## 7. À retenir

- CQRS = séparer écriture et lecture.
- Event Driven = communiquer les changements par événements.
- Ensemble, ils permettent des architectures robustes, scalables et adaptées au cloud/microservices.

---

**Ressources utiles** :
- [CQRS Explained (martinfowler.com)](https://martinfowler.com/bliki/CQRS.html)
- [Spring Event-Driven Architecture](https://docs.spring.io/spring-framework/reference/integration/events.html)
- [Spring Boot + Kafka Example](https://spring.io/guides/gs/messaging-kafka/)