# Kubernetes pour les nuls : Cheatsheet

## 1. C’est quoi Kubernetes ?
Kubernetes (K8s) est un outil pour gérer et déployer des applications "en conteneurs" (comme Docker) sur plusieurs serveurs.

---

## 2. Commandes de base
- Voir les pods (applications qui tournent) :
```bash
kubectl get pods
```
- Voir les services (points d’accès réseau) :
```bash
kubectl get svc
```
- Déployer un fichier YAML :
```bash
kubectl apply -f mon-fichier.yaml
```
- Supprimer un déploiement :
```bash
kubectl delete -f mon-fichier.yaml
```
- Voir les logs d’un pod :
```bash
kubectl logs nom-du-pod
```

---

## 3. Astuces
- Toujours vérifier le namespace :
```bash
kubectl config get-contexts
```
- Utiliser `kubectl describe` pour plus de détails
- Utiliser `kubectl exec -it nom-du-pod -- bash` pour entrer dans un pod

---

> Pour aller plus loin : [Kubernetes Docs](https://kubernetes.io/fr/docs/concepts/)
