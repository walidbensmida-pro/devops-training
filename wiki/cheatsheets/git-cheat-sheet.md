# Git pour les nuls : Cheatsheet

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
