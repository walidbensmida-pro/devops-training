# Sécurité JWT dans Spring Boot : Guide pas à pas

## 1. Objectif
Mettre en place une authentification et une autorisation sécurisées avec JWT dans une application Spring Boot, pour bien comprendre le fonctionnement des tokens, des rôles et de la protection des endpoints.

## 2. Fonctionnement général
- L’utilisateur s’authentifie via `/login` (POST, JSON avec username/password).
- Le backend vérifie les identifiants et génère un JWT contenant le username et les rôles.
- Le client utilise ce JWT dans l’en-tête `Authorization: Bearer <token>` pour accéder aux endpoints protégés.
- Un filtre Spring vérifie la validité du JWT à chaque requête.
- Les rôles dans le JWT permettent de restreindre l’accès à certains endpoints.

## 3. Utilisateurs de test
- `admin` / `admin123` (rôle USER)
- `superadmin` / `superadmin123` (rôle ADMIN)

## 4. Tester l’API
### a) Récupérer un token JWT
POST `/login` avec :
```json
{
  "username": "admin",
  "password": "admin123"
}
```
Réponse :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### b) Décoder le JWT
- Copier le token reçu
- Aller sur https://jwt.io/
- Coller le token pour voir le contenu (username, rôles, dates, etc.)

### c) Appeler un endpoint protégé
- Ajouter l’en-tête HTTP :
  `Authorization: Bearer <token>`
- Exemple : GET `/private`
  - Avec un token d’admin : accès refusé (403)
  - Avec un token de superadmin : accès autorisé

## 5. Sécurité côté backend
- Les routes publiques sont listées dans la config (`/`, `/login`, etc.)
- Toutes les autres routes nécessitent un JWT valide
- Les rôles sont vérifiés avec `@PreAuthorize` sur les endpoints sensibles

## 6. À retenir pour l’entretien
- JWT = authentification stateless, scalable, standard
- Les rôles sont embarqués dans le token et vérifiés côté backend
- On peut facilement ajouter d’autres providers (Keycloak, Auth0) plus tard
- Bien comprendre la différence entre authentification (qui ?) et autorisation (quoi ?)

## 7. Pour aller plus loin
- Ajouter des logs de sécurité
- Gérer l’expiration et le renouvellement des tokens
- Passer à Keycloak pour la gestion centralisée des utilisateurs

---

**Ce guide t’aide à comprendre et à expliquer la sécurité JWT dans Spring Boot, étape par étape, pour réussir un entretien ou sécuriser un projet.**
