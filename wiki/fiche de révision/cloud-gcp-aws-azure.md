# Fiche de révision : Cloud (GCP, AWS, Azure)

---

## Concepts clés

- **IaaS, PaaS, SaaS** :
  - IaaS : Infrastructure as a Service (VM, stockage, réseau). Ex : EC2 (AWS), Compute Engine (GCP), VM (Azure).
  - PaaS : Platform as a Service (déploiement d’apps sans gérer l’infra). Ex : App Engine, Elastic Beanstalk, Azure App Service.
  - SaaS : Software as a Service (appli clé en main). Ex : Gmail, Office 365.

- **Serverless** :
  - Exécution de code sans gérer de serveur (scalabilité automatique, facturation à l’usage). Ex : AWS Lambda, Google Cloud Functions, Azure Functions.

- **IAM (Identity & Access Management)** :
  - Gestion fine des droits d’accès aux ressources cloud (utilisateurs, rôles, policies). Ex : IAM AWS, IAM GCP, RBAC Azure.

- **Stockage** :
  - Objets : S3 (AWS), GCS (GCP), Blob Storage (Azure).
  - Fichiers : EFS, Filestore, Azure Files.
  - Bases de données managées : RDS, Cloud SQL, CosmosDB.

- **Réseau & sécurité** :
  - VPC, sous-réseaux, firewall, VPN, peering, security groups.
  - Chiffrement, gestion des secrets (KMS, Secret Manager, Key Vault).

- **Monitoring & observabilité** :
  - Logs, métriques, alertes. Ex : CloudWatch (AWS), Stackdriver (GCP), Azure Monitor.

- **Facturation & optimisation** :
  - Comprendre la tarification, surveiller les coûts, utiliser les budgets et alertes.

---

## Astuces entretien & réponses types

- **Différence IaaS / PaaS / SaaS** :
  - IaaS = tu gères tout (VM, OS, réseau). PaaS = tu déploies ton code, le cloud gère l’infra. SaaS = tu utilises une appli prête à l’emploi.

- **Expliquer le principe du serverless** :
  - Tu écris une fonction, le cloud la déploie, la scale et la facture à l’exécution. Idéal pour des tâches ponctuelles ou des APIs événementielles.

- **À quoi sert IAM ?**
  - À contrôler qui accède à quoi dans le cloud, avec des rôles et des permissions fines. Sécurité essentielle.

- **Qu’est-ce qu’un bucket ?**
  - Un espace de stockage d’objets (fichiers) dans le cloud. Ex : S3, GCS, Blob Storage.

- **Citer un service serverless par cloud** :
  - AWS : Lambda. GCP : Cloud Functions. Azure : Azure Functions.

- **Comment sécuriser ses ressources cloud ?**
  - IAM strict, chiffrement, audit, segmentation réseau, rotation des secrets, MFA.

- **Comment surveiller et optimiser les coûts ?**
  - Utiliser les outils natifs (Billing, Budgets, Cost Explorer), taguer les ressources, supprimer les ressources inutiles.

- **Exemple de déploiement d’une app sur le cloud** :
  - Dockeriser l’app, push sur un registry (ECR, GCR, ACR), déployer sur un service managé (ECS, GKE, AKS).

---

## Questions d'entretien & cas pratiques (avec réponses synthétiques)

- **Qu’est-ce qu’un bucket ?**
  - Un espace de stockage d’objets dans le cloud (S3, GCS, Blob Storage).

- **À quoi sert IAM ?**
  - À gérer les droits d’accès aux ressources cloud.

- **Citer un service serverless par cloud.**
  - AWS Lambda, Google Cloud Functions, Azure Functions.

- **Différence entre IaaS, PaaS et SaaS ?**
  - IaaS = VM, PaaS = plateforme de déploiement, SaaS = appli clé en main.

- **Comment sécuriser un accès à une base cloud ?**
  - VPC privé, IAM, chiffrement, audit, rotation des credentials.

- **Comment déployer une app conteneurisée sur le cloud ?**
  - Build image, push sur registry, déploiement sur service managé (ECS, GKE, AKS).

- **Comment surveiller la santé d’une appli cloud ?**
  - Logs, métriques, alertes via CloudWatch, Stackdriver, Azure Monitor.

- **Comment éviter une facture surprise ?**
  - Budgets, alertes, suppression des ressources inutiles, monitoring des coûts.

- **Expliquer le principe du multi-cloud.**
  - Utiliser plusieurs fournisseurs pour éviter le lock-in, améliorer la résilience ou optimiser les coûts.

- **Qu’est-ce qu’un VPC ?**
  - Virtual Private Cloud : réseau isolé dans le cloud pour contrôler le trafic et la sécurité.
