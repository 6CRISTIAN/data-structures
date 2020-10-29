package com.zentagroup.datastructures.avltree;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AVLTreeSet<E extends Comparable> implements Set<E> {

    private Node<E> root;

    /**
     * Constructor that instantiates an empty Tree.
     */
    public AVLTreeSet() {}

    /**
     * Constructor that inserts a new node on the tree as a root.
     * @param data E
     */
    public AVLTreeSet(E data) {
        this.root = new Node(data);
    }

    /**
     * Calculates the number of nodes in the tree set using the sizeRec method.
     * If the root is null it returns 0.
     * @return int n of nodes
     */
    @Override
    public int size() {
        if (this.root == null){
            return 0;
        }
        return sizeRec(this.root);
    }

    /**
     * Calculates the size (number of nodes) of the tree set based on a root node
     * using recursion.
     * @param root base node for the calculation
     * @return int n of nodes
     */
    public int sizeRec(Node<E> root) {
        int count = 1;
        if (root.left != null){
            count += sizeRec(root.left);
        }
        if (root.right != null){
            count += sizeRec(root.right);
        }
        return count;
    }

    /**
     * Calculates the height of a node. If the node is null it returns -1
     * @param node node to be checked
     * @return int height
     */
    public int height(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Updates the height of a node, taking the max height between its left and right node
     * and adding one.
     * @param node
     */
    public void updateHeight(Node<E> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Checks whether the set is empty
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Finds and returns a node based on an object of the type of the
     * nodes data. If it isn't found it returns null.
     * @param data
     * @return found node
     */
    public Node<E> findAndRetrieve(E data) {
        Node<E> current = root;
        while (current != null) {
            if (current.data.equals(data)) {
                break;
            }
            current = current.data.compareTo(data) < 0 ? current.right : current.left;
        }
        return current;
    }

    /**
     * Finds a node based on an object of the type of the
     * nodes data. If it isn't found it returns false.
     * @param data
     * @return found node
     */
    public boolean find(E data) {
        Node<E> current = root;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.data.compareTo(data) < 0 ? current.right : current.left;
        }
        return false;
    }

    /**
     * Checks if the tree contains certain node. If the object to be looked for isn't a node
     * it returns false or isn't found it returns false.
     * @param o object
     * @return
     */
    @Override
    public boolean contains(Object o) {
        if(!(o instanceof Node)){
            return false;
        }
        Node<E> node = (Node) o;
        return find(node.data);
    }

    /**
     * Checks if a node of the tree contains certain data.
     * @param data
     * @return boolean
     */
    public boolean containsData(E data) {
        return find(data);
    }

    /**
     * Clears the tree set
     */
    @Override
    public void clear() {
        this.root = null;
    }

    /**
     * Returns a new custom AVLTreeSetIterator
     * @return new Iterator
     */
    @Override
    public Iterator iterator() {
        return new AVLTreeSetIterator();
    }

    /**
     * Inserts each element of the tree set into an Object array and returns it.
     * @return object array with elements of the tree set.
     */
    @Override
    public Object[] toArray() {
        List<Object> list = new ArrayList<>();
        forEachInorder(list::add);
        return list.toArray();
    }

    /**
     *  Adds a data object of the type contained by the nodes, creates a node and
     *  adds it to the tree set in the corresponding position. Uses the method addRec
     *  to do so.
     * @param data
     * @return
     */
    @Override
    public boolean add(E data) {
        if (this.root == null) {
            this.root = new Node<E>(data);
            return true;
        }
        Node<E> node = new Node<E>(data);
        this.root = addRec(this.root, node);
        return root != null;
    }

    /**
     * Recursively looks for the right position to add the node and balances the
     * tree after the addition. The node will be added to the left of the greater
     * nodes and to the right of the minor ones.
     * Returns true if it was added or false if it wasn't.
     * @param current root node
     * @param newNode new node to be added
     * @return boolean
     */
    Node<E> addRec(Node<E> current, Node <E> newNode) {
        if (current == null) {
            return newNode;
        } else if (newNode.data.compareTo(current.data) < 0) {
            current.left = addRec(current.left, newNode);
        } else if (newNode.data.compareTo(current.data) > 0) {
            current.right = addRec(current.right, newNode);
        } else {
            System.out.println("Repeated node: Node wasn't added.");
        }
        return rebalance(current);
    }

    /**
     * Returns the balance of a node, which is the result of the height of the
     * right node minus the height of the left node. If the node us null it returns 0.
     * @param n node
     * @return int balance
     */
    public int getBalance(Node<E> n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    /**
     * Rebalances the tree set. If the balance factor is greater than one its unbalanced to the right
     * so it checks whether the it's right node is also unbalanced. If its
     * whether the
     * @param node
     * @return
     */
    private Node<E> rebalance(Node<E> node) {
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (height(node.right.right) < height(node.right.left)) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }
        if (balance < -1) {
            if (height(node.left.left) < height(node.left.right)) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }


    /***
     * Performs a right rotation over a given node
     * @param node base node for the rotation
     * @return Node
     */
    private Node<E> rotateRight(Node<E> node) {
        Node<E> x = node.left;
        Node<E> z = x.right;
        x.right = node;
        node.left = z;
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    /***
     * Performs a left rotation over a given node
     * @param node base node for the rotation
     * @return Node
     */
    private Node<E> rotateLeft(Node<E> node) {
        Node<E> x = node.right;
        Node<E> z = x.left;
        x.left = node;
        node.right = z;
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    /**
     * Removes an element of the tree set based on its data using the method
     * RemoveRec. Returns true if it was deleted or false if it wasn't.
     * @param o object to be deleted
     * @return boolean
     */
    @Override
    public boolean remove(Object o) {
        try{
            E data = (E) o;
            if(!containsData(data)) throw new Exception("Object "+ data +" isn't on the set.");
            this.root = removeRec(this.root, data);
            return true;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    /**
     * Recursively looks for an element and removes it from the tree set
     * rebalancing it afterwards.
     * @param root root node
     * @param data data to be looked for and removed
     * @return deleted node or null if it wasn't on the set.
     */
    Node<E> removeRec(Node<E> root, E data) {
        if (root == null) return root;
        if (data.compareTo(root.data) < 0) {
            root.left = removeRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = removeRec(root.right, data);
        }else {
            if ((root.left == null) || (root.right == null)) {
                Node<E> temp;
                temp = (root.left == null) ? root.right : root.left;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                Node<E> temp = minValueNode(root.right);
                root.data = temp.data;
                root.right = removeRec(root.right, temp.data);
            }
        }
        if (root == null) return root;
        updateHeight(root);
        return rebalance(root);
    }

    /**
     * Finds the node with the minor value in the
     * tree set based on a root node
     * @param node node from where the search will be started
     * @return min value node
     */
    Node <E> minValueNode(Node<E> node) {
        Node<E> current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    /**
     * Adds all the elements from a Collection to the tree set. If it could be added it returns
     * true, otherwise it returns false.
     * @param collection Collection of elements
     * @return boolean
     */
    @Override
    public boolean addAll(Collection collection) {
        try {
            collection.forEach(x -> {
                E newData = (E) x;
                add(newData);
            });
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Removes all the elements from a collection of the tree set.
     * If one of the elements isn't present on the tree set it still returns true in
     * case the present elements could be removed. If all elements couldn't be removed
     * it returns false.
     * @param collection Collection of elements to be removed
     * @return boolean
     */
    @Override
    public boolean removeAll(Collection collection) {
        try {
            collection.forEach(x -> {
                E newData = (E) x;
                remove(newData);
            });
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    // PENDING IMPLEMENTATION
    @Override
    public boolean retainAll(Collection c) {
        return true;
    }

    /**
     * Checks whether all the elements in a Collection are contained in the tree set.
     * If it doesn't find an element it inmediatly returns false without checking the rest
     * of the collection.
     * @param collection elements to be checked
     * @return boolean
     */
    @Override
    public boolean containsAll(Collection collection) {
        try {
            for(Object o : collection){
                E data = (E) o;
                if (!containsData(data)){
                    return false;
                }
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Adds an array of objects to the tree set and returns an array with
     * all the elements of the set after the insertion.
     * @param objects array
     * @return array of objects contained in the tree set.
     */
    @Override
    public Object[] toArray(Object[] objects) {
        addAll(Arrays.asList(objects));
        return toArray();
    }

    // Tree traversal methods

    public void forEachPostorder(Consumer<Object> action){ forEachPostorderRec(root, action); }

    public void forEachInorder(Consumer<Object> action){
        forEachInorderRec(root,action);
    }

    public void forEachPreorder(Consumer<Object> action){
        forEachPreorderRec(root, action);
    }

    /**
     * Goes in postorder traversal over a tree and performs an action.
     * Each node starting by the root traverses the left subtree,
     * traverses the right subtree and then gets accepted by the consumer (action).
     * @param node - root node
     * @param action - action to be performed in each nodes data.
     */
    private void forEachPostorderRec(Node<E> node, Consumer<Object> action) {
        if (node != null){
            forEachPostorderRec(node.left, action);
            forEachPostorderRec(node.right, action);
            action.accept(node.data);
        }
    }

    /**
     * Goes in inorder traversal over a tree and performs an action.
     * Each node starting by the root traverses the left subtree,
     * gets accepted by the consumer (action) and then traverses the right subtree.
     * @param node - root node
     * @param action - action to be performed in each nodes data.
     */
    private void forEachInorderRec(Node<E> node,Consumer<Object> action) {
        if (node != null){
            forEachInorderRec(node.left, action);
            action.accept(node.data);
            forEachInorderRec(node.right, action);
        }
    }

    /**
     * Goes in preorder traversal over a tree and performs an action.
     * Each node starting by the root gets accepted by the consumer (action) itself,
     * traverses the left subtree,
     * and then traverses the right subtree.
     * @param node - root node
     * @param action - action to be performed in each nodes data.
     */
    void forEachPreorderRec(Node<E> node, Consumer<Object> action) {
        if (node != null){
            action.accept(node.data);
            forEachPreorderRec(node.left, action);
            forEachPreorderRec(node.right, action);
        }
    }

    /**
     * Goes in level order traversal over a tree and performs an action.
     * It performs the action on each level from the top (root node)
     * to the last one, getting the nodes accepted by the consumer (action)
     * in each level from left to right.
     * @param action - action to be performed in each nodes data.
     */
    public void forEachLevelOrder(Consumer<Object> action) {
        int height = height(root);
        for (int i=1; i<=height; i++) forEachInGivenLevel(root, i, action);
    }

    /**
     * Performs an action over the nodes at a given level of the tree from left to right.
     * @param root - root node
     * @param level - level of the tree to print
     * @param action - action to be performed in each nodes data.
     */
    void forEachInGivenLevel (Node<E> root ,int level, Consumer<Object> action) {
        if (root == null) return;
        if (level == 1)
            action.accept(root.data);
        else if (level > 1) {
            forEachInGivenLevel(root.left, level-1, action);
            forEachInGivenLevel(root.right, level-1, action);
        }
    }

    // Node

    /**
     * Wraps all the information about every node on the tree.
     * @param <E> type of elements contained in each node
     */
    private class Node<E extends Comparable> {

        public E data;
        public int height;
        public Node<E> right, left;

        /**
         * Constructor to create a Node without links to other nodes.
         * @param data element contained in the node
         */
        public Node(E data) {
            this.height = 1;
            this.data = data;
            left = right = null;
        }

        /**
         * Constructor to create a linked node to other nodes.
         * @param data element contained in the node
         * @param right right node (containing greater element)
         * @param left left node (containing minor element)
         */
        public Node(E data, Node<E> right, Node<E> left) {
            this(data);
            this.height = 1 + Math.max(left.height, right.height);
            this.right = right;
            this.left = left;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this){
                return true;
            }
            if (!(o instanceof Node)){
                return false;

            }
            Node<E> node = (Node<E>) o;
            return this.data.equals(node.data);
        }

        @Override
        public String toString() {
            return this.data.toString();
        }

    }

    // Iterator

    /**
     * Custom AVlTreeSetIterator. Traverses the tree Post-order and returns the nodes data each time.
     * @param <E> Nodes data type
     */
    private class AVLTreeSetIterator<E extends Comparable> implements Iterator<E> {
        private LinkedList<Node<E>> visited = new LinkedList<>();
        private LinkedList<Node<E>> parents = new LinkedList<>();
        AVLTreeSet.Node current;

        public AVLTreeSetIterator() {
            this.current = root;
            parents.add(current);
        }

        public boolean hasNext() {
            if (parents.isEmpty() || root==null) return false;
            return true;
        }

        public E next() {
            current = parents.removeLast();
            return (E) findNext(current).data;
        }

        public Node<E> findNext(Node<E> current){
            parents.addLast(current);
            if(current.left != null && (!visited.contains(current.left))){
                return findNext(current.left);
            }
            if(current.right !=null && (!visited.contains(current.right))){
                return findNext(current.right);
            }
            visited.add(parents.removeLast());
            return current;
        }
    }

}
