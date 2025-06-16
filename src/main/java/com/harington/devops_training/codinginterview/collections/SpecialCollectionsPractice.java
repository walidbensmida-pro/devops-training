package com.harington.devops_training.codinginterview.collections;

import java.util.*;

public class SpecialCollectionsPractice {
    public static void main(String[] args) {
        demoLinkedListQueue();
        demoLinkedListStack();
        demoDeque();
        demoPriorityQueue();
        demoPriorityQueueDesc();
        demoTreeSet();
        demoTreeMap();
        demoEnumSet();
        demoStackReverse();
        demoHashMap();
        demoHashSet();
        demoLinkedHashMap();
        demoLinkedHashSet();
        demoArrayList();
        demoArrayDeque();
        demoVector();
        demoHashtable();
        demoNavigableMap();
        demoNavigableSet();
    }

    /**
     * LinkedList : liste chaînée double, permet d'insérer/supprimer rapidement en
     * début ou fin.
     * Peut servir de file (FIFO) ou de pile (LIFO). Accès par index plus lent
     * qu'une ArrayList.
     */
    // File d'attente FIFO avec LinkedList (implémente Queue)
    static void demoLinkedListQueue() {
        LinkedList<String> queue = new LinkedList<>();
        queue.addLast("A");
        queue.addLast("B");
        queue.addLast("C");
        System.out.println("1. File d'attente: " + queue);
        String first = queue.removeFirst();
        System.out.println("1. Retrait en tête: " + first + ", reste: " + queue);
    }

    /**
     * LinkedList utilisée comme pile (LIFO) : push/pop rapides en tête.
     */
    // Pile LIFO avec LinkedList (implémente Stack)
    static void demoLinkedListStack() {
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("2. Pile: " + stack);
        int top = stack.pop();
        System.out.println("2. Pop: " + top + ", reste: " + stack);
    }

    /**
     * Deque (ArrayDeque) : file ou pile très efficace, accès en tête et queue, pas
     * de capacité fixe.
     */
    // Deque (double-ended queue) : accès en tête et queue, très flexible
    static void demoDeque() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("X");
        deque.addLast("Y");
        deque.addFirst("Z");
        System.out.println("3. Deque: " + deque);
        deque.removeLast();
        System.out.println("3. Après retrait en queue: " + deque);
    }

    /**
     * PriorityQueue : file de priorité, tri automatique (min-heap par défaut).
     */
    // File de priorité (min-heap par défaut)
    static void demoPriorityQueue() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        System.out.println("4. PriorityQueue (min): " + pq);
        while (!pq.isEmpty())
            System.out.print(pq.poll() + " ");
        System.out.println();
    }

    /**
     * PriorityQueue (max-heap) : file de priorité avec tri décroissant.
     */
    // File de priorité max (avec Comparator.reverseOrder)
    static void demoPriorityQueueDesc() {
        PriorityQueue<Integer> pqDesc = new PriorityQueue<>(Comparator.reverseOrder());
        pqDesc.addAll(Arrays.asList(5, 1, 3));
        System.out.println("4. PriorityQueue (max): " + pqDesc);
    }

    /**
     * TreeSet : Set trié, sans doublons, accès log(n), ordre naturel ou Comparator.
     */
    // Set trié, sans doublons, accès log(n)
    static void demoTreeSet() {
        TreeSet<String> treeSet = new TreeSet<>(Arrays.asList("banana", "apple", "pear", "apple"));
        System.out.println("5. TreeSet: " + treeSet);
    }

    /**
     * TreeMap : Map triée par clé, accès log(n), ordre naturel ou Comparator.
     */
    // Map triée par clé, accès log(n)
    static void demoTreeMap() {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("b", 2);
        treeMap.put("a", 1);
        treeMap.put("c", 3);
        System.out.println("6. TreeMap: " + treeMap);
        treeMap.forEach((k, v) -> System.out.println("clé: " + k + ", valeur: " + v));
    }

    /**
     * EnumSet : Set optimisé pour enums, très rapide et compact, uniquement pour
     * types enum.
     */
    // EnumSet : set optimisé pour les enums (rapide, compact)
    static void demoEnumSet() {
        enum Day {
            MON, TUE, WED, THU, FRI, SAT, SUN
        }
        EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
        System.out.println("7. EnumSet: " + weekend + ", contient SAT? " + weekend.contains(Day.SAT));
    }

    /**
     * Stack : pile classique (héritée), push/pop en tête, peu utilisée aujourd'hui.
     */
    // Stack (pile) classique, héritée, pour inverser une chaîne
    static void demoStackReverse() {
        String str = "hello";
        Stack<Character> charStack = new Stack<>();
        for (char c : str.toCharArray())
            charStack.push(c);
        StringBuilder reversed = new StringBuilder();
        while (!charStack.isEmpty())
            reversed.append(charStack.pop());
        System.out.println("8. Inversé: " + reversed);
    }

    /**
     * HashMap : association clé/valeur, accès très rapide (hash), pas d'ordre
     * garanti.
     */
    // HashMap : association clé/valeur, accès très rapide (hash)
    static void demoHashMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("a", 1);
        hashMap.put("b", 2);
        hashMap.put("c", 3);
        System.out.println("9. HashMap: " + hashMap);
    }

    /**
     * HashSet : ensemble sans doublons, accès très rapide (hash), pas d'ordre
     * garanti.
     */
    // HashSet : ensemble sans doublons, accès très rapide (hash)
    static void demoHashSet() {
        HashSet<String> hashSet = new HashSet<>(Arrays.asList("apple", "banana", "apple"));
        System.out.println("10. HashSet: " + hashSet);
    }

    /**
     * LinkedHashMap : Map qui garde l'ordre d'insertion, accès rapide, utile pour
     * caches.
     */
    // LinkedHashMap : map qui garde l'ordre d'insertion
    static void demoLinkedHashMap() {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("x", 10);
        linkedHashMap.put("y", 20);
        linkedHashMap.put("z", 30);
        System.out.println("11. LinkedHashMap: " + linkedHashMap);
    }

    /**
     * LinkedHashSet : Set qui garde l'ordre d'insertion, accès rapide.
     */
    // LinkedHashSet : set qui garde l'ordre d'insertion
    static void demoLinkedHashSet() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("one", "two", "one", "three"));
        System.out.println("12. LinkedHashSet: " + linkedHashSet);
    }

    /**
     * ArrayList : liste dynamique, accès rapide par index, bonne pour lecture/ajout
     * en fin.
     */
    // ArrayList : liste dynamique, accès rapide par index
    static void demoArrayList() {
        ArrayList<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3));
        arrayList.add(4);
        System.out.println("13. ArrayList: " + arrayList);
    }

    /**
     * ArrayDeque : pile ou file efficace, pas de capacité fixe, plus rapide que
     * LinkedList pour pile/file.
     */
    // ArrayDeque : pile ou file efficace, sans capacité fixe
    static void demoArrayDeque() {
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.add("first");
        arrayDeque.addLast("last");
        System.out.println("14. ArrayDeque: " + arrayDeque);
    }

    /**
     * Vector : liste synchronisée (thread-safe), héritage historique, peu utilisée
     * aujourd'hui.
     */
    // Vector : liste synchronisée (héritage historique, peu utilisée)
    static void demoVector() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        System.out.println("15. Vector: " + vector);
    }

    /**
     * Hashtable : map synchronisée (thread-safe), héritage historique, peu utilisée
     * aujourd'hui.
     */
    // Hashtable : map synchronisée (héritage historique, peu utilisée)
    static void demoHashtable() {
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("k1", 100);
        hashtable.put("k2", 200);
        System.out.println("16. Hashtable: " + hashtable);
    }

    /**
     * NavigableMap (TreeMap) : accès aux clés voisines (ceiling, floor, etc.),
     * triée.
     */
    // NavigableMap (TreeMap) : accès aux clés voisines (ceiling, floor, etc.)
    static void demoNavigableMap() {
        NavigableMap<Integer, String> navMap = new TreeMap<>();
        navMap.put(10, "dix");
        navMap.put(20, "vingt");
        navMap.put(15, "quinze");
        System.out.println("17. NavigableMap: " + navMap);
        System.out.println("17. Plus petit >= 15: " + navMap.ceilingEntry(15));
    }

    /**
     * NavigableSet (TreeSet) : accès aux éléments voisins (ceiling, floor, etc.),
     * trié.
     */
    // NavigableSet (TreeSet) : accès aux éléments voisins (ceiling, floor, etc.)
    static void demoNavigableSet() {
        NavigableSet<Integer> navSet = new TreeSet<>(Arrays.asList(5, 10, 15, 20));
        System.out.println("18. NavigableSet: " + navSet);
        System.out.println("18. Plus grand <= 12: " + navSet.floor(12));
    }
}
