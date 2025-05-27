# Intégrer Keycloak avec Spring Boot (Sécurité centralisée)

Ce guide explique comment connecter une application Spring Boot à Keycloak pour gérer l’authentification et les rôles de façon centralisée.

---

## 1. Pourquoi Keycloak ?
- Fournisseur d’identité open source (SSO, gestion des utilisateurs, MFA…)
- Centralise la gestion des comptes, rôles et permissions
- S’intègre facilement avec Spring Boot

---

## 2. Prérequis
- Avoir Keycloak installé (en local ou via Docker)
- Avoir une application Spring Boot

---

## 3. Démarrer Keycloak en local (exemple Docker)
```bash
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:24.0.1 start-dev
```
Accès à l’admin : http://localhost:8080

---

## 4. Configurer Keycloak
1. Créer un Realm (ex : devops-training)
2. Créer un Client (ex : springboot-app, type : public)
3. Créer des rôles (ex : USER, ADMIN)
4. Créer des utilisateurs et leur attribuer des rôles

---

## 5. Ajouter les dépendances Spring Boot
Dans le `pom.xml` :
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

## 6. Configurer l’application pour Keycloak
Dans `application.yml` :
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/devops-training
```

---

## 7. Protéger les endpoints par rôle
```java
@RestController
public class AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminOnly() {
        return "Accès réservé à l’admin";
    }
}
```

---

## 8. Tester
- Lance Keycloak et l’application Spring Boot
- Connecte-toi avec un utilisateur ayant le rôle ADMIN
- Accède à `/admin` : tu dois être authentifié et avoir le bon rôle

---

## 9. Aller plus loin
- [Docs Keycloak](https://www.keycloak.org/docs/latest/)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)

---

> Ce guide peut être enrichi avec la gestion des groupes, des claims personnalisés, ou l’intégration avec d’autres providers (Google, GitHub…).
