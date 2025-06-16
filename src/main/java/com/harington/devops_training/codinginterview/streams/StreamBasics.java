package com.harington.devops_training.codinginterview.streams;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.*;

public class StreamBasics {
    static class Employe {
        String nom;
        int age;

        Employe(String nom, int age) {
            this.nom = nom;
            this.age = age;
        }

        public String toString() {
            return nom + " (" + age + ")";
        }
    }

    static class Produit {
        String nom;
        String categorie;
        int prix;
        int ventes;

        Produit(String nom, String categorie, int prix, int ventes) {
            this.nom = nom;
            this.categorie = categorie;
            this.prix = prix;
            this.ventes = ventes;
        }

        public String toString() {
            return nom + " (" + categorie + ", " + prix + ", " + ventes + ")";
        }
    }

    static class Commande {
        String client;
        int montant;

        Commande(String client, int montant) {
            this.client = client;
            this.montant = montant;
        }
    }

    static class ObjetDate {
        String nom;
        LocalDate date;

        ObjetDate(String nom, LocalDate date) {
            this.nom = nom;
            this.date = date;
        }
    }

    public static void main(String[] args) {
        // 1. Liste d'employés
        List<Employe> employes = List.of(
                new Employe("Alice", 30), new Employe("Bob", 45), new Employe("Charlie", 28), new Employe("Diana", 52));
        // 2. Liste de produits
        List<Produit> produits = List.of(
                new Produit("Stylo", "Fourniture", 2, 100),
                new Produit("Cahier", "Fourniture", 5, 80),
                new Produit("Souris", "Informatique", 20, 30),
                new Produit("Clavier", "Informatique", 30, 20));
        // 3. Liste de chaînes
        List<String> chaines = List.of("abc", "de", "fghij", "klm", "nop", "de");
        // 4. Liste de commandes
        List<Commande> commandes = List.of(
                new Commande("Alice", 120), new Commande("Bob", 200), new Commande("Alice", 80),
                new Commande("Diana", 50));
        // 5. Liste d'entiers
        List<Integer> entiers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // 6. Liste de personnes (nom, ville)
        List<String[]> personnes = List.of(
                new String[]{"Alice", "Paris"}, new String[]{"Bob", "Lyon"}, new String[]{"Charlie", "Paris"},
                new String[]{"Diana", "Marseille"});
        // 7. Liste de phrases
        List<String> phrases = List.of("hello world", "java streams", "hello java");
        // 8. Liste de notes
        List<Integer> notes = List.of(8, 12, 15, 9, 10, 17, 6);
        // 9. Liste de produits (déjà créée)
        // 10. Liste d'objets avec date
        List<ObjetDate> objets = List.of(
                new ObjetDate("Obj1", LocalDate.of(2020, 1, 1)),
                new ObjetDate("Obj2", LocalDate.of(2019, 5, 10)),
                new ObjetDate("Obj3", LocalDate.of(2021, 3, 15)));

        // Les questions restent à la suite pour que tu puisses répondre !
        // 1. Tu as une liste d'employés (nom, âge). Comment trouver les 2 plus âgés ?
        var topTwo = employes.stream()
                .sorted((o1, o2) -> o2.age - o1.age)
                .limit(2)
                .toList();
        System.out.println("1. " + topTwo);

        // 2. Tu as une liste de produits (nom, prix, ventes). Comment générer une
        // Map<String, Integer> avec pour chaque produit la somme totale des ventes ?
        Map<String, Integer> rep2 = produits.stream()
                .collect(Collectors.groupingBy(produit -> produit.nom, Collectors.summingInt(value -> value.ventes)));
        System.out.println("1. " + rep2);

        // 3. Tu as une liste de chaînes. Comment obtenir la liste des longueurs
        // distinctes, triées décroissantes ?
        List<Integer> reverseDistinctLenghts = chaines.stream()
                .map(String::length)
                .distinct()
                .sorted(Comparator.reverseOrder()).toList();

        System.out.println("3. " + reverseDistinctLenghts);

        // 4. Tu as une liste de commandes (client, montant). Comment calculer le
        // montant total par client ?
        Map<String, Integer> result = commandes.stream()
                .collect(Collectors.groupingBy(commande -> commande.client, Collectors.summingInt(value -> value.montant)));
        System.out.println("4. " + result);
        // 5. Tu as une liste d'entiers. Comment obtenir la concaténation des chiffres
        // pairs, séparés par un tiret ?
        String pairsConcat = entiers.stream()
                .filter(i -> i % 2 == 0)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
        System.out.println("5. " + pairsConcat);

        // 6. Tu as une liste de personnes (nom, ville). Comment obtenir la liste des
        // villes sans doublons, triées ?

        List<String> exo6 = personnes.stream()
                .map(strings -> strings[1])
                .distinct()
                .sorted()
                .toList();
        System.out.println("6. " + exo6);

        // 7. Tu as une liste de phrases. Comment obtenir la liste de tous les mots
        // distincts, triés alphabétiquement ?
        var count = phrases.stream()
                .flatMap(phrase -> Arrays.stream(phrase.split(" ")))
                .distinct()
                .sorted()
                .toList();
        System.out.println("7. " + count);

        // 8. Tu as une liste de notes (0-20). Comment compter combien ont la moyenne
        // (>=10) ?
        long exo8 = notes.stream()
                .filter(integer -> integer >= 10).count();

        System.out.println("8. " + exo8);

        // 9. Tu as une liste de produits (nom, catégorie, prix). Comment obtenir le
        // prix moyen par catégorie ?
        Map<String, Double> exo9 = produits.stream()
                .collect(Collectors.groupingBy(produit -> produit.categorie, Collectors.averagingInt(value -> value.prix)));

        System.out.println("9. " + exo9);

        // 10. Tu as une liste d'objets avec une date. Comment trouver le plus ancien ?
        List<ObjetDate> exo10 = objets.stream()
                .sorted((o1, o2) -> o1.date.compareTo(o2.date))
                .limit(1)
                .toList();
        System.out.println("10. " + exo10);
    }
}
