package com.zentagroup.datastructures.binarytree;

import java.util.*;

public class BinaryTreeSet<E extends Comparable> implements Set<E> {
    private int heightNum;
    private List<Comparable> list= new ArrayList<Comparable>();
    private int sizeNum = 0;
    private Node next;

    private Node<E> root;

    /**
     * Constructor that instantiate an empty Tree.
     */
    public BinaryTreeSet() {}

    /**
     * Constructor that insert a new node on the tree as a root.
     * @param data E
     */
    public BinaryTreeSet(E data) {
        this.root = new Node(data);
    }

    /**
     * method to find out how many nodes the tree has, if the root is 0 the sizeNum is 0
     * @return another method size(Node<E> node), how real find out how many nodes the tree has
     */
    @Override
    public int size() {
        sizeNum = 0;
        if(this.root== null){
            sizeNum = 0;
        }
        return size(this.root);
    }
    /**
     * method to find out how many nodes the tree has, using recursion
     * @return sizeNum that is node numbers
     */
    public int size(Node<E> node) {
        if (node != null) {
            if (node.left != null) {
                size(node.left);
            }
            if (node.right != null) {
                size(node.right);
            }
            sizeNum++;
        }
        return sizeNum;
    }
    /**
     * method to find out how many levels the tree has, using recursion
     * @return heightNum that is level's tree numbers
     */

    public int height() {
        heightNum = 0;
        if(root==null){
            heightNum = 0;
        }else {
            height(root, 1);
        }
        return heightNum;
    }
    private void height(Node node, int level) {
        if (node != null) {
            height(node.left, level + 1);
            if (level > heightNum) {
                heightNum = level;
            }
            height(node.right, level + 1);
        }
    }

    /**
     * method for know if the tree is empty
     * @return true if the tree is empty and false if tree have at least one node (the root)
     */
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * method to verify if a node exist in to the tree
     * @param o
     * @return true if the node exist and false if the node does not exist
     */

    @Override
    public boolean contains(Object o) {
        Node<E> node = root;
        while (node != null) {
            if (o == node.data) {
                return true;
            } else if (node.data.compareTo(o) < 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return false;
    }

    /**
     * method to iterated the tree
     * @return new Iterator object
     */
   @Override
    public Iterator<E> iterator() {
        return new BinaryTreeIterator();
    }

    /*
    @Override
    public Iterator<E> iterator() {
        this.iteratorInOrder(this.root);
        return list.iterator();
    }*/

    /**
     * method for became the tree like a array, when we save the tree's node in-orden
     * and print in console
     * @return the tree like a array
     */

    @Override
    public Object[] toArray() {
        this.iteratorInOrder(this.root);
        Object[] myArray = list.toArray();
        for (Object i: myArray) {
            System.out.println(i);
        }
        return myArray;
    }

    /**
     * method to get an array, add to the tree, and then call toArray() method for
     * print in the console the nodes of the tree
     * @param ts
     * @param <T>
     * @return
     */
    @Override
    public <T> T[] toArray(T[] ts) {
        Collection c = Arrays.asList(ts);
        addAll(c);
        this.toArray();
        return ts;
    }



    /**
     * Add a new element to tree. If the element not exists on the tree it wil be added at corresponding subtree
     * as a leaf and will return true. If it already exists inside the tree structure will not be added and returns
     * false.
     * @param data E
     * @return boolean if true if the node was added and false if not
     */
    public boolean add(E data) {
        if (root == null) {
            this.root = new Node(data);
            return true;
        }
        Node<E> newNode = new Node(data);
        return add(newNode, this.root);
    }

    /**
     *  remove a element into the tree, not matter the position of the element in the tree, ande we use the node
     *  to remove like a father for compare if the node have or not children we use 4 different cases for remove,
     *  case 1 if the node if the root of the tree,
     *  case 2 if the node to remove don't have a son,
     *  case 3 e if the node to remove have one son, then we check if the right son or left son and the
     *  case 4 when de node have 2 son
     * @param o Object
     * @return boolean if true if de node was removed and false if not.
     */

    @Override
    public boolean remove(Object o) {
        boolean stop =true;
        Node<E> node = root;
        Node<E> papa = node.father;
        while (stop) {
            if (o == node.data) {
                node.data.equals(node);
                node.setFather(papa);
                stop = false;
            } else if (node.data.compareTo(o) < 0) {
                papa = node;
                node = node.right;
            } else {
                papa = node;
                node = node.left;
            }
        }

        boolean sonLeftToCompare = node.left != null;
        boolean sonRightToCompare = node.right != null;
        boolean root = node.father != null;
        Node sonLeft = node.father.left;
        Node sonRight = node.father.right;


        /**
         * case 1 for remove, if the node to removed is the root, we set the data of the node like a null
         */
        if(!sonRightToCompare && !sonLeftToCompare && !root){ //remove the root
           try {
                node.data=null;
            }catch (Exception e){
                System.out.println(e.getStackTrace());
            }
        }
        /**
         * cases 2 for removed, if the node to remove don't have a son, then we removed setting the data of the node
         * like a null
         */

        if (!sonRightToCompare && !sonLeftToCompare) {

            if ( sonLeft == node ) {
                node.father.setLeft( null );
                return true;
            }
            if ( sonRight == node) {
                node.father.setRight( null );
                return true;
            }

            return false;
        }
        /**
         * case 3 for removed, if the node to removed have a one son, first check if the node have a right or left son, and then we change
         *the data of the node to remove to the date of the son, and set the data of the son like null.
         */
        if ( sonRightToCompare && !sonLeftToCompare ) { //right son
            Node<E> leaf = node.getLeft() != null ? node.getLeft() : node.getRight();

            if ( sonLeft == node ) {
                node.getFather().setLeft( leaf );

                leaf.setFather(node.getFather());
                node.setRight(null);
                node.setLeft(null);

                return true;
            }
            if ( sonRight == node) {
                node.getFather().setRight( leaf );

                leaf.setFather(node.getFather());
                node.setRight(null);
                node.setLeft(null);

                return true;
            }

            return false;
        }
        /**
         * case 3 for removed, if the node to removed have a one son, first check if the node have a right or left son, and then we change
         *the data of the node to remove to the data of the son, and set the data of the son like null.
         */
        if ( !sonRightToCompare && sonLeftToCompare ) { //left son
            Node<E> leaf = node.getLeft() != null ? node.getLeft() : node.getRight();

            if ( sonLeft == node ) {
                node.getFather().setLeft( leaf );

                leaf.setFather(node.getFather());
                node.setRight(null);
                node.setLeft(null);

                return true;
            }
            if ( sonRight == node) {
                node.getFather().setRight( leaf );
                leaf.setFather(node.getFather());
                node.setRight(null);
                node.setLeft(null);
                return true;
            }

            return false;
        }
        /**
         * case 4 for remove, if the node to removed have a two son right and left,then we change the data
         * of the node to remove, like son left data, and remove the left son.
         */
        if ( sonRightToCompare && sonLeftToCompare ) {
            Node<E> nodeR = goToLeft( node.getRight() );

            if ( nodeR != null ) {

                remove(nodeR.getData());
                node.setData(nodeR.getData());

                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * tree traverse but only to the left, it used for case 4 to removed
     * @param node Node
     * @return node if the left must to the right in the subtree we are removed
     */

    private Node<E> goToLeft(Node<E> node) {
        if (node.getLeft() != null) {
            return goToLeft( node.getLeft() );
        }
        return node;
    }
    /**
     * method that check if all elements of a collection exist into the tree
     * @param collection type Collection
     * @return true if all elements exist, and false if not
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object i: collection){
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * method that adds all elements of a collection
     * @param collection type Collection
     * @return true if all elements were added, and false if not
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {

        boolean everyElementCreated = true;
        for(E i : collection){
            if (!this.add(i)) {
                everyElementCreated = false;
            }
        }
        return everyElementCreated;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    /**
     * method to remove the all elements of a collection, but first
     * check if the elements exist into the tree
     * @param collection type Collection
     * @return true if all elements were removed, and false if not
     */

    @Override
    public boolean removeAll(Collection<?> collection) {
        for (Object i: collection){
            if (contains(i)) {
                this.remove(i);
            }else {
                return false;
            }
        }
        return true;
    }

    /**
     * method for delete the tree, set root tree null;
     */

    @Override
    public void clear() {
        this.root= null;
    }

    /**
     * Add a new element to tree structure using recursion as a way to insert a new node.
     * @param newNode
     * @param root
     * @return
     */
    private boolean add( Node<E> newNode, Node<E> root) {
        if (root.equals(newNode)) {
            return false;
        }
        if (newNode.data.compareTo(root.data) > 0) {
            if (root.right == null) {
                root.right = newNode;
                return true;
            } else {
                return add(newNode, root.right);
            }
        } else if (newNode.data.compareTo(root.data) < 0) {
            if (root.left == null) {
                root.left = newNode;
                return true;
            } else {
                return add(newNode, root.left);
            }
        }
        return false;
    }
    /**
     * Pre-Order tree traverse.
     * they call similar method how receive a parameter (the root) for star the traverse, but first check if the node,
     *is empty
     */
    public void preOrder() {
        if(this.root==null){
            System.out.println("tree is empty");
        }else {
            this.preOrder(this.root);
        }

    }
    /**
     * In-Order tree traverse.
     * they call similar method how receive a parameter (the root) for star the traverse, but first check if the node,
     * is empty
     */

    public void inOrder() {
        if(this.root==null){
            System.out.println("tree is empty");
        }else {
            this.inOrder(this.root);
        }

    }
    /**
     * Post-Order tree traverse.
     * they call similar method how receive a parameter (the root) for star the traverse, but first check if the node,
     * is empty
     */

    public void postOrder() {
        if(this.root==null){
            System.out.println("tree is empty");
        }else {
            this.postOrder(this.root);
        }
    }

    /**
     * Pre-Order tree traverse.
     * @param node Node<E>
     */
    private void preOrder(Node<E> node) {
        System.out.println(node);
        if (node.left != null) {
            preOrder(node.left);
        }
        if (node.right != null) {
            preOrder(node.right);
        }
    }

    /**
     * In-Order tree traverse.
     * @param node Node<E>
     */

    private void inOrder(Node<E> node) {
        if (node.left != null) {
            inOrder(node.left);
        }
        System.out.println(node);
        if (node.right != null) {
            inOrder(node.right);
        }

    }
    /**
     * In-Order tree traverse. for add into a List list
     * @param node
     */
    private void iteratorInOrder(Node<E> node) {
        if (node.left != null) {
            iteratorInOrder(node.left);
        }
        list.add(node.data);
        if (node.right != null) {
            iteratorInOrder(node.right);
        }
    }

    /**
     * Post-Order tree traverse.
     * @param node Node<E>
     */
    private void postOrder(Node<E> node) {
        if (node.left != null) {
            postOrder(node.left);
        }
        if (node.right != null) {
            postOrder(node.right);
        }
        System.out.println(node);
    }

    private class BinaryTreeIterator<E extends Comparable> implements Iterator<E>{
        private final List<Comparable> listIterator= new ArrayList<Comparable>();
        private int sizeList= 0;
        private int position =0;
        private BinaryTreeSet.Node node = root;

        /**
         * Constructor to create a new list, with all elements of the tree and the size of the list
         */
        public BinaryTreeIterator(){
            List<Comparable> newList = null;
            this.treeIterator(node);
            newList=listIterator;
            sizeList=newList.size();
        }

        /**
         * Method to know if exist a next element into the list, the method compare if size of the list is
         * greater than 0
         * @return return a boolean, if tur if exist a next element in the list, and false is not,
         */
        @Override
        public boolean hasNext() {
            return (sizeList>0);
        }


        /**
         *
         * @return
         */
        @Override
        public E next() {
            E nextNode = (E) listIterator.get(position);
            position++;
            sizeList--;
            return nextNode;
        }

        /**
         *  In-Order tree traverse. for add into a listIterator
         * @param node Node<E>
         */
        private void treeIterator(Node<E> node) {
            if(node==null){
                System.out.println("the tree does not have a elements ");
            }
            if (node.left != null) {
                treeIterator(node.left);
            }
            listIterator.add(node.data);
            if (node.right != null) {
                treeIterator(node.right);
            }
        }

    }


    /**
     * Wrap all information about every node on the tree.
     * @param <E>
     */
    private class Node<E extends Comparable> {

        public Node<E> right, left, father;
        public E data;

        /**
         * Constructor to create a Node without links to other nodes.
         * @param data
         */
        public Node(E data) {
            this.data = data;
        }

        /**
         * Constructor to create a linked node to other nodes.
         * @param data
         * @param right
         * @param left
         */
        public Node(E data, Node<E> right, Node<E> left) {
            this(data);
            this.right = right;
            this.left = left;
        }

        public Node() {

        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> right) {
            this.right = right;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> left) {
            this.left = left;
        }

        public Node<E> getFather() {
            return father;
        }

        public void setFather(Node<E> father) {
            this.father = father;
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            Node<E> node = (Node<E>) o;
            return this.data.equals(node.data);
        }

        @Override
        public String toString() {
            return this.data.toString();
        }

    }

}
/**
 * if(!egress.containsKey(e.vertex.data.getClass().getName())){
 *                 egress.put(e.vertex.data.getClass().getName(), new AVLTreeSet());
 *             }
 *             egress.get(e.vertex.data.getClass().getName()).add(e);
 *             if(!ingress.containsKey(e.vertex.data.getClass().getName())){
 *                 ingress.put(e.vertex.data.getClass().getName(), new AVLTreeSet());
 *             }
 *             ingress.get(e.vertex.data.getClass().getName()).add(e);
 *
 *             if(isBidirectional){
 *                 if(!e.vertex.ingress.containsKey(tag)){
 *                     e.vertex.ingress.put(tag, new AVLTreeSet());
 *                 }
 *                 e.vertex.ingress.put(tag, new Edge(e.relationship, this));
 *                 if(!e.vertex.egress.containsKey(tag)){
 *                     e.vertex.egress.put(tag, new AVLTreeSet());
 *                 }
 *                 e.vertex.egress.put(tag, new Edge(e.relationship, this));
 *             }
 *             return true;
 * **/