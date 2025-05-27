# Sécuriser son application Spring Boot avec HTTPS (TLS/SSL)

Ce guide explique comment activer HTTPS dans une application Spring Boot, étape par étape, pour garantir la confidentialité des échanges.

---

## 1. Pourquoi HTTPS ?
- Chiffre les échanges entre le client et le serveur
- Protège contre l’interception des données (ex : mots de passe, tokens)
- Indispensable pour toute API exposée sur Internet

---

## 2. Générer un certificat auto-signé (pour tests)
Dans un terminal, exécute :
```bash
keytool -genkeypair -alias moncertificat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 365
```
- Mot de passe conseillé : `changeit` (à personnaliser)
- Le fichier `keystore.p12` sera généré dans le dossier courant

---

## 3. Configurer Spring Boot pour HTTPS
Dans `src/main/resources/application.yml` :
```yaml
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: changeit
    key-store-type: PKCS12
    key-alias: moncertificat
```

---

## 4. Lancer l’application
Démarre Spring Boot normalement (`mvn spring-boot:run`).
- Accède à https://localhost:8443
- Ignore l’avertissement du navigateur (certificat auto-signé)

---

## 5. Bonnes pratiques
- Utiliser un certificat signé par une autorité pour la production (Let’s Encrypt, etc.)
- Ne jamais exposer le mot de passe du keystore dans le code source
- Toujours forcer le HTTPS (redirection depuis HTTP)
- Renouveler régulièrement les certificats

---

## 6. Aller plus loin
- [Spring Boot SSL Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.server.server.ssl.enabled)
- [Let’s Encrypt](https://letsencrypt.org/)

---

> Prochain atelier : Contrôle d’accès avancé (RBAC, rôles, permissions)
