# Kubernetes pour les enfants (et les grands débutants)

Voici un guide ultra-simple pour comprendre les fichiers principaux de Kubernetes. Idéal si tu découvres le sujet !

---

# Sommaire

- [Kubernetes pour les enfants (et les grands débutants)](#kubernetes-pour-les-enfants-et-les-grands-débutants)
- [Sommaire](#sommaire)
  - [1. Deployment](#1-deployment)
    - [Exemple simple](#exemple-simple)
    - [Exemple avancé (avec variables, ressources, probes)](#exemple-avancé-avec-variables-ressources-probes)
  - [2. Service](#2-service)
    - [Exemple simple (ClusterIP)](#exemple-simple-clusterip)
    - [Exemple NodePort](#exemple-nodeport)
  - [3. Ingress](#3-ingress)
    - [Exemple simple](#exemple-simple-1)
    - [Exemple avec plusieurs paths et TLS](#exemple-avec-plusieurs-paths-et-tls)
  - [4. PersistentVolumeClaim (PVC)](#4-persistentvolumeclaim-pvc)
    - [Exemple simple](#exemple-simple-2)
    - [Exemple avec storageClass](#exemple-avec-storageclass)
  - [5. Job](#5-job)
    - [Exemple simple](#exemple-simple-3)
    - [Exemple avancé (avec parallelism et backoffLimit)](#exemple-avancé-avec-parallelism-et-backofflimit)
  - [6. Secret](#6-secret)
    - [Exemple simple (Opaque)](#exemple-simple-opaque)
    - [Exemple TLS](#exemple-tls)
  - [7. ConfigMap](#7-configmap)
    - [Exemple simple](#exemple-simple-4)
    - [Exemple avec plusieurs clés et fichier](#exemple-avec-plusieurs-clés-et-fichier)
  - [8. ServiceAccount](#8-serviceaccount)
    - [Exemple simple](#exemple-simple-5)
    - [Exemple avec annotation cloud](#exemple-avec-annotation-cloud)
  - [9. RBAC (Role, RoleBinding)](#9-rbac-role-rolebinding)
    - [Exemple Role (lecture sur pods)](#exemple-role-lecture-sur-pods)
    - [Exemple RoleBinding](#exemple-rolebinding)
  - [10. Namespace](#10-namespace)
    - [Exemple simple](#exemple-simple-6)
    - [Exemple avec labels](#exemple-avec-labels)
  - [11. Schéma du routage Ingress (exemple)](#11-schéma-du-routage-ingress-exemple)
  - [Le contrôleur nginx-ingress expliqué simplement](#le-contrôleur-nginx-ingress-expliqué-simplement)
      - [Schéma visuel](#schéma-visuel)
  - [Notions Kubernetes à connaître (FAQ débutant)](#notions-kubernetes-à-connaître-faq-débutant)
    - [C’est quoi un LoadBalancer ?](#cest-quoi-un-loadbalancer-)
    - [C’est quoi un Namespace ?](#cest-quoi-un-namespace-)
    - [C’est quoi un Ingress Controller (ex : nginx) ?](#cest-quoi-un-ingress-controller-ex--nginx-)
    - [Autres notions utiles](#autres-notions-utiles)
  - [Exemple ultra-simple](#exemple-ultra-simple)
    - [Deployment](#deployment)
    - [Service](#service)
    - [Ingress](#ingress)
    - [ClusterIP, NodePort, LoadBalancer : quelle différence ?](#clusterip-nodeport-loadbalancer--quelle-différence-)

---

## 1. Deployment
- **C’est quoi ?**
  - C’est le chef d’orchestre : il dit combien de copies (pods) de ton appli tu veux, et s’occupe de les lancer et relancer si besoin.
- **Propriétés principales :**
  - `replicas` : combien de copies de ton appli ? (ex : 1, 2, 3...)
  - `selector` : comment reconnaître les bons pods ? (souvent un label comme `app: devops-training`)
  - `template` : la recette pour créer chaque pod (image Docker, variables, ports...)
    - `metadata.labels` : étiquette pour retrouver le pod
    - `spec.containers` : liste des conteneurs à lancer
      - `name` : nom du conteneur
      - `image` : image Docker à utiliser
      - `ports` : ports exposés
      - `env` : variables d’environnement
      - `resources` : limites CPU/mémoire
      - `volumeMounts` : où monter les volumes
    - `volumes` : stockage partagé entre conteneurs
  - `strategy` : comment faire les mises à jour (RollingUpdate, Recreate)
  - `minReadySeconds` : temps minimum pour considérer un pod comme prêt
  - `revisionHistoryLimit` : combien d’anciennes versions garder
  - `env` : variables d’environnement pour le conteneur
  - `resources` : limites CPU/mémoire
  - `livenessProbe` / `readinessProbe` : vérifications de santé

---

### Exemple simple
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mon-appli
spec:
  replicas: 2
  selector:
    matchLabels:
      app: mon-appli
  template:
    metadata:
      labels:
        app: mon-appli
    spec:
      containers:
        - name: mon-appli
          image: monimage:latest
          ports:
            - containerPort: 8080
```

### Exemple avancé (avec variables, ressources, probes)
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mon-appli
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
  minReadySeconds: 5
  revisionHistoryLimit: 2
  selector:
    matchLabels:
      app: mon-appli
  template:
    metadata:
      labels:
        app: mon-appli
    spec:
      containers:
        - name: mon-appli
          image: monimage:latest
          env:
            - name: ENV
              value: production
          resources:
            requests:
              memory: "128Mi"
              cpu: "250m"
            limits:
              memory: "256Mi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 5
          readinessProbe:
            httpGet:
              path: /ready
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 5
```

---

## 2. Service
- **C’est quoi ?**
  - C’est le standardiste : il donne une adresse stable à tes pods, et gère qui peut leur parler (et comment).
- **Propriétés principales :**
  - `type` : comment exposer le service ? (ClusterIP, NodePort, LoadBalancer)
  - `selector` : quels pods sont derrière ce service ?
  - `ports` : quels ports exposer ? (port du service, port du pod)
  - `targetPort` : port du conteneur cible
  - `nodePort` : port ouvert sur chaque nœud (pour type NodePort)
  - `clusterIP` : IP interne du service (auto ou fixée)
  - `externalName` : alias DNS externe (pour type ExternalName)

---

### Exemple simple (ClusterIP)
```yaml
apiVersion: v1
kind: Service
metadata:
  name: mon-service
spec:
  selector:
    app: mon-appli
  ports:
    - port: 80
      targetPort: 8080
```

### Exemple NodePort
```yaml
apiVersion: v1
kind: Service
metadata:
  name: mon-service-nodeport
spec:
  type: NodePort
  selector:
    app: mon-appli
  ports:
    - port: 80
      targetPort: 8080
      nodePort: 30080
```

---

## 3. Ingress
- **C’est quoi ?**
  - C’est le portier du cluster : il gère les entrées HTTP/HTTPS et redirige vers le bon service selon l’URL ou le nom de domaine.
- **Propriétés principales :**
  - `ingressClassName` : quel Ingress Controller utiliser ? (ex : nginx)
  - `rules` : quelles routes (host/path) vers quels services ?
  - `tls` : config HTTPS (certificats)
  - `backend` : service cible par défaut
  - `annotations` : options avancées (auth, rewrite, etc.)

---

### Exemple simple
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: mon-ingress
spec:
  ingressClassName: nginx
  rules:
    - host: monapp.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: mon-service
                port:
                  number: 80
```

### Exemple avec plusieurs paths et TLS
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: multi-ingress
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - monapp.local
      secretName: mon-tls-secret
  rules:
    - host: monapp.local
      http:
        paths:
          - path: /api
            pathType: Prefix
            backend:
              service:
                name: api-service
                port:
                  number: 80
          - path: /web
            pathType: Prefix
            backend:
              service:
                name: web-service
                port:
                  number: 80
```

---

## 4. PersistentVolumeClaim (PVC)
- **C’est quoi ?**
  - C’est la demande de stockage : tu demandes un espace disque pour tes pods (comme une clé USB qu’on branche à l’appli).
- **Propriétés principales :**
  - `accessModes` : comment accéder au volume ? (ReadWriteOnce, ReadOnlyMany, ReadWriteMany)
  - `resources.requests.storage` : combien d’espace ?
  - `storageClassName` : type de stockage (rapide, standard, etc.)
  - `volumeMode` : mode Block ou Filesystem

---

### Exemple simple
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mon-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

### Exemple avec storageClass
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mon-pvc-rapide
spec:
  accessModes:
    - ReadWriteOnce
  storageClassName: fast
  resources:
    requests:
      storage: 5Gi
```

---

## 5. Job
- **C’est quoi ?**
  - C’est une tâche à usage unique : il lance un ou plusieurs pods pour faire un boulot, puis s’arrête (ex : migration de base, script ponctuel).
- **Propriétés principales :**
  - `completions` : combien de fois exécuter le job ?
  - `parallelism` : combien de pods en parallèle ?
  - `backoffLimit` : combien de tentatives en cas d’échec ?
  - `restartPolicy` : comportement en cas d’échec (Always, OnFailure, Never)
  - `activeDeadlineSeconds` : durée max d’exécution

---

### Exemple simple
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: mon-job
spec:
  template:
    spec:
      containers:
        - name: script
          image: busybox
          command: ["echo", "Hello World"]
      restartPolicy: Never
```

### Exemple avancé (avec parallelism et backoffLimit)
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: job-parallel
spec:
  completions: 5
  parallelism: 2
  backoffLimit: 3
  template:
    spec:
      containers:
        - name: script
          image: busybox
          command: ["sh", "-c", "echo $RANDOM"]
      restartPolicy: OnFailure
```

---

## 6. Secret
- **C’est quoi ?**
  - C’est le coffre-fort : il stocke des infos sensibles (mots de passe, clés, certifs) de façon sécurisée.
- **Propriétés principales :**
  - `type` : générique (Opaque), clé TLS, Docker, etc.
  - `data` : paires clé/valeur encodées en base64
  - `stringData` : paires clé/valeur en clair (K8s encode pour toi)

---

### Exemple simple (Opaque)
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mon-secret
stringData:
  password: supermotdepasse
```

### Exemple TLS
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mon-tls-secret
  namespace: default
  labels:
    app: mon-app
  annotations:
    description: "Certificat TLS pour monapp.local"
type: kubernetes.io/tls
data:
  tls.crt: <base64-cert>
  tls.key: <base64-key>
```

---

## 7. ConfigMap
- **C’est quoi ?**
  - C’est le classeur de configs : il stocke des variables ou fichiers de config non sensibles pour tes applis.
- **Propriétés principales :**
  - `data` : paires clé/valeur (texte)
  - `binaryData` : paires clé/valeur (binaire, base64)

---

### Exemple simple
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ma-config
  labels:
    app: mon-app
  annotations:
    description: "Config pour mon app"
data:
  DB_HOST: localhost
  DB_PORT: "5432"
```

### Exemple avec plusieurs clés et fichier
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: config-fichiers
  namespace: default
data:
  app.conf: |
    param1=foo
    param2=bar
  settings.json: |
    { "debug": true }
```

---

## 8. ServiceAccount
- **C’est quoi ?**
  - C’est l’identité d’un pod pour accéder à l’API Kubernetes ou à d’autres services.
- **Propriétés principales :**
  - `secrets` : secrets associés (tokens)
  - `annotations` : infos pour lier à des permissions cloud (ex : GCP, AWS)

---

### Exemple simple
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: mon-serviceaccount
```

### Exemple avec annotation cloud
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: sa-gcp
  annotations:
    iam.gke.io/gcp-service-account: "mon-compte@projet.iam.gserviceaccount.com"
```

---

## 9. RBAC (Role, RoleBinding)
- **C’est quoi ?**
  - C’est le système de permissions : qui a le droit de faire quoi dans le cluster ?
- **Propriétés principales :**
  - `Role` : liste d’actions autorisées sur des ressources (verbs, resources, apiGroups)
  - `RoleBinding` : à qui donner ce rôle ? (users, groups, serviceAccounts)

---

### Exemple Role (lecture sur pods)
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: lecture-pods
rules:
  - apiGroups: [""]
    resources: ["pods"]
    verbs: ["get", "list", "watch"]
```

### Exemple RoleBinding
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: lecture-pods-binding
  namespace: default
subjects:
  - kind: ServiceAccount
    name: mon-serviceaccount
    namespace: default
roleRef:
  kind: Role
  name: lecture-pods
  apiGroup: rbac.authorization.k8s.io
```

---

## 10. Namespace
- **C’est quoi ?**
  - C’est un dossier virtuel : il permet d’isoler des groupes de ressources (ex : dev, prod, test).
- **Propriétés principales :**
  - `name` : nom du namespace
  - `labels` : pour organiser ou filtrer

---

### Exemple simple
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: dev
```

### Exemple avec labels
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: prod
  labels:
    environment: production
    team: backend
```

---

## 11. Schéma du routage Ingress (exemple)

```
[Internet]
    |
[IP Publique unique]
    |
[nginx-ingress Controller]
    |
+-------------------+-------------------+
|                   |                   |
[Ingress: keycloak] [Ingress: jenkins] [Ingress: devops-training]
    |                   |                   |
[Service]         [Service]           [Service]
    |                   |                   |
[Pods]             [Pods]               [Pods]
```

- **Explication** :
  - Une seule IP publique pointe vers le contrôleur nginx-ingress.
  - Chaque Ingress (Keycloak, Jenkins, etc.) définit des règles de routage (host/path).
  - Le contrôleur redirige vers le bon Service, qui envoie vers les bons Pods.

---

## Le contrôleur nginx-ingress expliqué simplement

- **C’est quoi ?**
  - C’est un programme (un pod) qui tourne dans ton cluster Kubernetes.
  - Il lit les objets Ingress et configure automatiquement un reverse proxy nginx pour router le trafic HTTP/HTTPS vers les bons services/pods.

- **Pourquoi il a besoin d’un LoadBalancer ?**
  - Pour que le trafic venant d’Internet puisse entrer dans le cluster, il faut une porte d’entrée : c’est le service de type LoadBalancer associé à nginx-ingress.
  - Le cloud (GCP, AWS…) crée une IP publique et redirige tout le trafic vers le pod nginx-ingress.
  - nginx-ingress se charge ensuite de router ce trafic vers le bon service ClusterIP selon les règles d’Ingress (host, path, etc.).

---

#### Schéma visuel

```
[Internet]
   |
[Service nginx-ingress (LoadBalancer, IP publique)]
   |
[Pod nginx-ingress-controller]
   |
+-------------------+-------------------+
|                   |                   |
[Service ClusterIP] [Service ClusterIP] [Service ClusterIP]
   |                   |                   |
[Pods]             [Pods]               [Pods]
```

- **Résumé** :
  - Le LoadBalancer cloud donne une IP publique.
  - Le pod nginx-ingress écoute sur cette IP et applique les règles d’Ingress.
  - Il redirige vers le bon service interne (ClusterIP), qui envoie vers les pods.

---

## Notions Kubernetes à connaître (FAQ débutant)

### C’est quoi un LoadBalancer ?
- **Définition simple** :
  - Un LoadBalancer (équilibreur de charge) distribue le trafic réseau entre plusieurs serveurs/pods pour éviter la surcharge d’un seul.
- **Dans Kubernetes** :
  - Quand tu crées un Service de type `LoadBalancer`, Kubernetes demande au cloud (GCP, AWS, Azure…) de créer un équilibreur de charge externe qui reçoit le trafic et le redirige vers tes pods.
  - **Sur ton PC ou en local** : il n’y a pas de vrai LoadBalancer, c’est simulé (ex : avec Minikube ou Kind).
- **Est-ce moi qui le crée ?**
  - Non, tu déclares juste le Service en YAML, et le cloud s’occupe de créer l’IP publique et l’équilibreur.
- **Lien avec nginx** :
  - nginx-ingress est un contrôleur qui agit comme un LoadBalancer HTTP/HTTPS à l’intérieur du cluster. Il reçoit le trafic de l’équilibreur cloud (ou du réseau local) et le distribue selon les règles d’Ingress.
  - **Schéma** :
    - [Internet] → [LoadBalancer cloud] → [nginx-ingress Controller] → [Services/Pods]

---

### C’est quoi un Namespace ?
- **Définition simple** :
  - Un Namespace, c’est comme un quartier ou un dossier : il permet de séparer les ressources (pods, services, etc.) pour éviter les conflits et organiser le cluster.
- **À quoi ça sert ?**
  - Isoler les environnements (dev, test, prod)
  - Donner des droits différents à chaque équipe
  - Éviter que deux applis aient le même nom de service/pod
- **Est-ce obligatoire ?**
  - Non, mais c’est une bonne pratique dès que tu as plusieurs applis ou équipes.

---

### C’est quoi un Ingress Controller (ex : nginx) ?
- **Définition simple** :
  - C’est un programme qui écoute les objets Ingress et configure automatiquement un reverse proxy (comme nginx) pour router le trafic HTTP/HTTPS vers les bons services.
- **Pourquoi nginx ?**
  - nginx est populaire, open-source, et très flexible pour gérer le routage, la sécurité, les certificats, etc.
- **Différence avec le LoadBalancer ?**
  - Le LoadBalancer distribue le trafic réseau (TCP/UDP) vers le cluster.
  - L’Ingress Controller (nginx) distribue le trafic HTTP/HTTPS à l’intérieur du cluster, selon les règles d’Ingress (host, path, etc.).

---

### Autres notions utiles
- **Pod** : la plus petite unité déployable (un ou plusieurs conteneurs)
- **Service** : donne une IP stable à tes pods, et permet de les exposer
- **ConfigMap/Secret** : stockent la config (non sensible/sensible)
- **RBAC** : gère les permissions (qui a le droit de faire quoi)

---

## Exemple ultra-simple

### Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mon-appli
spec:
  replicas: 2
  selector:
    matchLabels:
      app: mon-appli
  template:
    metadata:
      labels:
        app: mon-appli
    spec:
      containers:
        - name: mon-appli
          image: monimage:latest
          ports:
            - containerPort: 8080
```

### Service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: mon-appli
spec:
  type: ClusterIP
  selector:
    app: mon-appli
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
```

### Ingress
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: mon-appli-ingress
spec:
  ingressClassName: nginx
  rules:
    - host: monappli.local
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: mon-appli
                port:
                  number: 80
```

---

> Kubernetes, c’est comme une ville :
> - Les pods sont les maisons
> - Les services sont les standards téléphoniques
> - L’ingress est le portier à l’entrée de la ville
> - Les labels sont les numéros de maison
> - Le deployment est le chef de chantier
> - Les secrets sont dans le coffre-fort
> - Les ConfigMaps sont dans le classeur de recettes
> - Les ServiceAccounts ont une carte d’identité
> - Les Roles et RoleBindings font respecter la loi
> - Les namespaces sont des quartiers
>
> Voilà, tu peux relire ce guide à chaque fois que tu bloques sur un fichier YAML Kubernetes !

### ClusterIP, NodePort, LoadBalancer : quelle différence ?

- **ClusterIP** (par défaut)
  - Le service n’est accessible que depuis l’intérieur du cluster Kubernetes (pas depuis Internet).
  - Pas de LoadBalancer externe : seul un pod ou un autre service dans le cluster peut y accéder.
  - **Exemple d’usage** : base de données, backend interne, etc.

- **NodePort**
  - Le service est accessible depuis l’extérieur du cluster, via une IP de nœud et un port spécial (ex : 30080).
  - Pas de LoadBalancer cloud, mais tu peux tester en local ou sur un petit cluster.

- **LoadBalancer**
  - Kubernetes demande au cloud de créer un LoadBalancer externe (avec une IP publique).
  - Le trafic Internet arrive sur cette IP, puis est redirigé vers tes pods.
  - **Exemple d’usage** : exposer une appli web sur Internet.

---

> **Donc :**
> - Si tes services sont en `ClusterIP`, ils n’ont pas de LoadBalancer et ne sont pas accessibles directement depuis Internet.
> - Pour exposer une appli web, tu utilises souvent un Ingress (avec nginx-ingress) : le LoadBalancer cloud pointe vers nginx, qui route ensuite vers tes services ClusterIP.
> - **Schéma** :
>   - [Internet] → [LoadBalancer cloud] → [nginx-ingress Controller] → [Service ClusterIP] → [Pods]

> **Pourquoi tu vois "LoadBalancer" dans ton cluster alors que tes services sont ClusterIP ?**
>
> - Ce n’est pas tes services applicatifs (devops-training, jenkins, etc.) qui sont de type LoadBalancer, mais le service du contrôleur nginx-ingress !
> - Quand tu installes nginx-ingress sur GKE, il crée un service de type LoadBalancer pour obtenir une IP publique.
> - Tous tes Ingress (keycloak, jenkins, etc.) partagent cette même IP publique, car ils passent par nginx-ingress.
> - Tes services applicatifs restent en ClusterIP (internes), et nginx-ingress fait le lien entre l’extérieur (via le LoadBalancer) et l’intérieur du cluster.
>
> **Résumé visuel :**
> [Internet] → [Service nginx-ingress (LoadBalancer)] → [nginx-ingress Controller] → [Service ClusterIP] → [Pods]
>
> C’est pour ça que tu vois une seule IP LoadBalancer partagée pour tous tes Ingress dans l’interface !

---
