<!-- filepath: security/multi-account-github-setup.md -->
# Gérer plusieurs comptes GitHub sur un même PC : Guide pour Débutant

Ce guide explique, étape par étape et sans prérequis technique, comment utiliser plusieurs comptes GitHub (perso, pro, etc.) sur un même ordinateur, en toute sécurité.

---

## 1. Pourquoi avoir plusieurs comptes GitHub ?
- Un compte pour le travail, un pour les projets perso
- Séparer les accès, les droits, les emails
- Éviter de mélanger les identités (et les erreurs de push !)

---

## 2. Principe général
Chaque compte GitHub doit avoir sa propre clé SSH (comme une clé différente pour chaque porte) et une configuration Git adaptée.

**Schéma** :
```
[PC] --(clé SSH 1)--> [GitHub Perso]
[PC] --(clé SSH 2)--> [GitHub Pro]
```

---

## 3. Générer des clés SSH pour chaque compte

### a) Vérifier les clés existantes
Ouvre un terminal et tape :
```bash
ls -al ~/.ssh
```

### b) Générer une nouvelle clé pour chaque compte
```bash
ssh-keygen -t ed25519 -C "votre_email_perso@example.com"
# Donnez-lui un nom unique, ex : id_ed25519_github_perso
ssh-keygen -t ed25519 -C "votre_email_pro@example.com"
# Donnez-lui un nom unique, ex : id_ed25519_github_pro
```

---

## 4. Ajouter les clés SSH à l’agent
```bash
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519_github_perso
ssh-add ~/.ssh/id_ed25519_github_pro
```

---

## 5. Ajouter les clés à GitHub
- Va sur [github.com](https://github.com) > Settings > SSH and GPG keys > New SSH key
- Copie le contenu de chaque fichier `.pub` (clé publique) et colle-le dans GitHub

---

## 6. Configurer le fichier SSH pour différencier les comptes
Ouvre ou crée le fichier `~/.ssh/config` et ajoute :
```bash
# Compte perso
Host github.com-perso
    HostName github.com
    User git
    IdentityFile ~/.ssh/id_ed25519_github_perso

# Compte pro
Host github.com-pro
    HostName github.com
    User git
    IdentityFile ~/.ssh/id_ed25519_github_pro
```

---

## 7. Configurer Git pour chaque projet
Dans chaque dossier de projet, indique quel compte utiliser :
```bash
git config user.name "Votre Nom"
git config user.email "votre_email_perso@example.com"
```

Pour utiliser le bon host SSH, modifie l’URL du remote :
```bash
git remote set-url origin git@github.com-perso:VotrePseudo/nom-du-repo.git
```

---

## 8. Astuces et bonnes pratiques
- Toujours vérifier l’URL du remote avant de pousser
- Utiliser des noms explicites pour les clés SSH
- Ne jamais partager vos clés privées
- Documenter la config dans chaque projet (README)

---

## 9. Pour aller plus loin
- [Doc officielle GitHub SSH](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)
- [Guide sécurité Spring Boot](securite-springboot.md)

---

> Ce guide est volontairement vulgarisé pour permettre à toute personne, même sans expérience Git, de gérer plusieurs comptes GitHub sur un même PC en toute sécurité.