<!-- filepath: security/securite-springboot.md -->
# Sécuriser une application Spring Boot : Guide pour Débutant

Ce guide explique, étape par étape et sans prérequis en Java, comment sécuriser une application Spring Boot. Il s’adresse à toute personne débutant en développement ou en sécurité.

---

## 1. Pourquoi sécuriser une application ?
Imaginez votre application comme une maison : sans serrure, tout le monde peut entrer ! Sécuriser, c’est mettre des portes, des clés, et décider qui a le droit d’entrer dans chaque pièce.

**Objectif :** Protéger vos données et vos utilisateurs contre les accès non autorisés.

---

## 2. Les bases de la sécurité web
- **Authentification** : vérifier l’identité de l’utilisateur (qui es-tu ?)
- **Autorisation** : vérifier ce que l’utilisateur a le droit de faire (as-tu le droit d’entrer dans cette pièce ?)
- **Chiffrement** : rendre les données illisibles pour les pirates (comme un coffre-fort)

---

## 3. Spring Security, c’est quoi ?
Spring Security est un "garde du corps" pour votre application Java. Il protège vos routes, gère les mots de passe, les rôles, etc.

---

## 4. Mise en place dans Spring Boot (étape par étape)

### a) Ajouter la sécurité à votre projet
Dans le fichier `pom.xml` :
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### b) Comportement par défaut
Dès que vous ajoutez cette dépendance, toutes les pages de votre application sont protégées : il faut s’authentifier pour y accéder.

### c) Personnaliser la sécurité (qui a accès à quoi ?)
Exemple : ouvrir l’accès à `/public`, protéger `/admin` :
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .formLogin();
    }
}
```

**Schéma d’accès** :
```
[Utilisateur] --(accès /public)--> [OK]
[Utilisateur] --(accès /admin)--> [Vérif rôle ADMIN]
```

---

## 5. Gérer les utilisateurs

### a) En mémoire (pour tester)
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("user").password("{noop}userpass").roles("USER")
        .and()
        .withUser("admin").password("{noop}adminpass").roles("ADMIN");
}
```

### b) En base de données (pour la vraie vie)
- Implémentez `UserDetailsService` pour charger les utilisateurs depuis la BDD.
- Exemple :
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), getAuthorities(user));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
    }
}
```

---

## 6. Sécuriser les mots de passe
Toujours chiffrer les mots de passe ! Utilisez BCrypt (un algorithme de hachage sécurisé) :
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## 7. Authentification avancée : JWT
Pour les API modernes, on utilise souvent des tokens JWT (voir le guide dédié : [Guide JWT avec Spring Boot](jwt-springboot-guide.md)).

**Schéma** :
```
[Client] --(login+mdp)--> [Serveur]
[Serveur] --(JWT)--> [Client]
[Client] --(requête + JWT)--> [Serveur]
[Serveur] --(OK si JWT valide)-->
```

---

## 8. Bonnes pratiques
- Toujours chiffrer les mots de passe (BCrypt)
- Utiliser HTTPS
- Limiter les droits par défaut
- Mettre à jour les dépendances de sécurité
- Protéger les endpoints sensibles
- Journaliser les accès et tentatives d’intrusion

---

## 9. Pour aller plus loin
- [Guide JWT avec Spring Boot](jwt-springboot-guide.md)
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Exemple de UserDetailsService dans le projet](../../src/main/java/com/harington/devops_training/service/CustomUserDetailsService.java)

---

> Ce guide est volontairement vulgarisé pour permettre à toute personne, même sans expérience Java, de comprendre et d’implémenter la sécurité dans un projet Spring Boot.