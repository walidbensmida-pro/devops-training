# Fiche de révision : Programmation impérative, orientée objet et fonctionnelle (Java)

📑 **Sommaire**
1. [Programmation impérative](#1-programmation-impérative)
2. [Programmation orientée objet (POO)](#2-programmation-orientée-objet-poo)
3. [Programmation fonctionnelle](#3-programmation-fonctionnelle)
4. [Comparatif des paradigmes](#4-comparatif-des-paradigmes)
5. [Questions d'entretien](#5-questions-dentretien)

---

## 1. Programmation impérative
- **Principe** : On décrit étape par étape comment faire les choses (instructions, boucles, variables).
- **Exemple** :
```java
int somme = 0;
for (int i = 1; i <= 5; i++) {
    somme += i;
}
System.out.println(somme); // 15
```

---

## 2. Programmation orientée objet (POO)
- **Principe** : On modélise le monde avec des objets (état + comportement).
- **Concepts clés** : classe, objet, héritage, encapsulation, polymorphisme.
- **Exemple** :
```java
class Person {
    String name;
    Person(String name) { this.name = name; }
    void sayHello() { System.out.println("Hello " + name); }
}
Person p = new Person("Alice");
p.sayHello(); // Hello Alice
```

---

## 3. Programmation fonctionnelle
- **Principe** : On manipule des fonctions comme des objets, on évite l’état mutable, on privilégie les expressions.
- **Concepts clés** :
  - **Fonction pure** : même entrée → même sortie, pas d’effet de bord (pas de modification d’état externe).
    ```java
    int doubleVal(int x) { return x * 2; } // Fonction pure
    int randomPlus(int x) { return x + new Random().nextInt(); } // Pas pure !
    ```
  - **Immutabilité** : on ne modifie pas les objets, on crée des copies modifiées (pas de `setX()` partout).
    ```java
    List<String> l1 = List.of("a", "b");
    List<String> l2 = new ArrayList<>(l1);
    l2.add("c"); // l1 n'est pas modifiée
    ```
  - **Fonction d’ordre supérieur** : une fonction qui prend une fonction en paramètre ou retourne une fonction.
    ```java
    void appliqueDeuxFois(Function<Integer, Integer> f, int x) {
        System.out.println(f.apply(f.apply(x)));
    }
    appliqueDeuxFois(y -> y + 1, 3); // Affiche 5
    ```
  - **Interface fonctionnelle** : interface avec UNE SEULE méthode abstraite (ex : `Function<T,R>`, `Predicate<T>`, `Consumer<T>`, etc.).
    ```java
    @FunctionalInterface
    interface Calcul {
        int op(int a, int b);
    }
    Calcul addition = (a, b) -> a + b;
    System.out.println(addition.op(2, 3)); // 5
    ```
  - **Lambda** : fonction anonyme, syntaxe `(x) -> x + 1`.
    ```java
    Function<String, Integer> longueur = s -> s.length();
    System.out.println(longueur.apply("kafka")); // 5
    ```
  - **Référence de méthode** : `Class::methode` (ex : `String::toUpperCase`).
    ```java
    List<String> mots = List.of("a", "b");
    mots.stream().map(String::toUpperCase).forEach(System.out::println); // A B
    ```
  - **Composition** : on peut chaîner les fonctions (`f.andThen(g)`, `stream.map(...).filter(...)`).
    ```java
    Function<Integer, Integer> doubler = x -> x * 2;
    Function<Integer, Integer> ajouterUn = x -> x + 1;
    Function<Integer, Integer> doublerPuisAjouterUn = doubler.andThen(ajouterUn);
    System.out.println(doublerPuisAjouterUn.apply(3)); // 7
    ```
  - **Pas d’état partagé** : pas de variables globales modifiées dans les lambdas.
    ```java
    int total = 0;
    List<Integer> nums = List.of(1,2,3);
    // Mauvais :
    // nums.forEach(n -> total += n); // Effet de bord !
    // Bon :
    int sum = nums.stream().mapToInt(Integer::intValue).sum();
    ```

### a) Interface fonctionnelle
- Une interface avec UNE SEULE méthode abstraite.
- Permet d’utiliser des lambdas ou des références de méthode.
```java
@FunctionalInterface
interface Calcul {
    int op(int a, int b);
}
Calcul addition = (a, b) -> a + b;
System.out.println(addition.op(2, 3)); // 5
```

### b) Lambda
```java
Function<String, Integer> longueur = s -> s.length();
System.out.println(longueur.apply("kafka")); // 5
```

### c) Référence de méthode
```java
public class MathUtils {
    public static int mult(int a, int b) { return a * b; }
}
Calcul multiplication = MathUtils::mult;
System.out.println(multiplication.op(2, 3)); // 6
```

### d) Utilisation dans les streams
```java
List<String> mots = List.of("java", "kafka", "stream");
mots.stream()
    .map(String::toUpperCase)
    .filter(s -> s.length() > 4)
    .forEach(System.out::println); // KAFKA, STREAM
```

---

## 4. Comparatif des paradigmes
| Style         | Exemple clé                | Avantage principal         |
|---------------|---------------------------|---------------------------|
| Impératif     | Boucles, if/else          | Contrôle précis           |
| Objet         | Classe, méthode           | Modélisation du réel      |
| Fonctionnel   | Lambda, stream, map/filter| Code concis, testable     |

---

## 5. Questions d'entretien
- **Interface fonctionnelle** = 1 méthode abstraite → lambda possible
- **Lambda** = fonction anonyme, syntaxe `(x) -> x + 1`
- **Référence de méthode** = `Class::methode`
- **Streams** = pipeline de traitements fonctionnels sur des collections
- **En Java moderne, tu peux combiner POO et fonctionnel !**

---

## Quiz express : Teste-toi avant l'entretien !

1. **Qu'est-ce qu'une interface fonctionnelle ? Donne un exemple.**
2. **Quelle est la différence entre une lambda et une référence de méthode ?**
3. **Pourquoi privilégier l'immutabilité en programmation fonctionnelle ?**
4. **Donne un exemple de fonction d'ordre supérieur en Java.**
5. **Explique la différence entre une fonction pure et une fonction avec effet de bord.**
6. **Comment chaîner deux fonctions en Java ?**
7. **Dans quel cas utiliserais-tu un stream en Java ?**
8. **Pourquoi éviter de modifier des variables extérieures dans une lambda ?**
9. **Complète : `List<String> mots = ...; mots.stream().____(String::toUpperCase).forEach(System.out::println);`**
10. **Vrai ou faux : En Java moderne, on peut combiner POO et programmation fonctionnelle.**

*Relis la fiche, réponds à ces questions à l'oral ou à l'écrit, et tu seras prêt !*
