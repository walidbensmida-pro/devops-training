# Wiki Sécurité

Ce dossier regroupe les guides sur la sécurité applicative, JWT, Spring Security, gestion multi-comptes GitHub, etc.

# Dossier `security/`

Ce dossier regroupe tous les guides et fiches liés à la sécurité :
- Sécurité Spring Boot (JWT, OAuth2, Keycloak)
- Multi-comptes GitHub

## Fichiers principaux
- `securite-springboot.md` : Concepts et comparatif sécurité Spring
- `jwt-springboot-guide.md` : Guide pratique JWT avec Spring Boot
- `multi-account-github-setup.md` : Gérer plusieurs comptes GitHub sur un même PC

---

# Formation Sécurité DevOps – Module 2 : Gestion des secrets et variables sensibles

## Pourquoi protéger les secrets ?
Un « secret » (mot de passe, clé API, token…) permet d’accéder à des ressources sensibles. S’il est exposé, un attaquant peut prendre le contrôle de vos services ou voler des données.

---

## 1. Où trouve-t-on des secrets ?
- Fichiers de configuration (`application.yml`, `.env`)
- Variables d’environnement
- Pipelines CI/CD (GitHub Actions, GitLab CI…)
- Plateformes cloud (GCP, AWS, Azure)

---

## 2. Mauvaises pratiques à éviter
- Mettre des secrets en clair dans le code ou les dépôts Git
- Partager des secrets par email ou chat
- Réutiliser le même secret partout

---

## 3. Bonnes pratiques
- Utiliser des variables d’environnement pour injecter les secrets
- Ne jamais versionner les fichiers contenant des secrets (`.env`, `application-prod.yml`)
- Renouveler régulièrement les secrets
- Limiter les droits (principe du moindre privilège)

---

## 4. Outils pour gérer les secrets
- **Vault** (HashiCorp) : coffre-fort centralisé pour secrets
- **Kubernetes Secrets** : objets dédiés pour stocker des infos sensibles
- **AWS Secrets Manager / GCP Secret Manager**
- **Doppler, SOPS, etc.**

---

## 5. Exemple : Utiliser Vault avec Kubernetes
1. Installer Vault (voir `wiki/devops/vault.md`)
2. Stocker un secret dans Vault :
   ```bash
   vault kv put secret/maapp db_password=SuperSecret123
   ```
3. Configurer l’application pour lire le secret depuis Vault (via un sidecar ou un init container)

---

## 6. Exercice pratique
- Repérer les secrets dans un projet (fichiers, variables…)
- Proposer une solution pour les sécuriser (ex : déplacer dans Vault ou dans des variables d’environnement)

---

> Prochain module : Sécurisation des communications (HTTPS, certificats, TLS)

---

Pour toute nouvelle fiche sécurité, placez-la ici.
