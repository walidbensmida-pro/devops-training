# Fiche de révision : Évolutions majeures de Java (LTS uniquement)

---

## Java 8 (2014)

- **Lambdas & Streams** :
  - *Pourquoi ?* Simplifier la manipulation des collections, favoriser la programmation fonctionnelle, rendre le code plus concis et lisible.
  - *Apport* : Permet le traitement fluide des données, le parallélisme facile, et la réduction du boilerplate.

  ```java
  List<String> noms = List.of("a", "b");
  noms.stream().map(String::toUpperCase).forEach(System.out::println);
  // Lambda : (x) -> x.toUpperCase()
  ```
- **Optional** :
  - *Pourquoi ?* Réduire les NullPointerException, clarifier l'absence potentielle de valeur.
  - *Apport* : Encourage la gestion explicite de l'absence de valeur.

  ```java
  Optional<String> opt = Optional.of("val");
  opt.ifPresent(System.out::println);
  ```
- **Date/Time API (java.time)** :
  - *Pourquoi ?* Remplacer l'ancienne API (java.util.Date/Calendar) jugée complexe et source d'erreurs.
  - *Apport* : API moderne, immuable, inspirée de Joda-Time, adaptée aux besoins actuels.

  ```java
  LocalDate d = LocalDate.now();
  ```
- **Default Methods (interfaces)** :
  - *Pourquoi ?* Permettre d'ajouter des méthodes aux interfaces sans casser la rétrocompatibilité.
  - *Apport* : Facilite l'évolution des API.

  ```java
  interface MyIntf { default void hello() { System.out.println("hi"); } }
  ```

---

## Java 11 (2018)

- **Nouvelles méthodes String** :
  - *Pourquoi ?* Simplifier les manipulations courantes sur les chaînes.
  - *Apport* : Moins de code utilitaire à écrire.

  ```java
  System.out.println("abc ".isBlank());
  System.out.println("a\nb".lines().count());
  ```
- **Lancement d’un fichier source sans compilation** :
  - *Pourquoi ?* Faciliter l'exécution rapide de scripts ou de petits programmes.
  - *Apport* : Gain de temps pour les tests et l'apprentissage.

  ```shell
  java Hello.java
  ```
- **HttpClient API** :
  - *Pourquoi ?* Remplacer l'ancienne API Http obsolète.
  - *Apport* : HTTP/2, asynchrone, moderne, facile à utiliser.

  ```java
  HttpClient client = HttpClient.newHttpClient();
  HttpRequest req = HttpRequest.newBuilder().uri(URI.create("https://example.com")).build();
  HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
  ```

---

## Java 17 (2021)

- **Pattern matching for instanceof** :
  - *Pourquoi ?* Réduire le boilerplate lors des vérifications de type.
  - *Apport* : Plus concis, moins d'erreurs de cast.

  ```java
  if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
  }
  ```
- **Sealed classes** :
  - *Pourquoi ?* Contrôler l'héritage, renforcer la sécurité et la maintenabilité.
  - *Apport* : Permet de limiter les sous-types autorisés.

  ```java
  sealed interface Shape permits Circle, Square {}
  final class Circle implements Shape {}
  final class Square implements Shape {}
  ```
- **Records** :
  - *Pourquoi ?* Réduire le code pour les classes porteuses de données (POJO/DTO).
  - *Apport* : Génère automatiquement equals, hashCode, toString, etc.

  ```java
  record Point(int x, int y) {}
  Point p = new Point(1, 2);
  ```

---

## Java 21 (2023)

- **Virtual threads (stable)** :
  - *Pourquoi ?* Faciliter la programmation concurrente à grande échelle.
  - *Apport* : Des milliers de threads légers, sans surcharge mémoire.

  ```java
  Thread.startVirtualThread(() -> System.out.println("Hello"));
  ```
- **Record patterns** :
  - *Pourquoi ?* Simplifier la décomposition des records dans les conditions.
  - *Apport* : Plus lisible, pattern matching puissant.

  ```java
  record User(String name, int age) {}
  User u = new User("Bob", 30);
  if (u instanceof User(String n, int a)) {
    System.out.println(n + " " + a);
  }
  ```
- **String templates (preview)** :
  - *Pourquoi ?* Faciliter l'interpolation de variables dans les chaînes.
  - *Apport* : Syntaxe moderne, moins d'erreurs de concaténation.

  ```java
  // Exemple (syntaxe indicative, peut évoluer)
  String name = "Bob";
  int age = 30;
  // String s = STR."Hello {name}, you are {age}";
  ```
- **Scoped values** :
  - *Pourquoi ?* Partager des données de façon sûre entre threads virtuels.
  - *Apport* : Alternative moderne aux ThreadLocal, plus sûre et performante.

  ```java
  // ScopedValue<String> USER = ScopedValue.newInstance();
  // ScopedValue.where(USER, "bob").run(() -> ...);
  ```

---

**Astuce** : Retenir les features majeures (lambdas, streams, var, records, virtual threads, pattern matching, HttpClient) et savoir donner un exemple rapide pour chaque version !
