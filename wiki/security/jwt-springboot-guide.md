<!-- filepath: security/jwt-springboot-guide.md -->
# Sécuriser une API avec JWT dans Spring Boot : Guide pour Débutant

Ce guide explique, pas à pas et sans prérequis en Java, comment sécuriser une API avec des tokens JWT dans un projet Spring Boot. Il s’adresse à toute personne débutant en développement ou en sécurité.

---

## 1. Pourquoi sécuriser une API ?
Une API (Application Programming Interface) permet à des applications de communiquer entre elles. Sans sécurité, n’importe qui pourrait accéder à vos données ou modifier votre application !

**Objectif :** S’assurer que seules les personnes autorisées peuvent utiliser l’API.

---

## 2. C’est quoi un JWT ?
- **JWT** = JSON Web Token. C’est une sorte de "badge numérique" que l’on donne à un utilisateur après qu’il s’est identifié.
- Il contient des informations (nom, rôle, date d’expiration…) et est signé pour éviter la falsification.
- Il ressemble à ça :
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGljZSIsImV4cCI6MTY4ODg4ODg4OH0.abc123...
```
- On l’utilise pour prouver son identité à chaque requête, comme un ticket de cinéma.

---

## 3. Comment ça marche ? (Schéma)

1. **L’utilisateur s’identifie** (login/mot de passe)
2. **Le serveur vérifie** les infos et génère un JWT
3. **Le client reçoit le JWT** et le stocke (souvent dans le navigateur ou une appli mobile)
4. **À chaque requête**, le client envoie le JWT dans l’en-tête HTTP
5. **Le serveur vérifie le JWT** avant d’accepter la requête

```
[Client] --(login+mdp)--> [Serveur]
[Serveur] --(JWT)--> [Client]
[Client] --(requête + JWT)--> [Serveur]
[Serveur] --(OK si JWT valide)-->
```

---

## 4. Mise en place dans Spring Boot (étape par étape)

### a) Ajouter les dépendances
Dans le fichier `pom.xml` (c’est le fichier qui liste les "briques" de votre projet) :
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>0.9.1</version>
</dependency>
```

### b) Générer un JWT (côté serveur)
Quand un utilisateur s’identifie, on crée un JWT :
```java
String jwt = Jwts.builder()
    .setSubject(username) // le nom de l’utilisateur
    .setIssuedAt(new Date()) // date de création
    .setExpiration(new Date(System.currentTimeMillis() + 864_000_00)) // expire dans 1 jour
    .signWith(SignatureAlgorithm.HS512, "maCleSecrete") // signature
    .compact();
```
- On renvoie ce JWT au client (dans la réponse HTTP).

### c) Stocker le JWT côté client
- Sur le web : dans le stockage local du navigateur (localStorage)
- Sur mobile : dans un "secure storage"

### d) Envoyer le JWT à chaque requête
Le client ajoute le JWT dans l’en-tête HTTP :
```
Authorization: Bearer <le_token_jwt>
```

### e) Vérifier le JWT côté serveur
À chaque requête, le serveur :
- Récupère le JWT dans l’en-tête
- Vérifie la signature et la date d’expiration
- Si tout est OK, il autorise l’accès

Exemple de validation :
```java
Claims claims = Jwts.parser()
    .setSigningKey("maCleSecrete")
    .parseClaimsJws(token)
    .getBody();
String username = claims.getSubject();
```

---

## 5. Intégration dans Spring Security (pour les nuls)

Spring Security est un "garde du corps" pour votre application. On va lui apprendre à reconnaître les JWT.

### a) Créer un filtre JWT
Un filtre, c’est comme un vigile à l’entrée : il vérifie le badge (le JWT) de chaque visiteur.

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

### b) Créer l’utilitaire JWT
C’est une "boîte à outils" pour générer et vérifier les tokens.
```java
@Component
public class JwtUtil {
    private String SECRET_KEY = "maCleSecrete";
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
```

### c) Configurer Spring Security pour utiliser le filtre
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

---

## 6. Conseils et bonnes pratiques
- **Ne jamais exposer la clé secrète** (utilisez une variable d’environnement)
- **Définir une durée de vie courte** pour les tokens (ex : 1h)
- **Utiliser HTTPS** pour éviter l’interception des tokens
- **Rafraîchir les tokens** régulièrement (refresh token)
- **Gérer les erreurs** (token expiré, signature invalide…)
- **Ne stockez jamais le JWT dans un cookie non sécurisé**

---

## 7. Pour aller plus loin
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Exemple de JwtAuthenticationFilter dans le projet](../../src/main/java/com/harington/devops_training/security/JwtAuthenticationFilter.java)
- [Exemple de JwtUtil dans le projet](../../src/main/java/com/harington/devops_training/security/JwtUtil.java)
- [Guide Sécurité Spring Boot](securite-springboot.md)

---

> Ce guide est volontairement vulgarisé pour permettre à toute personne, même sans expérience Java, de comprendre et d’implémenter une sécurité JWT dans un projet Spring Boot.