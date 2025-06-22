# Git pour les nuls : Cheatsheet

📑 **Sommaire**
1. [C’est quoi Git ?](#1-cest-quoi-git-)
2. [Initialiser un dépôt](#2-initialiser-un-dépôt)
3. [Cloner un dépôt existant](#3-cloner-un-dépôt-existant)
4. [Ajouter et valider des changements](#4-ajouter-et-valider-des-changements)
5. [Branches](#5-branches)
6. [Fusion et résolution de conflits](#6-fusion-et-résolution-de-conflits)
7. [Astuces Git](#7-astuces-git)

## 1. C’est quoi Git ?
Git est un outil pour sauvegarder, partager et collaborer sur du code. Il garde l’historique de tous les changements.

---

## 2. Initialiser un dépôt
```bash
git init
```

## 3. Cloner un dépôt existant
```bash
git clone <url-du-depot>
```

## 4. Ajouter et valider des changements
```bash
git add <fichier>
git commit -m "Message clair"
```

## 5. Envoyer sur GitHub
```bash
git push origin main
```

## 6. Récupérer les dernières modifications
```bash
git pull origin main
```

## 7. Voir l’historique
```bash
git log
```

## 8. Annuler une modification
```bash
git checkout -- <fichier>
```

---

## Astuces
- Toujours écrire un message de commit clair
- Utilise `git status` pour voir où tu en es
- Ne jamais partager le dossier `.git` sans raison

---

> Pour aller plus loin : [Documentation Git](https://git-scm.com/doc)
