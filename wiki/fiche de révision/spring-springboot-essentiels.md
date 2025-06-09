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
