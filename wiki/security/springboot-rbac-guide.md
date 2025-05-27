# Contrôle d’accès avancé (RBAC, rôles, permissions) avec Spring Security

Ce guide explique comment mettre en place un contrôle d’accès fin dans une application Spring Boot, en utilisant les rôles et permissions (RBAC).

---

## 1. C’est quoi le RBAC ?
- **RBAC** = Role-Based Access Control (contrôle d’accès basé sur les rôles)
- On attribue des rôles aux utilisateurs (ex : ADMIN, USER)
- Chaque rôle donne accès à certaines actions ou ressources

---

## 2. Définir les rôles dans Spring Boot
Dans la base de données ou en mémoire, chaque utilisateur a un ou plusieurs rôles :
```java
// Exemple d’entité utilisateur
@Entity
public class Utilisateur {
    @Id
    private Long id;
    private String username;
    private String password;
    private String role; // ex : "ROLE_ADMIN", "ROLE_USER"
}
```

---

## 3. Protéger les endpoints selon le rôle
Dans le contrôleur :
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

## 4. Configurer Spring Security pour les rôles
Dans la classe de configuration :
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();
    }
}
```

---

## 5. Astuces et bonnes pratiques
- Toujours utiliser le préfixe `ROLE_` pour les rôles
- Privilégier les annotations `@PreAuthorize` ou `@Secured` pour la granularité
- Limiter les droits au strict nécessaire (principe du moindre privilège)
- Documenter les accès dans le projet

---

## 6. Aller plus loin
- [Spring Security – Méthodes d’autorisation](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html)
- [Guide Sécurité Spring Boot](securite-springboot.md)

---

> Prochain atelier : Sécurisation des endpoints sensibles et audit de sécurité
