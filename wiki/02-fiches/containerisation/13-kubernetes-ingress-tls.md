# Kubernetes Ingress, HTTPS et Cert-Manager : Guide ultra-pédagogique

## Objectif
Rendre accessible à tous la configuration d’un cluster Kubernetes avec Ingress, HTTPS automatique (Let’s Encrypt), et la gestion des domaines publics pour exposer ses applications de façon sécurisée.

---

## 0. HTTPS pour les nuls : c’est quoi et pourquoi ?

- **HTTPS** (HyperText Transfer Protocol Secure) est la version sécurisée de HTTP, le protocole qui permet d’afficher les sites web.
- Avec HTTPS, toutes les données échangées entre ton navigateur et le site sont **chiffrées** : personne ne peut les lire ou les modifier (ni pirates, ni opérateurs, ni WiFi public).
- Un site en HTTPS affiche un **cadenas** dans la barre d’adresse du navigateur.
- HTTPS protège :
  - Tes mots de passe, données personnelles, paiements, etc.
  - L’intégrité du site (pas de modification ou d’injection par un tiers).
- Pour activer HTTPS, il faut un **certificat** délivré par une autorité reconnue (ex : Let’s Encrypt).
- Sans HTTPS, les navigateurs affichent des alertes de sécurité et bloquent parfois l’accès.

---

## 1. Pourquoi utiliser Ingress et Cert-Manager ?

- **Ingress** permet d’exposer plusieurs applications (Jenkins, Keycloak, Vault, etc.) via une seule IP publique, en utilisant des sous-domaines (jenkins.mondomaine.com, etc.).
- **Cert-Manager** automatise la génération et le renouvellement des certificats HTTPS (Let’s Encrypt), pour que vos sites soient sécurisés sans effort.
- **Let’s Encrypt** fournit gratuitement des certificats reconnus par tous les navigateurs.

---

## 2. Architecture simplifiée

```
[Internet]
    |
[IP Publique LoadBalancer GKE]
    |
[nginx-ingress Controller]
    |
+-------------------+
| Ingress           |
| - jenkins.mondomaine.com  --> Service Jenkins
| - keycloak.mondomaine.com --> Service Keycloak
| - vault.mondomaine.com    --> Service Vault
+-------------------+
```

---

## 3. Étapes détaillées

### a) Préparer le DNS
- Acheter un domaine (ex : OVH).
- Créer des enregistrements A pour chaque sous-domaine (jenkins, keycloak, vault, etc.) pointant vers l’IP publique du LoadBalancer nginx-ingress.
- Vérifier la propagation DNS avec `nslookup`.

### b) Déployer nginx-ingress
- Installer le contrôleur nginx-ingress sur le cluster GKE.
- Il gère le routage HTTP/HTTPS pour tous les services exposés.

### c) Installer cert-manager et ClusterIssuer
- Installer cert-manager (Helm ou manifest officiel).
- Déployer un ClusterIssuer pour Let’s Encrypt (production ou staging).

Exemple de ClusterIssuer :
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: votremail@domaine.com
    privateKeySecretRef:
      name: letsencrypt-prod
    solvers:
      - http01:
          ingress:
            class: nginx
```

### d) Créer les Ingress pour chaque application
- Un Ingress par application, avec :
  - L’annotation `cert-manager.io/cluster-issuer: "letsencrypt-prod"`
  - La section `tls` avec le bon host et secretName
  - La règle host correspondant au sous-domaine

Exemple :
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: jenkins-ingress
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - jenkins.mondomaine.com
      secretName: jenkins-mondomaine-com-tls
  rules:
    - host: jenkins.mondomaine.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: jenkins
                port:
                  number: 8080
```

### e) Appliquer les manifests
- `kubectl apply -f <fichier>` pour chaque Ingress et ClusterIssuer.

### f) Vérifier la génération des certificats
- `kubectl describe certificate <secretName> -n <namespace>`
- Si le certificat est en erreur, vérifier :
  - La propagation DNS (le sous-domaine pointe-t-il bien sur l’IP du cluster ?)
  - Que le challenge HTTP n’est pas bloqué par une redirection automatique HTTP→HTTPS
  - Les events cert-manager : `kubectl get events -A | findstr cert-manager`

### g) Tester l’accès HTTPS
- Naviguer sur https://jenkins.mondomaine.com, vérifier le cadenas vert.
- Si erreur de certificat, vérifier le DNS et la configuration Ingress.

---

## 4. FAQ et dépannage

**Q : Pourquoi le certificat Let’s Encrypt échoue ?**
- Le DNS ne pointe pas sur la bonne IP (vérifier avec nslookup).
- Le challenge HTTP est redirigé vers HTTPS (erreur 308) : désactiver temporairement la redirection avec l’annotation `nginx.ingress.kubernetes.io/ssl-redirect: "false"`.
- Le port 80 n’est pas ouvert ou accessible.

**Q : Comment relancer la génération d’un certificat ?**
- Supprimer le certificat échoué :
  ```
  kubectl delete certificate <secretName> -n <namespace>
  ```
- Cert-manager va automatiquement en régénérer un.

**Q : Peut-on exposer plusieurs applications sur la même IP ?**
- Oui, grâce à Ingress et aux sous-domaines.

---

## 5. Schéma récapitulatif

```
[Internet]
    |
[IP Publique LoadBalancer]
    |
[nginx-ingress]
    |
+-------------------+
| Ingress           |
| - jenkins.mondomaine.com  --> Service Jenkins
| - keycloak.mondomaine.com --> Service Keycloak
| - vault.mondomaine.com    --> Service Vault
+-------------------+
```

---

## 6. Conseils pour les débutants
- Toujours vérifier la propagation DNS avant de demander un certificat.
- Utiliser des outils comme nslookup ou dig pour diagnostiquer.
- Lire les events cert-manager pour comprendre les erreurs.
- Ne pas hésiter à supprimer/recréer un certificat en cas de souci.

---

## 7. Ressources utiles
- [Documentation cert-manager](https://cert-manager.io/docs/)
- [Let’s Encrypt](https://letsencrypt.org/)
- [Kubernetes Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
- [nginx-ingress](https://kubernetes.github.io/ingress-nginx/)

---

*Document rédigé pour les débutants, avec retour d’expérience réel sur GKE, OVH, cert-manager et Let’s Encrypt.*
