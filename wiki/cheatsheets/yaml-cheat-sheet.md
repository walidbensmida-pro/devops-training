# YAML pour les nuls : Cheatsheet

## 1. C’est quoi YAML ?
YAML est un format de fichier texte pour décrire des données (configurations, déploiements, etc.). Il est utilisé partout en DevOps (Kubernetes, GitHub Actions, etc.).

---

## 2. Règles de base
- L’indentation (espaces) est très importante !
- Pas de tabulations, que des espaces
- Les listes commencent par un tiret `-`

---

## 3. Exemples
- **Clé/valeur** :
```yaml
nom: Alice
age: 30
```
- **Liste** :
```yaml
fruits:
  - pomme
  - banane
  - orange
```
- **Objet imbriqué** :
```yaml
personne:
  nom: Bob
  age: 25
```

---

## 4. Astuces
- Toujours vérifier l’indentation
- Utiliser un éditeur qui colore le YAML
- Valider le YAML sur [yamlvalidator.com](https://yamlvalidator.com/)

---

> Pour aller plus loin : [YAML Tutorial](https://yaml.org/)
