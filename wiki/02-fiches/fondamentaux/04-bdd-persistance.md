# Fiche de révision : Bases de données & persistance

📑 **Sommaire**
1. [Concepts clés](#concepts-clés)
2. [Transactions & ACID](#transactions--acid)
3. [SQL vs NoSQL](#sql-vs-nosql)
4. [Exemples SQL](#exemples-sql)
5. [Index et performances](#index-et-performances)
6. [Questions d'entretien](#questions-dentretien)

---

## Concepts clés

- **SQL vs NoSQL** :
  - SQL = schéma fixe, requêtes puissantes (JOIN, ACID), ex : PostgreSQL, MySQL.
  - NoSQL = schéma flexible, scalabilité horizontale, ex : MongoDB, Cassandra.
  - Cas d’usage NoSQL : données volumineuses, schéma variable, haute disponibilité, analytics, graphes.

- **Transactions & ACID** :
  - **Atomicité** : tout ou rien.
  - **Cohérence** : respect des règles d’intégrité.
  - **Isolation** : transactions indépendantes (READ UNCOMMITTED, READ COMMITTED, REPEATABLE READ, SERIALIZABLE).
  - **Durabilité** : données persistées même après crash.
  - Exemple :

    ```sql
    BEGIN;
    UPDATE compte SET solde = solde - 100 WHERE id=1;
    UPDATE compte SET solde = solde + 100 WHERE id=2;
    COMMIT;
    ```

  - Problèmes classiques : deadlock, dirty read, phantom read, lost update.

- **Normalisation & formes normales** :
  - 1NF, 2NF, 3NF : éviter la redondance, garantir l’intégrité.
  - Dénormalisation pour la performance (analytics, reporting).

- **Index** :
  - Accélère les recherches, ralentit les insertions.
  - Index composite, index unique, index full-text.

    ```sql
    CREATE INDEX idx_nom ON utilisateur(nom);
    CREATE UNIQUE INDEX idx_email ON utilisateur(email);
    ```

- **Requêtes SQL avancées** :
  - Jointures, agrégations, sous-requêtes, vues, transactions complexes.

    ```sql
    SELECT u.nom, COUNT(o.id) FROM utilisateur u
    LEFT JOIN orders o ON o.user_id = u.id
    GROUP BY u.nom;
    ```

- **ORM (JPA/Hibernate)** :
  - Mapping objet-relationnel, facilite la persistance Java <-> SQL.
  - Relations : @OneToMany, @ManyToMany, fetch, cascade.

    ```java
    @Entity
    public class User {
      @Id Long id;
      String name;
      @OneToMany List<Order> orders;
    }
    ```

- **Migrations (Flyway, Liquibase)** :
  - Versionner et automatiser l’évolution du schéma.
  - Stratégies : migration incrémentale, rollback, zero downtime.

    ```sql
    -- V2__ajout_colonne_email.sql
    ALTER TABLE utilisateur ADD COLUMN email VARCHAR(255);
    ```

- **Performances** :
  - Index, requêtes optimisées (EXPLAIN), cache (application, base, ORM), partitionnement (sharding), réplication.

- **Réplication & sharding** :
  - Réplication = copie des données pour la haute dispo.
  - Sharding = partitionnement horizontal pour la scalabilité.

- **Backup & restauration** :
  - Sauvegarde régulière, test de restauration, stratégie adaptée à la criticité.

- **Sécurité & audit** :
  - Chiffrement, gestion des accès (GRANT/REVOKE), audit des requêtes.

---

## Astuces entretien & réponses types

- **Expliquer ACID avec un exemple concret** :
  - ACID = Atomicité, Cohérence, Isolation, Durabilité. Exemple : un virement bancaire (voir bloc SQL plus haut) : soit les deux comptes sont modifiés, soit aucun, même en cas de crash.

- **Justifier le choix SQL vs NoSQL** :
  - SQL : relations complexes, transactions, intégrité forte (ex : appli bancaire).
  - NoSQL : gros volume, schéma variable, scalabilité horizontale (ex : logs, réseaux sociaux).

- **Principe d’un index et ses impacts** :
  - Un index accélère les recherches (SELECT), mais ralentit les insertions/updates. Il consomme de l’espace disque. À utiliser sur les colonnes souvent filtrées ou jointes.

- **Rôle d’un ORM et d’un outil de migration** :
  - ORM (ex : JPA/Hibernate) : mappe les objets Java sur les tables SQL, facilite la persistance, gère les requêtes, les transactions, les relations.
  - Outil de migration (Flyway, Liquibase) : versionne et applique automatiquement les évolutions du schéma (ajout colonne, index, etc.) sur tous les environnements.

- **Normalisation et exemple de dénormalisation** :
  - Normalisation = découper les tables pour éviter la redondance (3NF : chaque colonne dépend uniquement de la clé primaire).
  - Dénormalisation = regrouper pour accélérer les lectures (ex : ajouter une colonne "total_commande" dans la table client pour éviter un calcul à chaque requête).

- **Problèmes d’isolation et cohérence en distribué** :
  - Isolation faible : dirty read, non-repeatable read, phantom read. En microservices, la cohérence forte est difficile, on privilégie souvent la cohérence éventuelle (event sourcing, SAGA).

- **Migrer une base sans downtime** :
  - Stratégie : migration incrémentale, feature toggle, double écriture, compatibilité ascendante, tests de rollback, réplication temporaire.

- **Exemple de requête SQL avancée** :
  - Jointure + agrégation :
    ```sql
    SELECT u.nom, COUNT(o.id) FROM utilisateur u
    LEFT JOIN orders o ON o.user_id = u.id
    GROUP BY u.nom;
    ```

- **Différence entre réplication et sharding** :
  - Réplication = copies identiques pour la dispo/sécurité. Sharding = découpage horizontal pour la scalabilité.

- **Stratégies de backup et sécurité** :
  - Backup régulier, stockage hors site, test de restauration, chiffrement des dumps, accès restreint, audit des accès.

---

## Questions d'entretien & cas pratiques (avec réponses synthétiques)

- **Qu’est-ce qu’une transaction ?**
  - Un ensemble d’opérations exécutées de façon atomique et isolée, respectant ACID.

- **À quoi sert un ORM ?**
  - À faire le lien entre objets du code et tables SQL, simplifier la persistance et la gestion des relations.

- **Citer un outil de migration de base de données.**
  - Flyway, Liquibase.

- **Pourquoi utiliser un index ?**
  - Pour accélérer les recherches et les jointures sur une colonne.

- **Différence entre réplication et sharding ?**
  - Réplication = copies identiques, sharding = découpage horizontal.

- **Explique la 3e forme normale et donne un exemple.**
  - 3NF : chaque colonne dépend uniquement de la clé primaire, pas d’autres colonnes. Exemple : séparer la table "commandes" et "clients" au lieu de dupliquer les infos client dans chaque commande.

- **Comment éviter un deadlock ?**
  - Toujours verrouiller les ressources dans le même ordre, limiter la durée des transactions, utiliser des timeouts.

- **Comment migrer une base sans downtime ?**
  - Migration incrémentale, double écriture, compatibilité ascendante, tests de rollback.

- **Donne un exemple de mapping @OneToMany en JPA.**
  ```java
  @Entity
  public class User {
    @Id Long id;
    @OneToMany List<Order> orders;
  }
  ```

- **Comment sécuriser l’accès à une base en production ?**
  - Comptes à privilèges minimaux, chiffrement, audit, accès réseau restreint, rotation des mots de passe.

- **Que fait la commande EXPLAIN ?**
  - Affiche le plan d’exécution d’une requête SQL, utile pour optimiser les index et la performance.

- **Comment gérer la cohérence des données dans un microservice ?**
  - Utiliser la cohérence éventuelle (event sourcing, SAGA), idempotence, messages transactionnels, compensation.
