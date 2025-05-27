# Déployer un Ingress vraiment portable sur Kubernetes (GKE, Minikube, Cloud...) : Nginx vs Ingress natif GKE

## Problème rencontré

En déployant Keycloak (ou d'autres applications) sur GKE avec l'Ingress natif, l'accès externe ne fonctionnait pas :
- L'Ingress restait en état "UNHEALTHY".
- Le navigateur affichait une page vide ou une erreur (ERR_EMPTY_RESPONSE).
- Pourtant, le service et le pod Keycloak répondaient bien en interne (testé avec wget depuis un pod).

**Cause :**
- L'Ingress natif GKE effectue un health check strict sur `/` et attend un code HTTP 200.
- Beaucoup d'applications (dont Keycloak) redirigent `/` (code 302/303) ou ne répondent pas exactement comme attendu, ce qui fait échouer le health check et bloque l'accès externe.

## Solution : Installer nginx-ingress

nginx-ingress est un contrôleur Ingress open source, portable et très tolérant :
- Il fonctionne sur tous les clusters (GKE, Minikube, AKS, EKS, etc.).
- Il accepte les redirections et gère mieux les cas "réels" d'applications web.
- Il permet d'utiliser les mêmes fichiers Ingress YAML partout, sans adaptation.

### Commande utilisée pour installer nginx-ingress :
```sh
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml
```

### Modification à faire dans chaque Ingress :
Ajoutez la ligne suivante dans la spec :
```yaml
spec:
  ingressClassName: nginx
```

## Différence entre nginx-ingress et l'Ingress natif GKE

| nginx-ingress (open source) | Ingress natif GKE |
|----------------------------|-------------------|
| Fonctionne partout         | Spécifique à GKE  |
| Tolérant aux redirections  | Health check strict|
| Plus de fonctionnalités    | Moins flexible    |
| Portabilité totale         | Adapté GCP only   |

- **nginx-ingress** : idéal pour la portabilité, le développement multi-cloud, et les applications qui ne répondent pas strictement 200 sur `/`.
- **Ingress natif GKE** : bien intégré à Google Cloud, mais strict et parfois source de bugs "incompréhensibles".

## Résumé des étapes
1. Installer nginx-ingress sur le cluster (commande ci-dessus)
2. Ajouter `ingressClassName: nginx` dans tous les manifests Ingress
3. Récupérer l'IP du service `ingress-nginx-controller` et l'utiliser dans le fichier hosts pour chaque host (keycloak.local, jenkins.devops.local, etc.)
4. Accéder à toutes les applications via leur nom d'hôte

---

> Ce guide explique pourquoi il est souvent préférable d'utiliser nginx-ingress pour un cluster Kubernetes portable et sans prise de tête, et comment résoudre les problèmes d'accès liés à l'Ingress natif GKE.
