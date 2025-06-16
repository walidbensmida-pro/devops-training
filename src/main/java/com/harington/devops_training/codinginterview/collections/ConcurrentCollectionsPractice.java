package com.harington.devops_training.codinginterview.collections;

import java.util.*;
import java.util.concurrent.*;

public class ConcurrentCollectionsPractice {
    public static void main(String[] args) throws InterruptedException {
        demoConcurrentHashMap();
        demoCopyOnWriteArrayList();
        demoBlockingQueue();
        demoConcurrentLinkedQueue();
        demoSemaphore();
        demoCountDownLatch();
        demoConcurrentSkipListSet();
    }

    /**
     * ConcurrentHashMap : Map thread-safe, accès et modifications concurrents sans
     * blocage global.
     * Idéal pour compter, agréger ou stocker des données partagées entre threads.
     */
    // 1. ConcurrentHashMap pour compter les accès à des clés depuis plusieurs
    // threads
    static void demoConcurrentHashMap() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        Runnable increment = () -> {
            for (int i = 0; i < 1000; i++) {
                map.merge("key", 1, Integer::sum);
            }
        };
        Thread t1 = new Thread(increment);
        Thread t2 = new Thread(increment);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("1. Compteur ConcurrentHashMap: " + map);
    }

    /**
     * CopyOnWriteArrayList : liste thread-safe, chaque modification crée une
     * nouvelle copie.
     * Très efficace en lecture intensive, peu d'écritures. Utilisée pour listes
     * d'abonnés, listeners, etc.
     */
    // 2. CopyOnWriteArrayList pour stocker des éléments ajoutés par plusieurs
    // threads
    static void demoCopyOnWriteArrayList() throws InterruptedException {
        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        Runnable addTask = () -> {
            for (int i = 0; i < 5; i++)
                cowList.add(i);
        };
        Thread t3 = new Thread(addTask);
        Thread t4 = new Thread(addTask);
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println("2. CopyOnWriteArrayList: " + cowList);
    }

    /**
     * BlockingQueue : file thread-safe, opérations put/take bloquantes.
     * Parfait pour les modèles producteur/consommateur entre threads.
     */
    // 3. BlockingQueue pour producteur/consommateur
    static void demoBlockingQueue() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    queue.put("msg" + i);
                    System.out.println("Produit: msg" + i);
                }
            } catch (InterruptedException ignored) {
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    String msg = queue.take();
                    System.out.println("Consommé: " + msg);
                }
            } catch (InterruptedException ignored) {
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    /**
     * ConcurrentLinkedQueue : file non bloquante, thread-safe, basée sur des listes
     * chaînées.
     * Idéale pour files d'attente partagées à haut débit.
     */
    // 4. ConcurrentLinkedQueue pour file d'attente partagée
    static void demoConcurrentLinkedQueue() {
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        clq.add("A");
        clq.add("B");
        System.out.println("4. ConcurrentLinkedQueue: " + clq);
        clq.poll();
        System.out.println("4. Après poll: " + clq);
    }

    /**
     * Semaphore : compteur pour limiter le nombre de threads accédant à une
     * ressource.
     * Utilisé pour contrôler l'accès concurrent (ex : pool de connexions).
     */
    // 5. Semaphore pour limiter l'accès concurrent
    static void demoSemaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        Runnable limitedTask = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " a le droit d'entrer");
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            } finally {
                semaphore.release();
            }
        };
        Thread s1 = new Thread(limitedTask);
        Thread s2 = new Thread(limitedTask);
        Thread s3 = new Thread(limitedTask);
        s1.start();
        s2.start();
        s3.start();
        s1.join();
        s2.join();
        s3.join();
    }

    /**
     * CountDownLatch : synchronisation, attend que N threads aient terminé avant de
     * continuer.
     * Pratique pour attendre la fin d'initialisations ou de tâches parallèles.
     */
    // 6. CountDownLatch pour synchroniser le démarrage de threads
    static void demoCountDownLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Runnable worker = () -> {
            System.out.println(Thread.currentThread().getName() + " prêt");
            latch.countDown();
        };
        new Thread(worker).start();
        new Thread(worker).start();
        new Thread(worker).start();
        latch.await();
        System.out.println("6. Tous les threads sont prêts !");
    }

    /**
     * ConcurrentSkipListSet : Set trié, thread-safe, basé sur skip list.
     * Permet des ajouts, suppressions et recherches concurrentes avec ordre
     * naturel.
     */
    // 7. ConcurrentSkipListSet pour éléments triés en concurrence
    static void demoConcurrentSkipListSet() {
        ConcurrentSkipListSet<Integer> skipSet = new ConcurrentSkipListSet<>();
        skipSet.addAll(Arrays.asList(5, 2, 8, 1));
        System.out.println("7. ConcurrentSkipListSet: " + skipSet);
    }
}
