package com.zentagroup.datastructures;

import com.zentagroup.datastructures.avltree.AVLTree;
import com.zentagroup.datastructures.avltree.AVLTreeSet;
import com.zentagroup.datastructures.binarytree.BinaryTree;
import com.zentagroup.datastructures.redblacktree.RedBlackTree;

import java.util.Arrays;
import java.util.Iterator;

import com.zentagroup.datastructures.graphs.Graph;
import com.zentagroup.datastructures.binarytree.BinaryTreeSet;
import com.zentagroup.datastructures.graphs.StructuredGraph;
import com.zentagroup.datastructures.graphs.StructuredGraph.*;


import java.util.*;

public class Main {

    public static final String BINARY_TREE = "bt";
    public static final String RB_BINARY_TREE_SET = "RBbts";
    public static final String AVL_TREE = "avlt";
    public static final String GRAPH = "graph";
    public static final String BINARY_TREE_SET = "btS";
    public static final String AVL_TREE_SET = "avlts";
    public static final String STRUCTURED_GRAPH = "sgraph";
    public static final String STRUCTURED_GRAPH_WEIGHT = "sgraphw";

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("This program needs an argument to execute corresponding code");
            return;
        }
        switch (args[0]) {
            case BINARY_TREE:
                executeBinaryTree();
                break;
            case BINARY_TREE_SET:
                executeBinaryTreeSet();
                break;
            case AVL_TREE:
                executeAVLTree();
                break;
            case GRAPH:
                executeGraph();
                break;
            case AVL_TREE_SET:
                executeAVLTreeSet();
                break;
            case RB_BINARY_TREE_SET:
                executeRedBlackBTS();
                break;
            case STRUCTURED_GRAPH:
                executeStructuredGraph();
                break;
            case STRUCTURED_GRAPH_WEIGHT:
                executeStructuredGraphWithWeight();
                break;
        }
    }

    public static void executeAVLTree() {
        AVLTree<Integer> avl = new AVLTree(1);
        avl.add(2);
        avl.add(3);
        avl.add(4);
        avl.add(5);
        avl.add(6);
        avl.add(7);
        avl.add(8);
        avl.add(9);
        System.out.println("Traversing tree Pre-Order");
        avl.preOrder();
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree In-Order");
        avl.inOrder();
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree Post-Order");
        avl.postOrder();
        System.out.println("------------------------------------------");
    }

    public static void executeBinaryTree() {
        BinaryTree<Integer> bt = new BinaryTree();
        bt.add(4);
        bt.add(2);
        bt.add(6);
        bt.add(1);
        bt.add(3);
        bt.add(5);
        bt.add(8);
        bt.add(7);
        bt.add(9);
        System.out.println("Traversing tree Pre-Order");
        bt.preOrder();
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree In-Order");
        bt.inOrder();
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree Post-Order");
        bt.postOrder();
        System.out.println("------------------------------------------");

    }
    public static void executeBinaryTreeSet() {
        BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
        tree.add(4);
        tree.add(2);
        tree.add(6);
        tree.add(1);
        tree.add(3);
        tree.add(5);
        tree.add(8);
        tree.add(7);
        tree.add(9);

        BinaryTreeSet<Integer> btS = new BinaryTreeSet<>();
        btS.add(14);
        btS.add(17);
        btS.add(19);

        System.out.println("cantidad de nodos: " + tree.size());
        System.out.println("contiene el 7: " + tree.contains(7));
        System.out.println("esta vacio " + tree.isEmpty());
        System.out.println("el arbol tiene " + tree.height() + " niveles");
        System.out.println("------------------------------------------");
        tree.inOrder();
        System.out.println("------------------------------------------");
        System.out.println("eliminamos 2, 9 y 8");
        tree.remove(9);
        tree.remove(2);
        tree.remove(8);
        System.out.println("------------------------------------------");
        tree.inOrder();
        tree.add(2);
        tree.add(8);
        tree.add(9);


        System.out.println("Agregamos un grupo de elementos");
        tree.addAll(btS);
        System.out.println("------------------------------------------");
        System.out.println("El arbol contiene los nuevos elementos: " + tree.containsAll(btS));
        System.out.println("------------------------------------------");
        System.out.println("Imprimimos el arbol con los nuevos elementos");
        tree.inOrder();
        System.out.println("------------------------------------------");
        System.out.println("eliminamos los elementos creados");
        tree.removeAll(btS);
        System.out.println("------------------------------------------");
        System.out.println("El arbol incluye los nuevos elemetos: " + tree.containsAll(btS));
        System.out.println("------------------------------------------");
        System.out.println("Imprimimos el arbol con sin los elementos");
        tree.inOrder();
        System.out.println("------------------------------------------");
        System.out.println("Imprimimos el arbol pero agregamos el array de numeros 10,11,12,13");
        Integer[] binaryTreeSetArray = {10, 11, 12, 13};
        tree.toArray(binaryTreeSetArray);
    }

    public static void executeAVLTreeSet() {
        AVLTreeSet<Integer> avlts = new AVLTreeSet();
        avlts.add(1);
        avlts.add(3);
        avlts.add(2);
        avlts.add(4);
        avlts.add(8);
        avlts.add(5);
        avlts.add(7);
        avlts.add(6);

        Collection<Integer> c = new ArrayList<Integer>() {
            {
                add(10);
                add(22);
                add(13);
                add(30);
            }
        };

        avlts.addAll(c);

        System.out.println(avlts.containsAll(c));
        c.add(56);
        System.out.println(avlts.containsAll(c));

        System.out.println("Traversing tree Pre-Order");
        avlts.forEachPreorder(System.out::println);
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree In-Order");
        avlts.forEachInorder(System.out::println);
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree Post-Order");
        avlts.forEachPostorder(System.out::println);
        System.out.println("------------------------------------------");
        System.out.println("Traversing tree Level-Order");
        avlts.forEachLevelOrder(System.out::println);
        System.out.println("------------------------------------------");
        System.out.println("Printing as array");
        System.out.println(Arrays.toString(avlts.toArray()));
        System.out.println("------------------------------------------");
        Integer[] array = {65, 60, 98, 58};
        System.out.println("Adding array [65, 60, 98, 58] and printing as array");
        System.out.println(Arrays.toString(avlts.toArray(array)));
        System.out.println("------------------------------------------");
        System.out.println(avlts.remove(98));
        System.out.println("Traversing after deleting 98");
        avlts.forEachInorder(System.out::println);
        System.out.println("------------------------------------------");
        System.out.println(avlts.removeAll(Arrays.asList(array)));
        System.out.println("Traversing after deleting [65,60,98,58]");
        avlts.forEachInorder(System.out::println);
        System.out.println("------------------------------------------");

        Iterator it = avlts.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public static void executeRedBlackBTS() {
        try {

            /**
             * the inOrder method print node and boolean
             * true -> red, false -> black
             */

            RedBlackTree<Integer> num = new RedBlackTree<>();
            num.add(1);
            num.add(2);
            num.add(3);
            num.add(4);
            num.add(5);
            num.add(6);
            num.add(7);
            num.add(8);
            num.add(9);
            num.add(10);
            num.add(11);
            num.add(12);

            num.forEachInOrder(n -> System.out.println(n + 9));
            System.out.println("---------------------------");
            Iterator<Integer> it = num.iterator();
            while (it.hasNext()) {
                System.out.println(it.next() + 30 + " it");
            }
            System.out.println("---------------------------");

            RedBlackTree<String> str = new RedBlackTree<>();
            str.add("C");
            str.add("B");
            str.add("E");
            str.add("A");
            str.add("F");
            str.add("D");
            str.inOrder();
            System.out.println("---------------------------");
            System.out.println("contains Z: " + str.contains("Z"));
            System.out.println("---------------------------");

            removeTestRedBlackBTS();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeTestRedBlackBTS() {
        try {
            /**
             --- REMOVE CASES ---
             num.remove(6);      -
             num.remove(1);      -
             num.remove(17);     -
             num.remove(25);     -
             num.remove(13);     -
             num.remove(8);      -
             num.remove(11);     -
             num1.remove(18);    -
             num1.remove(3);     -
             num2.remove(2);     -
             */
            System.out.println("--------- REMOVE ---------");
            RedBlackTree<Integer> num = new RedBlackTree<>();
            System.out.println("------------- First Tree --------------");
            num.add(13);
            num.add(8);
            num.add(17);
            num.add(1);
            num.add(11);
            num.add(6);
            num.add(15);
            num.add(25);
            num.add(22);
            num.add(27);
            num.inOrder();

            System.out.println("---------------------------");
            num.remove(11);
            num.inOrder();

            System.out.println();
            System.out.println("------------- Second Tree --------------");
            RedBlackTree<Integer> num1 = new RedBlackTree<>();
            num1.add(7);
            num1.add(3);
            num1.add(18);
            num1.add(10);
            num1.add(22);
            num1.add(8);
            num1.add(11);
            num1.add(26);
            num1.inOrder();

            System.out.println("---------------------------");
            num1.remove(3);
            num1.inOrder();

            System.out.println();
            System.out.println("------------- Third Tree --------------");
            RedBlackTree<Integer> num2 = new RedBlackTree<>();
            num2.add(5);
            num2.add(2);
            num2.add(8);
            num2.add(1);
            num2.add(4);
            num2.add(7);
            num2.add(9);
            num2.inOrder();

            System.out.println("---------------------------");
            num2.remove(2);
            num2.inOrder();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeGraph() {
        Graph g = new Graph(4);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);
        //g.addEdge(4, 2);
        int node = 1;
        System.out.println("\nFollowing is Breadth First Traversal " +
                "(starting from vertex " + node + ")");
        g.BFS(node);
        System.out.println("\nFollowing is Depth First Traversal " +
                "(starting from vertex " + node + ")");
        g.DFS(node);
        System.out.println("");
    }

    public static void executeStructuredGraph() {

        StructuredGraph g = new StructuredGraph();

        HashMap<String, Object> donkey = new HashMap();
        donkey.put("name", "Donkey");
        donkey.put("age", "30");

        HashMap shrek = new HashMap();
        shrek.put("name", "Shrek");
        shrek.put("age", "27");

        HashMap sushi = new HashMap();
        sushi.put("name", "Sushi");

        HashMap rice = new HashMap();
        rice.put("name", "Rice");

        HashMap burger = new HashMap();
        burger.put("name", "Burger");

        HashMap burgerGourmet = new HashMap();
        burgerGourmet.put("name", "burgerGourmet");

        HashMap soySauce = new HashMap();
        soySauce.put("name", "Soy Sauce");

        HashMap tomato = new HashMap();
        tomato.put("name", "Tomato");

        HashMap fiona = new HashMap();
        fiona.put("name", "Fiona");

        g.addVertex(donkey, "Person");
        g.addVertex(sushi, "Food");

        g.addEdge(donkey, "Person", shrek, "Person", "lives with", true);
        g.addEdge(shrek, "Person", sushi, "Food", "loves", false);
        g.addEdge(sushi, "Food", burgerGourmet, "Food", "loves", false);
        g.addEdge(sushi, "Food", tomato, "Food", "loves", false);
        g.addEdge(fiona, "Person", soySauce, "Food", "loves", false);
        g.addEdge(shrek, "Person", burger, "Food", "likes", false);
        g.addEdge(tomato, "Food", soySauce, "Food", "contains", false);
        g.addEdge(soySauce, "Food", rice, "Food", "contains", false);
        g.addEdge(burger, "Food", soySauce, "Food", "has", false);

        System.out.println();
        System.out.println("-------- Print BFS ---------------------------------------------------------------------");
        System.out.println();

        System.out.println("BFS Donkey:");
        List<Vertex> personTest = g.BFS(donkey, "Person");
        System.out.println(personTest);

        System.out.println();
        System.out.println("-------- Print DFS ---------------------------------------------------------------------");
        System.out.println();

        System.out.println("DFS Donkey:");
        List<Vertex> dfsp1 = g.DFS(donkey, "Person");
        System.out.println(dfsp1);

        System.out.println();
        System.out.println("------------- Find shortest path (shrek to rice) ----------------------------------------");
        System.out.println();
        System.out.println(g.findShortestPath(shrek, "Person", rice, "Food"));

        System.out.println();
        System.out.println("------------- Find shortest path weight (shrek to rice) --------------------------------");
        System.out.println();
        System.out.println(g.calculateWeight(g.findShortestPath(shrek, "Person", rice, "Food")));

        System.out.println();
        System.out.println("------------- Find type of relationships (shrek) ---------------------------------------");
        System.out.println();
        System.out.println(g.findTypeOfRelationships(shrek, "Person"));

        System.out.println();
        System.out.println("---------- FindEntrant (soySauce) ------------------------------------------------------");
        System.out.println();
        System.out.println(g.findEntrantVertices(soySauce, "Food"));

        System.out.println();
        System.out.println("--------- FindEntrant by type (soySauce all Foods) -------------------------------------");
        System.out.println();
        System.out.println(g.findEntrantVertices(soySauce, "Food", "Food", null));

        System.out.println();
        System.out.println("--------- FindEntrant by type and relationship (soySauce 'Food' and 'contains') --------");
        System.out.println();
        System.out.println(g.findEntrantVertices(soySauce, "Food", "Food", "contains"));

        System.out.println();
        System.out.println("---------- FindNeighbors (shrek) -------------------------------------------------------");
        System.out.println(g.findNeighbors(shrek, "Person"));

        System.out.println();
        System.out.println("--------- FindNeighbors by type (shrek, 'Food') ----------------------------------------");
        System.out.println();
        System.out.println(g.findNeighbors(shrek, "Person", "Food", null));

        System.out.println();
        System.out.println("--------- FindNeighbors by type and relationship (shrek 'Food' and 'likes') ------------");
        System.out.println();
        System.out.println(g.findNeighbors(shrek, "Person", "Food", "likes"));
        System.out.println();

    }

    public static void executeStructuredGraphWithWeight() {

        StructuredGraph g = new StructuredGraph();
        HashMap santiago = new HashMap();
        santiago.put("name", "Santiago");
        santiago.put("movie", "de chile");

        HashMap tokio = new HashMap();
        tokio.put("name", "Tokio");
        tokio.put("movie", "Casa de papel");

        HashMap narnia = new HashMap();
        narnia.put("name", "Narnia");
        narnia.put("movie", "Narnia");

        HashMap hogwarts = new HashMap();
        hogwarts.put("name", "Hogwarts");
        hogwarts.put("movie", "Harry Potter");

        HashMap zentown = new HashMap();
        zentown.put("name", "Zentown");
        zentown.put("movie", "Zentimientos");

        HashMap wonderland = new HashMap();
        wonderland.put("name", "Wonderland");
        wonderland.put("movie", "Wonderland");

        HashMap muymuylejano = new HashMap();
        muymuylejano.put("name", "Muy muy lejano");
        muymuylejano.put("movie", "Sherk");

        g.addEdge(santiago, "City", tokio, "City", "km", false, 90.00);
        g.addEdge(santiago, "City", muymuylejano, "City", "km", false, 300.00);
        g.addEdge(tokio, "City", narnia, "City", "km", false, 120.00);
        g.addEdge(tokio, "City", wonderland, "City", "km", false, 60.00);
        g.addEdge(wonderland, "City", hogwarts, "City", "km", false, 30.00);
        g.addEdge(muymuylejano, "City", hogwarts, "City", "km", false, 600.00);
        g.addEdge(hogwarts, "City", zentown, "City", "km", false, 900.00);
        g.addEdge(santiago, "City", tokio, "City", "km", false, 180.00);

        System.out.println();
        System.out.println("------------- Find shortest path (santiago - zentown) -----------------------------------");
        System.out.println();
        System.out.println(g.findShortestPath(santiago, "City", zentown, "City"));

        System.out.println();
        System.out.println("------------- Find shortest path weight (santiago - zentown) ----------------------------");
        System.out.println();
        System.out.println(g.calculateWeight(g.findShortestPath(santiago, "City", zentown, "City")));

        System.out.println();
        System.out.println("------------- Find all Edges (Santiago - tokio) -----------------------------------------");
        System.out.println();
        System.out.println(g.findEdgesBetweenTwoObjects(santiago, "City", tokio, "City"));

        System.out.println();
        System.out.println("------------- Find Edge with relationship (Santiago - tokio) ----------------------------");
        System.out.println();
        System.out.println(g.findEdge(santiago, "City", tokio, "City", "km"));

        System.out.println();
        System.out.println("------------- Find Vertex by attribute (Muy muy lejano) ---------------------------------");
        System.out.println();
        System.out.println(g.findVertex(muymuylejano, "City"));

        System.out.println();
        System.out.println("------------- Find Edge (Santiago - Muy muy lejano) -------------------------------------");
        System.out.println();
        System.out.println(g.findEdge(santiago, "City", muymuylejano, "City", "km"));

        System.out.println();
        System.out.println("------------- Find Edge by attribute (Santiago - Muy muy lejano) ------------------------");
        System.out.println();
        HashMap santiagoAtt = new HashMap();
        santiagoAtt.put("movie", "de chile");
        System.out.println(g.findEdge(santiagoAtt, "City", muymuylejano, "City", "km"));

        System.out.println();
        System.out.println("------------- Delete Vertex (Tokio) -----------------------------------------------------");
        System.out.println();
        System.out.println(g.deleteVertex(tokio, "City"));

        System.out.println();
        System.out.println("------------- Find Ingress and Egress (Santiago) ---------------------------------------");
        System.out.println();
        System.out.println(g.findEgressAndIngress(santiago, "City"));

        System.out.println();
        System.out.println("------------- Counting Nodes and Edges -------------------------------------------------");
        System.out.println();
        System.out.println("Edges: " + g.getNumEdges());
        System.out.println("Vertex: " + g.getNumVertices());
        System.out.println();

    }

}
