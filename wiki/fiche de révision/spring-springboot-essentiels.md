# Fiche de révision : Spring & Spring Boot (essentiels)

---

## Concepts clés

- **IOC/DI (Inversion of Control / Dependency Injection)** :
  - Permet d’injecter les dépendances automatiquement, favorise le découplage et la testabilité.
  - Exemple : `@Autowired` injecte un bean dans une classe.
  - Constructor injection recommandée pour l’immuabilité et les tests.

- **Beans & Scopes** :
  - Un bean est un objet géré par le conteneur Spring. Scopes : singleton (par défaut), prototype, request, session.
  - Cycle de vie : création, injection, destruction (annotations `@PostConstruct`, `@PreDestroy`).

- **Configuration** :
  - `@Configuration`, `@Bean` pour déclarer des beans manuellement.
  - `@Value` pour injecter une valeur de configuration.
  - `@Profile` pour gérer des configs différentes selon l’environnement.
  - Externalisation de la config : `application.yml`, variables d’environnement, vault.

- **Spring Boot** :
  - Démarrage rapide, starters (`spring-boot-starter-web`, etc.), autoconfiguration, Actuator pour la supervision.
  - Fichier `application.yml` ou `application.properties` pour la config.
  - DevTools pour le rechargement à chaud.

- **REST** :
  - `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping` pour exposer des APIs REST.
  - Sérialisation/désérialisation automatique avec Jackson.
  - Validation des entrées avec `@Valid` et `@RequestBody`.
  - Gestion des exceptions : `@ControllerAdvice`, `@ExceptionHandler`.

- **Sécurité** :
  - Spring Security pour l’authentification/autorisation, gestion des rôles, filtres, JWT, OAuth2.
  - Sécurisation des endpoints, CORS, CSRF, gestion des sessions.

- **Tests** :
  - `@SpringBootTest` pour tester l’application complète, MockMvc pour tester les contrôleurs, profils de test.
  - Tests unitaires avec JUnit, Mockito, tests d’intégration avec H2.

- **Data/JPA** :
  - Spring Data JPA pour l’accès BDD, repositories, requêtes automatiques, pagination, projections.
  - Transactions avec `@Transactional`.

- **Observabilité** :
  - Spring Boot Actuator pour exposer `/actuator/health`, `/actuator/metrics`, `/actuator/info`.
  - Intégration Prometheus/Grafana possible.

---

## Astuces entretien & réponses types

- **Différence entre @Component, @Service, @Repository** :
  - `@Component` = générique, `@Service` = logique métier, `@Repository` = accès BDD (et gestion des exceptions).

- **À quoi sert l’annotation @Autowired ?**
  - À injecter automatiquement une dépendance (bean) dans une classe. Préférer l’injection par constructeur.

- **Comment sécuriser une API REST avec Spring Boot ?**
  - Ajouter Spring Security, configurer les endpoints protégés, utiliser JWT ou OAuth2 pour l’authentification, gérer les CORS.

- **Démarrer un projet Spring Boot en 2 minutes** :
  - Utiliser [start.spring.io](https://start.spring.io), choisir les starters, générer le projet, lancer avec `mvn spring-boot:run`.

- **Principaux starters Spring Boot** :
  - `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-test`, `spring-boot-starter-actuator`.

- **À quoi sert l’Actuator ?**
  - À exposer des endpoints pour monitorer l’état de l’application (santé, métriques, info, readiness/liveness).

- **Exemple de contrôleur REST minimal** :

  ```java
  @RestController
  public class HelloController {
    @GetMapping("/hello")
    public String hello() { return "Hello"; }
  }
  ```

- **Exemple de repository Spring Data JPA** :

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
  }
  ```

- **Gestion d’erreur globale** :

  ```java
  @ControllerAdvice
  public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
      return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
    }
  }
  ```

---

## Questions d'entretien & cas pratiques (avec réponses synthétiques)

- **À quoi sert l’annotation @Autowired ?**
  - À injecter automatiquement une dépendance (bean) dans une classe. Préférer l’injection par constructeur.

- **Comment sécuriser une API REST avec Spring Boot ?**
  - Ajouter Spring Security, configurer les endpoints protégés, utiliser JWT ou OAuth2, gérer les CORS.

- **Quelle différence entre @RestController et @Controller ?**
  - `@RestController` = retour direct JSON, `@Controller` = retour d’une vue (template HTML).

- **Comment injecter une valeur de configuration dans un bean ?**
  - Avec `@Value("${ma.config}")` ou via un `@ConfigurationProperties`.

- **Comment tester un contrôleur Spring Boot ?**
  - Avec MockMvc ou un test d’intégration annoté `@SpringBootTest`.

- **Comment activer un profil spécifique ?**
  - En passant `--spring.profiles.active=dev` au lancement ou via la config.

- **Comment brancher une base de données à Spring Boot ?**
  - Ajouter le starter JPA, configurer la datasource dans `application.yml`, utiliser les repositories Spring Data.

- **À quoi sert @Transactional ?**
  - À exécuter une méthode dans une transaction (commit/rollback automatique).

- **Comment exposer des métriques de santé ?**
  - Avec Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`).

- **Comment gérer la validation des entrées d’une API ?**
  - Utiliser `@Valid` sur les DTO et annoter les champs (`@NotNull`, `@Size`, etc.), gérer les erreurs avec `@ControllerAdvice`.

- **Comment gérer la configuration selon l’environnement ?**
  - Utiliser les profils Spring (`@Profile`), des fichiers `application-dev.yml`, `application-prod.yml`, et des variables d’environnement.

- **Comment faire un test d’intégration avec une BDD en mémoire ?**
  - Ajouter H2 en dépendance, configurer le profil de test, utiliser `@DataJpaTest` ou `@SpringBootTest`.

- **Comment documenter une API Spring Boot ?**
  - Utiliser Springdoc OpenAPI ou Swagger pour générer la doc automatiquement.

---

## Comment fonctionne Spring & Spring Boot ? (pédagogique, simple)

### 1. Démarrage d'une appli Spring Boot

- Quand tu lances l'appli (main ou `mvn spring-boot:run`), Spring Boot :
  1. Charge le fichier `application.yml` ou `application.properties` (config globale).
  2. Scanne tout le code à la recherche d'annotations spéciales (`@Component`, `@Service`, `@Repository`, `@Controller`, etc.).
  3. Crée un "contexte" (container) où il va gérer tous les objets utiles (les "beans").
  4. Instancie les beans, injecte les dépendances automatiquement (DI), configure les objets selon le YAML.
  5. Démarre le serveur web (Tomcat intégré) si besoin.

### 2. Les beans et l'injection de dépendance

- Un **bean** = un objet géré par Spring (créé, configuré, injecté automatiquement).
- Pour qu'une classe soit un bean, il faut une annotation comme :
  - `@Component` (générique)
  - `@Service` (logique métier)
  - `@Repository` (accès BDD)
  - `@Controller` ou `@RestController` (web/API)
- **Injection de dépendance** :
  - Si une classe a besoin d'une autre, Spring l'injecte automatiquement (plus besoin de `new`).
  - On utilise `@Autowired` (ou mieux : l'injection par constructeur).

**Exemple** :

```java
@Service
public class UserService {
  private final UserRepository repo;
  public UserService(UserRepository repo) { this.repo = repo; }
}
```

- Ici, `UserRepository` est injecté automatiquement dans `UserService`.

### 3. Le scan automatique

- Spring scanne tous les packages à partir du package principal (là où il y a `@SpringBootApplication`).
- Il cherche toutes les classes annotées pour les enregistrer comme beans.
- Si tu oublies l'annotation, la classe n'est pas gérée par Spring (pas d'injection possible, pas de config automatique).

### 4. Le fichier `application.yml`

- Sert à configurer l'appli (port, BDD, sécurité, profils, etc.).
- Les valeurs peuvent être injectées dans les beans avec `@Value` ou `@ConfigurationProperties`.

**Exemple** :

```yaml
server:
  port: 8081
ma:
  config: valeur
```

```java
@Value("${ma.config}")
private String maConfig;
```

### 5. À quoi servent les annotations ?

- Elles disent à Spring ce qu'il doit gérer et comment :
  - `@Component`, `@Service`, `@Repository`, `@Controller` : déclarer un bean.
  - `@Autowired` : demander une injection automatique.
  - `@Configuration`, `@Bean` : configurer des beans manuellement.
  - `@Value` : injecter une valeur de config.
  - `@Profile` : activer selon l'environnement.
  - `@RestController`, `@GetMapping`, etc. : exposer des APIs.

**Si tu ne mets pas l'annotation** :
- La classe n'est pas gérée par Spring, donc pas d'injection, pas de config automatique, pas d'API exposée.

### 6. Résumé oral pour l'entretien

> "Spring Boot démarre en scannant le code pour trouver les classes annotées, crée un container d'objets (beans), injecte automatiquement les dépendances, configure tout selon le YAML, et lance le serveur web. Les annotations servent à dire à Spring quoi gérer. Si on ne les met pas, la classe n'est pas prise en charge."

---

## Comment fonctionne Spring Boot ? (démarrage, cycle de vie, super simple)

### 1. Démarrage d’une appli Spring Boot

- Tu lances la classe avec `@SpringBootApplication` (main).
- Spring Boot :
  1. Charge la config (`application.yml` ou `.properties`).
  2. Scanne le code pour trouver les classes annotées (beans).
  3. Crée le "contexte" Spring (container d’objets/beans).
  4. Instancie et configure tous les beans (selon les annotations et le YAML).
  5. Démarre le serveur web intégré (Tomcat, Jetty, etc.) si besoin.
  6. Affiche la bannière Spring Boot et l’URL d’accès.

### 2. Ce que fait Spring Boot pour toi

- Configure automatiquement plein de choses (BDD, web, sécurité…) selon les dépendances présentes.
- Fournit des "starters" (dépendances prêtes à l’emploi) pour chaque usage (`spring-boot-starter-web`, etc.).
- Gère le cycle de vie des beans (création, injection, destruction).
- Permet de surcharger la config via YAML, variables d’env, profils.
- Expose des endpoints d’admin avec Actuator (`/actuator/health`, etc.).

### 3. Cycle de vie simplifié

1. **Initialisation** : lecture de la config, scan des beans.
2. **Création des beans** : instanciation, injection des dépendances.
3. **Configuration** : application des valeurs du YAML, des profils, etc.
4. **Démarrage du serveur** : si web, Tomcat démarre et écoute sur le port défini.
5. **Application prête** : endpoints REST, services, etc. sont accessibles.

### 4. Pourquoi c’est simple ?

- Pas besoin de tout configurer à la main : Spring Boot "devine" ce que tu veux selon les starters et le YAML.
- Un seul point d’entrée (`@SpringBootApplication`), tout est auto-scanné.
- Tu ajoutes des classes annotées, Spring Boot les gère tout seul.

### 5. Résumé oral pour l’entretien

> "Spring Boot démarre en lisant la config, scanne le code pour trouver les classes à gérer, crée tous les objets nécessaires, configure tout automatiquement, puis lance le serveur web. Grâce aux starters et au YAML, on a très peu de config à écrire soi-même."
