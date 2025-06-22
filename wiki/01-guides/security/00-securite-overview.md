# Sécurité, Authentification et Autorisation dans une application Spring Boot

## 1. Concepts clés

### JWT (JSON Web Token)
- **Définition** : Format de jeton sécurisé, autoportant, signé (et parfois chiffré).
- **Utilisation** : Transmet des informations d’authentification entre client et serveur sans session côté serveur.
- **Avantages** : Stateless, facile à valider, adapté aux API REST.
- **Limites** : Gestion du renouvellement et de la révocation plus complexe.

### OAuth2
- **Définition** : Protocole d’autorisation standardisé permettant à une application d’accéder à des ressources protégées au nom d’un utilisateur.
- **Rôles** :
  - Resource Owner (utilisateur)
  - Client (application)
  - Authorization Server (serveur d’authentification)
  - Resource Server (API protégée)
- **Flux courants** : Authorization Code, Client Credentials, etc.
- **Avantages** : Séparation claire des rôles, délégation d’accès, sécurité renforcée.

### OpenID Connect (OIDC)
- **Définition** : Extension d’OAuth2 pour l’authentification (et non seulement l’autorisation).
- **Fonctionnement** : Fournit un ID Token (souvent un JWT) contenant l’identité de l’utilisateur.
- **Avantages** : Authentification standardisée, interopérabilité, SSO (Single Sign-On).

### Keycloak
- **Définition** : Solution open source de gestion des identités et des accès (IAM) qui implémente OAuth2, OIDC et SAML.
- **Fonctionnalités** :
  - Gestion des utilisateurs, rôles, groupes
  - SSO, intégration sociale (Google, Facebook...)
  - Administration centralisée
  - Support multi-applications
- **Avantages** : Externalise toute la sécurité, gain de temps, conformité aux standards.

## 2. Comparatif

| Solution         | Authentification | Autorisation | SSO | Gestion utilisateurs | Standard | Complexité |
|------------------|------------------|--------------|-----|---------------------|----------|------------|
| JWT pur          | Oui (basique)    | Oui          | Non | Non                 | Oui      | Faible     |
| OAuth2           | Non (autorisation) | Oui        | Non | Non                 | Oui      | Moyenne    |
| OpenID Connect   | Oui              | Oui          | Oui | Non                 | Oui      | Moyenne    |
| Keycloak         | Oui              | Oui          | Oui | Oui                 | Oui      | Élevée     |

- **JWT pur** : Simple, adapté aux petits projets ou microservices internes.
- **OAuth2/OIDC** : Pour API exposées à des tiers, besoin de délégation, SSO.
- **Keycloak** : Pour projets d’entreprise, gestion centralisée, multi-applications.

## 3. Pourquoi les utiliser ?
- **Sécurité** : Protéger les API, éviter la gestion manuelle des mots de passe.
- **Interopérabilité** : Utiliser des standards reconnus, faciliter l’intégration avec d’autres systèmes.
- **Scalabilité** : Gérer la sécurité de plusieurs applications de façon centralisée.
- **Expérience utilisateur** : SSO, gestion des rôles, intégration sociale.

## 4. Intégration dans Spring Boot

### a) JWT pur
- Utiliser une librairie comme `jjwt` ou `spring-boot-starter-oauth2-resource-server`.
- Générer un JWT à la connexion, le transmettre dans l’en-tête Authorization.
- Valider le JWT à chaque requête côté backend.

### b) OAuth2 / OpenID Connect
- Utiliser `spring-boot-starter-oauth2-client` et/ou `spring-boot-starter-oauth2-resource-server`.
- Configurer un provider (Keycloak, Auth0, Google, etc.).
- Gérer les flux d’authentification et d’autorisation via les endpoints standards.

### c) Keycloak
- Déployer Keycloak (Docker, Kubernetes, SaaS...)
- Configurer un Realm, des clients, des utilisateurs et des rôles.
- Intégrer Spring Boot avec le starter `spring-boot-starter-oauth2-resource-server` ou l’adaptateur Keycloak.
- Externaliser toute la gestion de la sécurité.

## 5. Conseils pour un entretien technique
- **Comprendre les différences** entre authentification (qui suis-je ?) et autorisation (ai-je le droit ?).
- **Savoir expliquer** les avantages de chaque solution selon le contexte (POC, production, multi-applications, etc.).
- **Maîtriser la configuration** de base dans Spring Boot (application.yml, SecurityConfig, dépendances Maven).
- **Être capable de justifier** le choix d’une solution (simplicité, scalabilité, conformité, etc.).

## 6. Liens utiles
- [JWT.io](https://jwt.io/)
- [OAuth2 RFC](https://datatracker.ietf.org/doc/html/rfc6749)
- [OpenID Connect](https://openid.net/connect/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)

---

**Résumé** :
- JWT = format de jeton.
- OAuth2 = protocole d’autorisation.
- OpenID Connect = authentification basée sur OAuth2.
- Keycloak = solution complète qui implémente tout ça.

Adaptez la solution à votre contexte : pour un POC, JWT pur suffit ; pour un projet d’entreprise, privilégiez Keycloak ou un provider OIDC.
