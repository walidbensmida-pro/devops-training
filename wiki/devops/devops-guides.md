# Découvrir le DevOps : Guide pour Débutant

Ce guide explique, étape par étape et sans prérequis technique, les bases du DevOps, l’automatisation, le CI/CD, le déploiement, et les outils incontournables (Jenkins, Kaniko, Vault, GKE, etc.).

---

## 1. C’est quoi le DevOps ?
- **DevOps** = Développement + Opérations. C’est une façon de travailler où les développeurs et les administrateurs système collaborent pour livrer plus vite, plus souvent, et plus fiable.
- **Objectif** : Automatiser, tester, déployer, surveiller, et améliorer en continu.

**Schéma** :
```
[Code] -> [Build] -> [Test] -> [Déploiement] -> [Surveillance] -> [Amélioration]
```

---

## 2. Les outils DevOps du projet
- **Jenkins** : Automatisation des tâches (CI/CD)
- **Kaniko** : Construction d’images Docker sans accès root
- **Vault** : Gestion sécurisée des secrets
- **GKE** : Google Kubernetes Engine, pour déployer des applications en cluster

---

## 3. Pipeline CI/CD (pour les nuls)
- **CI** (Intégration Continue) : tester automatiquement le code à chaque modification
- **CD** (Déploiement Continu) : déployer automatiquement en production

**Exemple de pipeline** :
```
[Push code] -> [Jenkins] -> [Tests] -> [Build Docker] -> [Déploiement GKE]
```

---

## 4. Exemples concrets du projet
- [Méthodologie ACID pour projet DevOps](acid.md)
- [Déploiement Jenkins sur GKE (partie 1)](deploy-jenkins-gke.md)
- [Déploiement Jenkins sur GKE (partie 2)](deploy-jenkins-gke-part-2.md)
- [Utilisation de Kaniko pour build Docker sans Docker](kaniko.md)
- [Déploiement de Vault sur GKE](vault.md)

---

## 5. Bonnes pratiques DevOps
- Automatiser tout ce qui peut l’être
- Versionner la configuration (infrastructure as code)
- Tester avant de déployer
- Surveiller l’application en production
- Documenter chaque étape

---

## 6. Pour aller plus loin
- [Jenkins](https://www.jenkins.io/doc/)
- [Kubernetes](https://kubernetes.io/fr/docs/concepts/)
- [HashiCorp Vault](https://www.vaultproject.io/docs)
- [Kaniko](https://github.com/GoogleContainerTools/kaniko)

---

> Ce guide est volontairement vulgarisé pour permettre à toute personne, même sans expérience DevOps, de comprendre et d’implémenter les bases du DevOps dans un projet moderne.
