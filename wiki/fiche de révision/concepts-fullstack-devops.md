# Fiche de révision : Concepts incontournables pour l'entretien Fullstack/DevOps

---

## Sommaire

- [Fiche de révision : Concepts incontournables pour l'entretien Fullstack/DevOps](#fiche-de-révision--concepts-incontournables-pour-lentretien-fullstackdevops)
  - [Sommaire](#sommaire)
  - [1. SOLID (Principes de conception objet)](#1-solid-principes-de-conception-objet)
  - [2. ACID (Transactions, bases de données)](#2-acid-transactions-bases-de-données)
  - [3. CAP (Bases NoSQL)](#3-cap-bases-nosql)
  - [4. DRY / KISS / YAGNI / SOC](#4-dry--kiss--yagni--soc)
  - [5. REST (API)](#5-rest-api)
  - [6. DevOps : CI/CD, IaC, Observabilité](#6-devops--cicd-iac-observabilité)
  - [7. Cloud \& Scalabilité](#7-cloud--scalabilité)
  - [8. Sécurité (bases)](#8-sécurité-bases)
  - [9. Patterns à connaître](#9-patterns-à-connaître)
  - [10. Bonus : Autres acronymes utiles](#10-bonus--autres-acronymes-utiles)

---

## 1. SOLID (Principes de conception objet)

- **S** : Single Responsibility Principle (Responsabilité unique)

  - *Une classe doit avoir une seule responsabilité.*
  - *Pourquoi ?* : Facilite la maintenance, les tests, et limite l'impact des changements.

  - *Exemple* :

    ```java
    // Mauvais :
    class UserService { void saveUser(){} void sendEmail(){} }
    // Bon :
    class UserService { void saveUser(){} }
    class EmailService { void sendEmail(){} }
    ```

  - *Explication rapide* : Chaque classe doit se concentrer sur une seule tâche ou fonctionnalité.

- **O** : Open/Closed Principle (Ouvert/Fermé)

  - *Une classe doit être ouverte à l’extension, fermée à la modification.*
  - *Pourquoi ?* : Permet d'ajouter des fonctionnalités sans casser l'existant, réduit les bugs lors des évolutions.

  - *Exemple* :

    ```java
    abstract class Shape { abstract double area(); }
    class Circle extends Shape { ... }
    // On ajoute Rectangle sans modifier Shape
    ```

  - *Explication rapide* : On doit pouvoir ajouter de nouvelles fonctionnalités sans toucher au code existant.

- **L** : Liskov Substitution Principle (Substitution de Liskov)

  - *Une sous-classe doit pouvoir remplacer sa super-classe sans bug.*
  - *Pourquoi ?* : Garantit la robustesse du code lors de l'utilisation du polymorphisme.

  - *Exemple* :

    ```java
    class Bird { void fly(){} }
    class Duck extends Bird {}
    // Si on ajoute Penguin extends Bird, il ne doit pas casser le code qui attend un Bird
    ```

  - *Explication rapide* : Les objets d'une classe dérivée doivent pouvoir remplacer ceux de la classe de base sans provoquer d'erreurs.

- **I** : Interface Segregation Principle (Séparation des interfaces)

  - *Mieux vaut plusieurs petites interfaces qu’une grosse.*
  - *Pourquoi ?* : Évite d'imposer à une classe d'implémenter des méthodes inutiles, rend le code plus flexible.

  - *Exemple* :

    ```java
    interface Printer { void print(); }
    interface Scanner { void scan(); }
    class MultiFunctionPrinter implements Printer, Scanner {}
    ```

  - *Explication rapide* : Il vaut mieux avoir des interfaces spécifiques et petites que des interfaces générales et encombrantes.

- **D** : Dependency Inversion Principle (Inversion des dépendances)

  - *Dépendre d’abstractions, pas de classes concrètes.*
  - *Pourquoi ?* : Facilite les tests, le changement d'implémentation, et rend le code moins couplé.

  - *Exemple* :

    ```java
    interface Repo { void save(); }
    class UserService { Repo repo; }
    ```

  - *Explication rapide* : Les détails doivent dépendre des abstractions, et non l'inverse.

---

## 2. ACID (Transactions, bases de données)

- **A** : Atomicité

  - *Une transaction est tout ou rien.*
  - *Exemple* :
    > Si tu virement 100€, soit tout est fait, soit rien n’est débité.
  - *Explication rapide* : Une transaction doit être traitée dans son intégralité ou pas du tout.

- **C** : Cohérence

  - *Les données restent valides avant et après la transaction.*
  - *Exemple* :
    > Pas de solde négatif après un virement.
  - *Explication rapide* : Les transactions doivent amener la base de données d'un état valide à un autre état valide.

- **I** : Isolation

  - *Les transactions ne se voient pas entre elles.*
  - *Exemple* :
    > Deux achats en même temps ne voient pas l’état intermédiaire.
  - *Explication rapide* : Les transactions concurrentes ne doivent pas interférer l'une avec l'autre.

- **D** : Durabilité

  - *Une fois validée, la transaction est persistée même après crash.*
  - *Exemple* :
    > Après commit, la donnée est sur disque.
  - *Explication rapide* : Une fois qu'une transaction est confirmée, ses effets doivent persister même en cas de panne système.

---

## 3. CAP (Bases NoSQL)

- **C** : Cohérence

  - *Tous les nœuds voient la même donnée au même moment.*

- **A** : Disponibilité

  - *Chaque requête reçoit une réponse, même en cas de panne partielle.*

- **P** : Tolérance au Partitionnement

  - *Le système continue à fonctionner même si le réseau est coupé entre deux parties.*

- *On ne peut garantir que 2 sur 3 en même temps (ex : Cassandra = AP, MongoDB = CP).* 
- *Explication rapide* : En cas de défaillance, un système doit choisir entre maintenir la cohérence, la disponibilité ou la tolérance au partitionnement.

---

## 4. DRY / KISS / YAGNI / SOC

- **DRY** : Don’t Repeat Yourself

  - *Évite la duplication de code.*
  - *Exemple* :

    ```java
    // Mauvais :
    void saveUser(){} void saveAdmin(){}
    // Bon :
    void save(Entity e){}
    ```

  - *Explication rapide* : La duplication de code doit être évitée car elle rend les modifications futures plus difficiles et sujettes à erreurs.

- **KISS** : Keep It Simple, Stupid

  - *Fais simple, évite la complexité inutile.*
  - *Exemple* :
    > Préfère une boucle simple à une récursion complexe si ce n’est pas utile.
  - *Explication rapide* : La simplicité doit être un objectif clé dans la conception, et la complexité doit être évitée.

- **YAGNI** : You Aren’t Gonna Need It

  - *Ne code que ce qui est nécessaire maintenant.*
  - *Exemple* :
    > N’ajoute pas de fonctionnalités “au cas où”.
  - *Explication rapide* : Ne pas ajouter de fonctionnalités jusqu'à ce qu'elles soient nécessaires.

- **SOC** : Separation of Concerns

  - *Sépare les responsabilités (ex : service, repository, contrôleur).* 
  - *Exemple* :
    > Un contrôleur ne doit pas gérer la base de données directement.
  - *Explication rapide* : Différentes préoccupations ou responsabilités dans une application doivent être séparées pour réduire la complexité.

---

## 5. REST (API)

- **Stateless** :
  - *Le serveur ne garde pas d’état entre deux requêtes.*

- **Ressources** :
  - *Chaque ressource a une URL unique (ex : /users/1).* 

- **Verbes HTTP** :
  - *GET (lire), POST (créer), PUT (remplacer), PATCH (modifier), DELETE (supprimer).* 

- **Code de statut** :
  - *200 OK, 201 Created, 400 Bad Request, 404 Not Found, 500 Server Error…*

- *Exemple* :
  > POST /users crée un utilisateur, GET /users/1 lit l’utilisateur 1.

- *Explication rapide* : REST est une architecture d'API qui utilise des verbes HTTP et des URLs pour manipuler des ressources.

---

## 6. DevOps : CI/CD, IaC, Observabilité

- **CI/CD** :
  - *Automatiser les tests et le déploiement à chaque commit.*
  - *Exemple* :
    > Jenkins, GitHub Actions, GitLab CI…
  - *Explication rapide* : CI/CD sont des pratiques qui permettent de livrer des changements de code plus fréquemment et de manière fiable.

- **IaC** : Infrastructure as Code
  - *Décrire l’infra dans du code versionné.*
  - *Exemple* :
    > Terraform, Ansible, Helm…
  - *Explication rapide* : Gérer et provisionner des infrastructures informatiques à l'aide de fichiers de configuration lisibles par machine.

- **Observabilité** :
  - *Surveiller l’état de l’appli (logs, métriques, traces).* 
  - *Exemple* :
    > ELK, Prometheus, Grafana…
  - *Explication rapide* : Capacité à mesurer l'état interne d'un système à partir des données qu'il produit.

- **Conteneurisation** :
  - *Emballer l’appli et ses dépendances dans un conteneur.*
  - *Exemple* :
    > Docker, Kubernetes
  - *Explication rapide* : Emballer une application avec tout son environnement d'exécution pour garantir qu'elle fonctionne de manière cohérente dans différents environnements.

---

## 7. Cloud & Scalabilité

- **Scalabilité horizontale/verticale** :
  - *Ajouter des serveurs (horizontal) ou plus de puissance (vertical).* 

- **Load Balancer** :
  - *Répartit la charge entre plusieurs serveurs.*

- **Haute disponibilité** :
  - *L’appli reste dispo même si un serveur tombe.*

- **Failover** :
  - *Bascule automatique sur un autre serveur en cas de panne.*

- *Explication rapide* : Capacité d'un système à s'adapter à une augmentation de la charge en ajoutant des ressources.

---

## 8. Sécurité (bases)

- **Principle of Least Privilege** :
  - *Donner le minimum de droits nécessaires.*

- **Authentification vs Autorisation** :
  - *Authentification = qui es-tu ? Autorisation = as-tu le droit ?*

- **Chiffrement (at rest, in transit)** :
  - *Données chiffrées sur disque et lors du transfert.*

- **Vulnérabilités courantes** :
  - *XSS (injection JS), CSRF (falsification requête), SQLi (injection SQL), SSRF (requête côté serveur).* 

- *Exemple* :
  > Toujours valider et échapper les entrées utilisateur.

- *Explication rapide* : La sécurité doit être intégrée à chaque étape du développement et du déploiement.

---

## 9. Patterns à connaître

- **Singleton** : *Une seule instance globale.*

  - *Exemple* :

    ```java
    class Singleton { private static final Singleton INSTANCE = new Singleton(); }
    ```

  - *Explication rapide* : Garantit qu'une classe n'a qu'une seule instance et fournit un point d'accès global à cette instance.

- **Factory** : *Crée des objets sans exposer la logique de création.*

  - *Explication rapide* : Définit une interface pour créer un objet, mais laisse le choix de la classe à instancier aux sous-classes.

- **Observer** : *Notifie automatiquement les abonnés d’un changement.*

  - *Explication rapide* : Permet à un objet de notifier d'autres objets sur des changements d'état sans les coupler étroitement.

- **Adapter** : *Adapte une interface à une autre.*

  - *Explication rapide* : Permet à des classes avec des interfaces incompatibles de travailler ensemble.

- **Facade** : *Simplifie l’accès à un sous-système complexe.*

  - *Explication rapide* : Fournit une interface simplifiée à un ensemble d'interfaces dans un sous-système.

- **Strategy** : *Change dynamiquement l’algorithme utilisé.*

  - *Explication rapide* : Définit une famille d'algorithmes, encapsule chacun d'eux, et les rend interchangeables.

- **Repository** : *Abstraction de la couche d’accès aux données.*

  - *Explication rapide* : Médie entre le domaine et la couche de mappage de données en utilisant des objets de domaine pour interroger et enregistrer des données.

- **MVC** : *Sépare Modèle, Vue, Contrôleur.*

  - *Explication rapide* : Modèle-Vue-Contrôleur est un modèle architectural qui sépare une application en trois composants principaux.

- **CQRS** : *Sépare lecture et écriture.*

  - *Explication rapide* : Command Query Responsibility Segregation sépare les opérations de lecture et d'écriture sur les données.

- **Event Sourcing** : *Stocke l’historique des changements sous forme d’événements.*

  - *Explication rapide* : Au lieu de stocker uniquement l'état final, on stocke chaque changement d'état en tant qu'événement.

---

## 10. Bonus : Autres acronymes utiles

- **TDD** : Test Driven Development (écrire les tests avant le code)
- **DDD** : Domain Driven Design (modéliser le métier)
- **BDD** : Behavior Driven Development (tests orientés comportement)
- **SRP** : Single Responsibility Principle (voir SOLID)
- **POO** : Programmation Orientée Objet
- **FP** : Functional Programming (programmation fonctionnelle)

---

**Astuce** : Savoir expliquer chaque concept simplement, donner un exemple, et dire pourquoi c’est utile !
