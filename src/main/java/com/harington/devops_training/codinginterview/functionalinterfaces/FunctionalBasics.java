package com.harington.devops_training.codinginterview.functionalinterfaces;

import java.time.LocalDate;
import java.util.function.*;
import java.util.stream.Stream;

public class FunctionalBasics {
    public static void main(String[] args) {
        // 1. Qu'est-ce qu'une interface fonctionnelle en Java ?
        // Réponse : C'est une interface qui ne définit qu'une seule méthode abstraite
        // (et éventuellement des méthodes par défaut ou statiques).

        // 2. Citez 4 interfaces fonctionnelles de base du JDK.
        // Réponse : Predicate, Supplier, Function, Consumer

        // 3. Écrivez une lambda qui teste si un entier est pair.
        Predicate<Integer> isEven = x -> x % 2 == 0;
        System.out.println("3. 4 est pair ? " + isEven.test(4));

        // 4. Quelle est la différence entre Predicate et Function ?
        // Réponse : Predicate prend un argument et retourne un boolean. Function prend
        // un argument et retourne un résultat (de type potentiellement différent).

        // 5. Donnez un exemple d'utilisation de Consumer.
        Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
        printUpper.accept("hello");

        // 6. Comment composer deux Function en Java ?
        Function<Integer, Integer> f1 = x -> x + 1;
        Function<Integer, Integer> f2 = x -> x * 2;
        Function<Integer, Integer> composed = f1.andThen(f2); // (x+1)*2
        System.out.println("6. Composé (3+1)*2 = " + composed.apply(3));

        // 7. À quoi sert la méthode default andThen() sur Consumer ?
        // Réponse : Elle permet d'enchaîner deux Consumer : le premier s'exécute, puis
        // le second sur la même valeur.
        Consumer<String> printLower = s -> System.out.println(s.toLowerCase());
        Consumer<String> chain = printUpper.andThen(printLower);
        chain.accept("Java");

        // 8. Écrivez un Supplier qui retourne la date du jour.
        Supplier<LocalDate> todaySupplier = LocalDate::now;
        System.out.println("8. Date du jour : " + todaySupplier.get());

        // 9. Comment utiliser une référence de méthode avec un Predicate ?
        Predicate<String> isEmpty = String::isEmpty;
        System.out.println("9. \"\" est vide ? " + isEmpty.test(""));

        // 10. Expliquez la différence entre Function<T,R> et UnaryOperator<T>.
        // Réponse : Function<T, R> prend un T en entrée et retourne un R.
        // UnaryOperator<T> est un Function où l'entrée et la sortie sont du même type
        // (T -> T).

        // 11. Créez votre propre interface fonctionnelle et utilisez-la avec une
        // lambda.
        @FunctionalInterface
        interface StringTransformer {
            String transform(String s);
        }
        StringTransformer upper = s -> s.toUpperCase();
        System.out.println("11. upper.transform('java') = " + upper.transform("java"));

        // 12. Comment utiliser les interfaces fonctionnelles dans un stream pour
        // filtrer, transformer et collecter ?
        var result = Stream.of("a", "bb", "ccc")
                .filter(s -> s.length() > 1)
                .map(String::toUpperCase)
                .toList();
        System.out.println("12. Résultat stream : " + result);

        // 13. Expliquez comment gérer les exceptions dans une lambda passée à une
        // interface fonctionnelle.
        Consumer<String> safePrint = s -> {
            try {
                System.out.println(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                System.out.println("Not a number");
            }
        };
        safePrint.accept("42");
        safePrint.accept("abc");

        // 14. Donnez un exemple d'utilisation de BiFunction et de composition avec
        // Function.
        BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function<Integer, String> toString = Object::toString;
        Function<Integer, String> sumToString = toString.compose(x -> sum.apply(x, 10));
        System.out.println("14. sumToString.apply(5) = " + sumToString.apply(5)); // "15"

        // 15. Comment utiliser les interfaces fonctionnelles pour rendre un code plus
        // testable et modulaire ?
        // Réponse : On peut injecter des comportements (lambdas, méthodes) en
        // paramètre, ce qui permet de tester chaque partie indépendamment et de
        // remplacer facilement une logique par une autre (ex: pour les tests unitaires
        // ou la configuration).
    }
}
