# Fiche de révision : Tests & Qualité logicielle

---

## Concepts clés

- **Pyramide des tests** :


  ```mermaid
  graph TD
    A[Tests E2E (UI, API, bout en bout) - Peu nombreux, lents, coûteux] 
    B[Tests d'intégration - Plus nombreux, valident l'interaction entre composants] 
    C[Tests unitaires - Très nombreux, rapides, isolés]
    A --> B --> C
  ```

  - Majorité de tests unitaires (base), moins de tests d’intégration (milieu), peu de tests E2E (sommet).

- **Types de tests** :
  - Tests unitaires : vérifient une fonction/classe isolée (ex : JUnit, Mockito).
  - Tests d’intégration : valident l’interaction entre plusieurs composants (ex : base H2, Testcontainers).
  - Tests end-to-end (E2E) : valident le système complet (ex : Selenium, Cypress).
  - Tests de non-régression : garantissent qu’un bug corrigé ne revient pas.
  - Tests de performance : valident la rapidité et la robustesse (JMH, Gatling, JMeter).
  - Tests de mutation : vérifient la pertinence des tests unitaires (ex : PIT).
  - TDD (Test Driven Development) : écrire les tests avant le code.
  - Couverture de code : pourcentage de code exécuté par les tests.

- **Outils** :
  - JUnit (tests unitaires Java), Mockito (mocks), Testcontainers (tests d’intégration avec containers), SonarQube (analyse statique, qualité), JaCoCo (couverture), Gatling/JMeter (perf), PIT (mutation).

- **Mocking** :
  - Simuler des dépendances externes pour isoler le code testé (ex : Mockito, @MockBean).

- **Analyse statique** :
  - Outils comme SonarQube, Checkstyle, SpotBugs pour détecter bugs, code mort, duplications, conventions.

- **CI/CD & automatisation** :
  - Lancer les tests à chaque commit/push (GitHub Actions, Jenkins, GitLab CI).
  - Bloquer le déploiement si les tests échouent.
  - Générer des rapports de tests et de couverture automatiquement.

- **Qualité logicielle** :
  - Revue de code (code review), pair programming, documentation, conventions de nommage, gestion technique de la dette.
  - Détection des code smells, duplications, complexité cyclomatique.

---

## Astuces entretien & réponses types

- **Différence test unitaire / test d’intégration** :
  - Unitaire = une fonction isolée, rapide, sans dépendance externe. Intégration = plusieurs composants ensemble, base de données, réseau, plus lent.

- **À quoi sert SonarQube ?**
  - À analyser la qualité du code (bugs, duplications, conventions, couverture, sécurité).

- **Qu’est-ce qu’un mock ?**
  - Un objet simulé pour isoler le code testé d’une dépendance externe.

- **Exemple de test unitaire avec JUnit & Mockito** :


  ```java
  @ExtendWith(MockitoExtension.class)
  class UserServiceTest {
    @Mock UserRepository repo;
    @InjectMocks UserService service;
    @Test
    void testFindUser() {
      when(repo.findById(1L)).thenReturn(Optional.of(new User(1L, "Bob")));
      assertEquals("Bob", service.findUser(1L).getName());
    }
  }
  ```

- **Exemple de test d’intégration avec Testcontainers** :


  ```java
  @Testcontainers
  class MyIntegrationTest {
    @Container static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15");
    @Test void testDb() { /* ... */ }
  }
  ```

- **Comment mesurer la couverture de code ?**
  - Avec JaCoCo, SonarQube, ou IntelliJ. Viser 70-80% minimum, mais privilégier la qualité à la quantité.

- **Comment mocker une dépendance dans un test Spring Boot ?**
  - Avec `@MockBean` sur le bean à simuler.

- **Comment tester une API REST ?**
  - Avec MockMvc (Spring), RestAssured, ou des tests E2E (Cypress, Postman).

- **Comment intégrer les tests dans un pipeline CI/CD ?**
  - Ajouter une étape "test" dans le pipeline, bloquer le build si échec.

- **Comment garantir la qualité logicielle dans un projet ?**
  - Tests automatisés, analyse statique, revues de code, CI/CD, documentation, gestion de la dette technique.

- **Comment tester une méthode privée ?**
  - Tester via les méthodes publiques qui l’utilisent (ne pas tester directement).

- **Comment s’assurer qu’un bug ne revient pas ?**
  - Ajouter un test de non-régression qui échoue si le bug réapparaît.

- **Pourquoi la pyramide des tests est importante ?**
  - Pour garantir rapidité, fiabilité, coût maîtrisé des tests.

---

## Questions d'entretien & cas pratiques (avec réponses synthétiques)

- **Différence entre test unitaire et test d’intégration ?**
  - Unitaire = une fonction isolée, rapide. Intégration = plusieurs composants, base, plus lent.

- **À quoi sert SonarQube ?**
  - À analyser la qualité du code (bugs, duplications, conventions, couverture, sécurité).

- **Qu’est-ce qu’un mock ?**
  - Un objet simulé pour isoler le code testé d’une dépendance externe.

- **Comment garantir la qualité logicielle dans un projet ?**
  - Tests automatisés, analyse statique, revues de code, CI/CD, documentation.

- **Comment tester une méthode privée ?**
  - Tester via les méthodes publiques qui l’utilisent (ne pas tester directement).

- **Comment tester une API REST Spring ?**
  - Avec MockMvc, RestAssured, ou tests E2E.

- **Comment s’assurer qu’un bug ne revient pas ?**
  - Ajouter un test de non-régression qui échoue si le bug réapparaît.

- **Comment mocker une dépendance dans un test Spring Boot ?**
  - Avec `@MockBean`.

- **Comment mesurer la couverture de code ?**
  - Avec JaCoCo, SonarQube, ou IntelliJ.

- **Pourquoi la pyramide des tests est importante ?**
  - Pour garantir rapidité, fiabilité, coût maîtrisé des tests.

- **Qu’est-ce qu’un test de mutation ?**
  - Un test qui modifie le code source pour vérifier que les tests détectent bien les bugs (outil : PIT).

- **Comment tester la performance d’une application ?**
  - Avec des outils comme Gatling, JMeter, ou JMH pour le micro-benchmarking.

- **Comment détecter la dette technique ?**
  - Analyse statique (SonarQube), code smells, complexité cyclomatique, duplications.
