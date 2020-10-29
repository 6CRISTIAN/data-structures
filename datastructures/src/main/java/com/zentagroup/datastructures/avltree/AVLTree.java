package com.zentagroup.datastructures.avltree;

public class AVLTree<E extends Comparable> {

    private Node<E> root;
    private int size;

    public AVLTree() {
        this.size = 0;
    }

    public AVLTree(E data) {
        this.root = new Node(data);
        this.size = 1;
    }

    /**
     * Add a new element to tree. If the element not exists on the tree it wil be added at corresponding subtree
     * as a leaf and will return true. If it already exists inside the tree structure will not be added and returns
     * false.
     * @param data E
     * @return
     */
    public boolean add(E data) {
        if (root == null) {
            this.root = new Node(data);
            ++this.size;
            return true;
        }
        Node<E> newNode = new Node(data);
        return add(newNode, this.root);
    }

    /**
     * Add a new element to tree structure using recursion as a way to insert a new node.
     * @param newNode
     * @param root
     * @return
     */
    private boolean add(Node<E> newNode, Node<E> root) {
        if (root.equals(newNode)) {
            return false;
        }
        boolean inserted = false;
        if (newNode.data.compareTo(root.data) > 0) {
            if (root.right == null) {
                root.right = newNode;
                ++this.size;
                inserted = true;
            } else {
                add(newNode, root.right);
            }
        } else if (newNode.data.compareTo(root.data) < 0) {
            if (root.left == null) {
                root.left = newNode;
                ++this.size;
                inserted = true;
            } else {
                add(newNode, root.left);
            }
        }
        root.height = 1 + Math.max(getNodeHeight(root.left), getNodeHeight(root.right));
        Node<E> balancedTree = balanceTree(root);
        if (balancedTree != null) {
            copyNodeTo(balancedTree, root);
        }
        return inserted;
    }

    private Node<E> balanceTree(Node<E> node) {
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                rotateToLeft(node.left);
            }
            return rotateToRight(node);
        }
        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                rotateToRight(node.right);
            }
            return rotateToLeft(node);
        }
        return null;
    }

    /**
     * Perform rotation to the right of a subtree that needs to be balanced.
     * @param node
     * @return
     */
    private Node<E> rotateToRight(Node<E> node) {
        Node<E> l = node.left;
        Node<E> lr = l.right;
        Node<E> newNode = new Node(node);
        l.right = newNode;
        newNode.left = lr;
        newNode.height = 1 + Math.max(getNodeHeight(newNode.left), getNodeHeight(newNode.right));
        l.height = 1 + Math.max(getNodeHeight(l.left), getNodeHeight(l.right));
        return l;
    }

    /**
     * Perform rotation to the left of a subtree that needs to be balanced.
     * @param node
     * @return
     */
    private Node<E> rotateToLeft(Node<E> node) {
        Node<E> r = node.right;
        Node<E> rl = r.left;
        Node<E> newNode = new Node(node);
        r.left = newNode;
        newNode.right = rl;
        newNode.height = 1 + Math.max(getNodeHeight(newNode.left), getNodeHeight(newNode.right));
        r.height = 1 + Math.max(getNodeHeight(r.left), getNodeHeight(r.right));
        return r;
    }

    /**
     * Get node height
     * @param node
     * @return
     */
    private int getNodeHeight(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Return balance factor. This number determines if subtree needs to be balanced or not.
     * @param node
     * @return
     */
    private int getBalanceFactor(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return getNodeHeight(node.left) - getNodeHeight(node.right);
    }

    /**
     * Helper function to replace root of a subtree that needs to be balanced.
     * @param copyFrom
     * @param copyTo
     */
    private void copyNodeTo(Node<E> copyFrom, Node<E> copyTo) {
        copyTo.data = copyFrom.data;
        copyTo.right = copyFrom.right;
        copyTo.left = copyFrom.left;
        copyTo.height = copyFrom.height;
    }

    public void preOrder() {
        this.preOrder(this.root);
    }

    public void inOrder() {
        this.inOrder(this.root);
    }

    public void postOrder() {
        this.postOrder(this.root);
    }

    /**
     * Pre-Order tree traverse.
     * @param node
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
     * @param node
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

    private class Node<E extends Comparable> implements Cloneable {

        public Node<E> right, left;
        public E data;
        public int height;

        public Node(E data) {
            this.data = data;
            this.height = 1;
        }

        /**
         * Creates a Node instance based on another Node object.
         * @param node
         */
        public Node(Node<E> node) {
            this.data = node.data;
            this.left = node.left;
            this.right = node.right;
            this.height = node.height;
        }

        public Node(E data, Node<E> right, Node<E> left) {
            this(data);
            this.right = right;
            this.left = left;
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
