# Intégration Keycloak (OIDC) et Sécurisation sélective avec Spring Boot (pour débutant)

Ce guide explique pas à pas comment sécuriser une application Spring Boot avec Keycloak, en ne protégeant que certaines pages (ex : Wiki IA), tout en gardant le reste du site public. Il détaille aussi la création du client Keycloak, la gestion des utilisateurs, et la résolution du problème de reconnexion automatique.

---

# 0. Introduction à l'authentification moderne (pour débutant)

Avant de plonger dans la configuration, il est important de comprendre les concepts de base : **OAuth2**, **OpenID**, **OIDC**, et pourquoi on les utilise.

---

## Qu'est-ce que l'authentification et pourquoi la déléguer ?

- **Authentification** : c'est le fait de prouver qui on est (ex : se connecter avec un identifiant/mot de passe).
- **Autorisation** : c'est le fait de vérifier ce qu'on a le droit de faire (ex : accéder à une page protégée).
- **Déléguer l'authentification** : au lieu de gérer les mots de passe dans chaque application, on confie cette tâche à un service spécialisé (ex : Keycloak, Google, Facebook, etc.).

---

## 1. Les grands mots expliqués simplement

- **OAuth2** : un protocole (une règle) qui permet à une application de demander à un autre service d'authentifier un utilisateur. Il ne gère pas l'identité, juste l'accès.
- **OpenID** : un protocole plus ancien qui permettait de prouver son identité sur plusieurs sites.
- **OIDC (OpenID Connect)** : une extension moderne d'OAuth2 qui ajoute la gestion de l'identité (qui je suis) en plus de l'accès. C'est le standard actuel pour la connexion unique (SSO).
- **SSO (Single Sign-On)** : une fois connecté sur un service, on est automatiquement connecté sur les autres applications qui font confiance à ce service.
- **Keycloak** : un logiciel qui implémente OIDC et OAuth2, et qui gère pour toi la connexion, la création d'utilisateurs, les droits, etc.

---

## 2. Pourquoi utiliser OIDC/OAuth2 et Keycloak ?

- **Sécurité** : tu ne stockes pas les mots de passe dans ton application.
- **Centralisation** : un seul compte pour plusieurs applications.
- **Facilité** : tu peux ajouter des règles (mot de passe fort, double facteur, etc.) sans toucher à ton code.
- **Interopérabilité** : tu peux connecter ton appli à Google, Facebook, ou ton propre Keycloak.

---

## 3. Le schéma du flow OIDC (simplifié)

```
[Utilisateur] --(1)--> [Ton site Spring Boot] --(2)--> [Keycloak]
   |                                                        |
   |<-------------------(3)---------------------------------|
   |                                                        |
   |<-------------------(4)---------------------------------|

1. L'utilisateur clique sur "Connexion" sur ton site.
2. Ton site redirige l'utilisateur vers Keycloak (avec une URL spéciale).
3. L'utilisateur saisit ses identifiants sur Keycloak (page de login).
4. Keycloak renvoie l'utilisateur sur ton site, avec une preuve qu'il est bien connecté (un "jeton").
5. Ton site vérifie ce jeton et autorise l'accès aux pages protégées.
```

---

## 4. Pourquoi ce flow ?

- **Sécurité** : le mot de passe n'est jamais vu par ton site, seulement par Keycloak.
- **Simplicité** : tu n'as pas à gérer la création, la perte ou la sécurité des mots de passe.
- **Extensible** : tu peux ajouter d'autres fournisseurs d'identité facilement.

---

## 5. Les étapes concrètes dans ce projet

### a) Création du client Keycloak "devops-training"

1. **Se connecter à l'interface d'administration Keycloak** (généralement http://<votre-ip>:8080 ou via l'Ingress).
2. Aller dans le **realm** (espace d'utilisateurs) de votre projet.
3. Cliquer sur "Clients" > "Créer un client".
4. Nommer le client : `devops-training`.
5. Choisir le type : `OpenID Connect`.
6. Renseigner l'URL de redirection :
   - Exemple : `http://localhost:8080/login/oauth2/code/keycloak` (adapter selon votre config)
7. Enregistrer, puis récupérer le **client secret** (clé secrète) généré par Keycloak.

### b) Création d'un utilisateur Keycloak

1. Aller dans "Utilisateurs" > "Créer un utilisateur".
2. Remplir les champs (nom, prénom, email, etc.).
3. Après création, aller dans l'onglet "Identifiants" et définir un mot de passe.
4. (Optionnel) Forcer le changement de mot de passe à la première connexion.

### c) Configuration Spring Boot pour Keycloak

Dans `application.yml` :
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: devops-training
            client-secret: <votre-client-secret>
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          keycloak:
            issuer-uri: http://<votre-ip>:8080/realms/<votre-realm>
```
- Remplacez `<votre-client-secret>`, `<votre-ip>` et `<votre-realm>` par vos valeurs.

### d) Sécurisation sélective dans Spring Boot

Dans la classe `SecurityConfig.java` :
- On protège uniquement `/wiki-ai` et `/api/ask-wiki`.
- Le reste est public.
- On configure le logout pour revenir à l'accueil.
- On désactive CSRF pour simplifier (à activer si besoin).

Exemple :
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/wiki-ai", "/api/ask-wiki").authenticated()
            .anyRequest().permitAll()
        )
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(authorization -> authorization
                .authorizationRequestResolver(
                    customAuthorizationRequestResolver(clientRegistrationRepository)
                )
            )
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
        )
        .csrf(csrf -> csrf.disable());
    return http.build();
}
```

### e) Problème rencontré : reconnexion automatique (SSO)

- **Problème** : Quand on clique sur "Connexion", si on est déjà connecté à Keycloak ailleurs, on est reconnecté sans voir la page de login.
- **Pourquoi ?** : C'est le comportement normal du SSO (Single Sign-On).
- **Solution** : Forcer Keycloak à afficher la page de login à chaque fois, même si une session existe.

#### Comment ?
On ajoute un "resolver" personnalisé dans `SecurityConfig.java` qui ajoute le paramètre `prompt=login` à chaque requête d'authentification.

Exemple :
```java
private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(ClientRegistrationRepository repo) {
    DefaultOAuth2AuthorizationRequestResolver defaultResolver =
            new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
    return new OAuth2AuthorizationRequestResolver() {
        @Override
        public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
            OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
            if (authRequest == null) return null;
            Map<String, Object> extraParams = new HashMap<>(authRequest.getAdditionalParameters());
            extraParams.put("prompt", "login");
            return OAuth2AuthorizationRequest.from(authRequest)
                    .additionalParameters(extraParams)
                    .build();
        }
        @Override
        public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
            OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
            if (authRequest == null) return null;
            Map<String, Object> extraParams = new HashMap<>(authRequest.getAdditionalParameters());
            extraParams.put("prompt", "login");
            return OAuth2AuthorizationRequest.from(authRequest)
                    .additionalParameters(extraParams)
                    .build();
        }
    };
}
```
- Ce code garantit que la page de login Keycloak s'affiche toujours, même si tu es déjà connecté ailleurs.

---

## 6. Résumé pédagogique

- **Keycloak** gère la connexion/déconnexion des utilisateurs.
- **Spring Boot** peut protéger seulement certaines pages.
- **SSO** permet de rester connecté partout, mais on peut forcer la page de login avec `prompt=login`.
- **Tout est factorisé** : le menu, le style, la sécurité, pour faciliter l’ajout de nouvelles pages.

---

## 7. Pour aller plus loin
- Pour sécuriser d’autres pages, ajoute-les dans `.requestMatchers()`.
- Pour rendre tout le site privé, remplace `.anyRequest().permitAll()` par `.anyRequest().authenticated()`.
- Pour personnaliser le thème ou le menu, modifie les fragments dans `src/main/resources/templates/fragments/`.

---

**Ce guide est fait pour les débutants : chaque mot-clé est expliqué, et tu peux copier-coller les exemples dans ton projet.**

N’hésite pas à enrichir ce fichier avec tes propres retours d’expérience !
