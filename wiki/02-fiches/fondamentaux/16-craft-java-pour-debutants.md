# Software Craft Java Expliqué Simplement

📑 **Sommaire**
1. [Les Principes du Craft en Analogies](#1-les-principes-du-craft-en-analogies)
   - [Le Code Propre (Clean Code)](#le-code-propre--comme-une-cuisine-rangée-)
   - [Les Tests Automatisés (TDD)](#les-tests-automatisés--comme-un-filet-de-sécurité-)
   - [La Simplicité (KISS)](#la-simplicité--comme-une-recette-de-cuisine-)
2. [Les Pratiques Clés](#2-les-pratiques-clés-)
3. [Exemples Concrets en Java](#3-exemples-concrets-en-java-)
4. [Les Pièges à Éviter](#4-les-pièges-à-éviter-)
5. [Questions d'Entretien](#5-questions-dentretien-version-simple-)
6. [Conseils Pratiques](#6-conseils-pratiques-)

## 1. Les Principes du Craft en Analogies

### Le Code Propre : Comme une Cuisine Rangée 🧹
Imagine ta cuisine :
- Chaque ustensile a sa place
- Les ingrédients sont étiquetés
- Le plan de travail est propre
- Facile de retrouver ce qu'on cherche
- Plus agréable pour cuisiner ensemble

**En code, ça donne quoi ?**
- Des noms de variables et méthodes explicites
- Des fonctions courtes qui font une seule chose
- Peu de commentaires car le code parle de lui-même
- Organisation logique des classes

```java
// Cuisine mal rangée (mauvais code)
public void a() {
    int x = getData(true);
    if (x > 0) {
        processData(x, 0, false);
    }
}

// Cuisine bien rangée (bon code)
public void traiterCommandeClient() {
    int idClient = recupererIdClient(clientActif);
    if (idClient > 0) {
        envoyerConfirmationCommande(idClient);
    }
}
```

### Les Tests Automatisés : Comme un Filet de Sécurité 🧪
Comme un trapéziste au cirque :
- Tu peux tenter des figures complexes
- Tu n'as pas peur de tomber
- Tu peux améliorer ton numéro en toute sécurité
- Le public (utilisateurs) est rassuré

**En code, ça donne quoi ?**
- Tests unitaires pour les petites fonctions
- Tests d'intégration pour les interactions
- Tests automatiques qui s'exécutent à chaque changement
- Refactoring sans stress

```java
@Test
public void calculPrixTotal_AvecRemise_AppliqueReductionCorrectement() {
    // Arrange
    Panier panier = new Panier();
    panier.ajouterArticle(new Article("Livre", 20.0));
    
    // Act
    double prixFinal = panier.calculerPrixTotal(10); // 10% de remise
    
    // Assert
    assertEquals(18.0, prixFinal, 0.01);
}
```

### La Simplicité : Comme une Recette de Cuisine 📝
Comme une bonne recette :
- Instructions claires et directes
- Pas d'étapes inutiles
- Facile à suivre pour un débutant
- Peut être améliorée progressivement

**En code, ça donne quoi ?**
- Solutions les plus simples possibles
- Éviter la sur-ingénierie
- "You Aren't Gonna Need It" (YAGNI)
- Traiter les problèmes un par un

```java
// Recette compliquée (trop d'abstraction)
public interface DataProcessor<T extends Processable, R extends Result> {
    R process(T input, ProcessingContext<T, R> context);
}

// Recette simple (direct au but)
public interface CalculateurPrix {
    double calculerPrix(Produit produit);
}
```

## 2. Les Pratiques Clés 🔧

### Refactoring Régulier
- Comme ranger sa cuisine régulièrement
- Petites améliorations constantes
- Ne pas attendre que tout soit en désordre
- "Boy Scout Rule" : laisse le code plus propre qu'à ton arrivée

### Pair Programming
- Comme cuisiner à deux
- Un qui code, un qui réfléchit
- Partage de connaissances
- Moins d'erreurs, plus de créativité

### Code Reviews
- Comme faire goûter ton plat avant de servir
- Feedback constructif
- Apprentissage mutuel
- Qualité collective, pas individuelle

### Dette Technique
- Comme une vaisselle qu'on remet à plus tard
- Acceptable si temporaire et planifiée
- Doit être remboursée rapidement
- Devient problématique si ignorée

## 2bis. Pratiques de Craft avec Java Spring 🌱

### 1. Injection de Dépendances (DI)
- Utilise l'annotation `@Autowired` ou l'injection par constructeur pour favoriser le découplage.
- Privilégie l'injection par constructeur pour faciliter les tests et la lisibilité.

```java
// Mauvais : injection par champ (difficile à tester)
@Autowired
private ServiceClient serviceClient;

// Bon : injection par constructeur
private final ServiceClient serviceClient;

public CommandeController(ServiceClient serviceClient) {
    this.serviceClient = serviceClient;
}
```

### 2. Utilisation des Profils Spring
- Sépare les configurations (dev, test, prod) avec `@Profile`.
- Permet d'adapter le comportement selon l'environnement.

```java
@Profile("dev")
@Configuration
public class DevConfig {
    // Beans spécifiques au développement
}
```

### 3. Gestion des Exceptions Centralisée
- Utilise `@ControllerAdvice` pour centraliser la gestion des erreurs HTTP.
- Permet d'éviter la duplication de code de gestion d'erreur.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

### 4. Validation des Entrées
- Utilise les annotations de validation (`@Valid`, `@NotNull`, etc.) sur les DTOs.
- Permet de garantir la qualité des données dès l'entrée.

```java
public class CommandeDto {
    @NotNull
    private Long idProduit;

    @Min(1)
    private int quantite;
}
```

### 5. Respect du Principe de Responsabilité Unique (SRP)
- Un contrôleur = une ressource métier.
- Un service = une logique métier.
- Un repository = l'accès aux données.

```java
// ...exemple de séparation des couches...
```

### 6. Utilisation des Tests Spring
- Utilise `@SpringBootTest` pour les tests d'intégration.
- Utilise `@WebMvcTest` pour tester les contrôleurs sans charger tout le contexte.

```java
@WebMvcTest(CommandeController.class)
class CommandeControllerTest {
    // ...tests du contrôleur...
}
```

### 7. Configuration Externalisée
- Place les paramètres dans `application.properties` ou `application.yml`.
- Utilise `@Value` ou `@ConfigurationProperties` pour injecter la configuration.

```java
@Value("${app.taux-tva}")
private double tauxTva;
```

### 8. Utilisation des DTOs et Mappers
- Ne retourne jamais directement les entités JPA dans les API.
- Utilise des DTOs et des mappers (ex: MapStruct) pour séparer la couche API de la couche données.

```java
@Mapper(componentModel = "spring")
public interface CommandeMapper {
    CommandeDto toDto(Commande entity);
    Commande toEntity(CommandeDto dto);
}
```

### 9. Sécurité par défaut
- Active Spring Security même pour les projets internes.
- Privilégie la configuration explicite des accès.

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ...configuration des accès...
}
```

### 10. Documentation Automatique
- Utilise Swagger/OpenAPI (`springdoc-openapi`) pour documenter automatiquement les endpoints REST.

```java
@OpenAPIDefinition(
    info = @Info(title = "API Commandes", version = "1.0")
)
@SpringBootApplication
public class Application { }
```

---

**Résumé** :  
Le craft avec Spring, c'est appliquer les principes du clean code, du découplage, des tests et de la simplicité, tout en profitant des outils Spring pour structurer, sécuriser et documenter ton application.

## 3. Exemples Concrets en Java 💻

### Clean Code en Action
```java
// Avant
public List<Object> getItems() {
    List<Object> lst = new ArrayList<>();
    for (int i = 0; i < this.a.size(); i++) {
        if (this.a.get(i).getStatus() == 1) {
            lst.add(this.a.get(i));
        }
    }
    return lst;
}

// Après
public List<Produit> recupererProduitsActifs() {
    return produits.stream()
            .filter(Produit::estActif)
            .collect(Collectors.toList());
}
```

### SOLID en Pratique (Single Responsibility)
```java
// Avant: classe qui fait trop de choses
public class GestionCommande {
    public void traiterCommande(Commande cmd) {
        // Validation
        // Traitement business
        // Envoi email
        // Écriture logs
    }
}

// Après: responsabilités séparées
public class ValidateurCommande {
    public boolean estValide(Commande cmd) { /* ... */ }
}

public class ProcesseurCommande {
    public void traiter(Commande cmd) { /* ... */ }
}

public class NotificateurCommande {
    public void envoyerConfirmation(Commande cmd) { /* ... */ }
}
```

### Patterns Utiles
```java
// Builder Pattern: création d'objets complexes
Utilisateur user = new UtilisateurBuilder()
    .avecNom("Martin")
    .avecEmail("martin@exemple.com")
    .avecRoles(List.of(Role.CLIENT))
    .construire();

// Strategy Pattern: algorithmes interchangeables
public interface StrategiePaiement {
    void effectuerPaiement(double montant);
}

public class PaiementCarte implements StrategiePaiement {
    @Override
    public void effectuerPaiement(double montant) { /* ... */ }
}

public class PaiementPaypal implements StrategiePaiement {
    @Override
    public void effectuerPaiement(double montant) { /* ... */ }
}
```

## 4. Les Pièges à Éviter 🚨

### Sur-ingénierie
❌ Créer des abstractions "au cas où"
❌ Patterns partout même quand inutiles
❌ Classes/interfaces pour tout

✅ Commencer simple
✅ Refactoriser quand nécessaire
✅ "Make it work, make it right, make it fast"

### Absence de Tests
❌ "Pas le temps" de tester
❌ Tests rédigés après coup
❌ Tests qui testent l'implémentation, pas le comportement

✅ TDD quand possible
✅ Tests comme documentation
✅ Automatisation des tests

### Commentaires Excessifs
❌ Commenter chaque ligne
❌ Commentaires qui répètent le code
❌ Commentaires obsolètes

✅ Code auto-documenté
✅ Commentaires pour le "pourquoi", pas le "quoi"
✅ JavaDoc pour les APIs publiques

## 5. Questions d'Entretien (Version Simple) 💡

### Q: Comment sais-tu qu'un code est "propre"?
R: Un code propre est facile à lire, à comprendre et à modifier. Si un nouveau développeur peut comprendre rapidement ce que fait le code sans avoir besoin d'explications, c'est un bon signe. Les noms sont clairs, les fonctions font une seule chose, et la structure est logique.

### Q: Comment abordes-tu le refactoring d'un code existant?
R: 
1. Je m'assure d'avoir des tests
2. Je fais de petits changements à la fois
3. Je teste après chaque changement
4. Je cherche d'abord les "mauvaises odeurs" (code smells)
5. J'applique des patterns et principes comme SOLID

### Q: Pourquoi les tests sont-ils importants?
R: Les tests permettent:
- De vérifier que le code fonctionne correctement
- De refactoriser sans crainte
- De documenter le comportement attendu
- D'identifier les régressions rapidement
- De concevoir de meilleures interfaces (TDD)

### Q: Comment expliques-tu SOLID à un débutant?
R:
- **S**ingle Responsibility: une classe = une seule raison de changer
- **O**pen/Closed: ouvert à l'extension, fermé à la modification
- **L**iskov Substitution: les sous-classes doivent fonctionner comme leurs parents
- **I**nterface Segregation: mieux vaut plusieurs interfaces spécifiques qu'une grosse
- **D**ependency Inversion: dépendre des abstractions, pas des implémentations

### Q: Quels sont les principaux principes à connaître pour un entretien Java/Spring ?
R: Voici les principes essentiels à maîtriser :
- **Clean Code** : code lisible, simple, bien nommé, facile à maintenir.
- **KISS** ("Keep It Simple, Stupid") : privilégier la simplicité, éviter la complexité inutile.
- **DRY** ("Don't Repeat Yourself") : éviter la duplication de code.
- **YAGNI** ("You Aren't Gonna Need It") : ne pas coder ce qui n'est pas nécessaire.
- **SOLID** :
  - **S**ingle Responsibility Principle (SRP) : une classe = une seule responsabilité.
  - **O**pen/Closed Principle : ouvert à l’extension, fermé à la modification.
  - **L**iskov Substitution Principle : les sous-classes doivent pouvoir remplacer la classe mère.
  - **I**nterface Segregation Principle : plusieurs petites interfaces plutôt qu’une grosse.
  - **D**ependency Inversion Principle : dépendre des abstractions, pas des implémentations.
- **TDD** (Test Driven Development) : écrire les tests avant le code.
- **Refactoring** : améliorer le code sans changer son comportement.
- **Separation of Concerns** : séparer les responsabilités (ex: contrôleur, service, repository).
- **Convention over Configuration** : préférer les conventions pour réduire la configuration.
- **Principes Spring** : injection de dépendances, configuration externalisée, gestion des exceptions, validation, sécurité par défaut, etc.

## 6. Conseils Pratiques 🎯

### Pour Débuter
1. Lis "Clean Code" de Robert C. Martin
2. Pratique les katas de code (exercices courts)
3. Fais des revues de code avec des développeurs expérimentés
4. Commence par tester ton code
5. Utilise des outils d'analyse comme SonarQube

### Pour Progresser
1. Enseigne à d'autres (meilleure façon d'apprendre)
2. Participe à des coding dojos
3. Contribue à des projets open source
4. Explore différents paradigmes (fonctionnel, orienté objet)
5. Pratique le refactoring régulièrement

### Pour Réussir en Entretien
1. Prépare des exemples concrets de ton expérience
2. Explique comment tu as amélioré du code existant
3. Montre ta compréhension des compromis (trade-offs)
4. Sois honnête sur tes erreurs et apprentissages
5. Démontre ta passion pour l'amélioration continue
