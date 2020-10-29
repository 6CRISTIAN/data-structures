package com.zentagroup.datastructures.binarytree;

public class BinaryTree<E extends Comparable> {

    private Node<E> root;

    /**
     * Constructor that instantiate an empty Tree.
     */
    public BinaryTree() {}

    /**
     * Constructor that insert a new node on the tree as a root.
     * @param data E
     */
    public BinaryTree(E data) {

        this.root = new Node(data);
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

    /**
     * Wrap all information about every node on the tree.
     * @param <E>
     */
    private class Node<E extends Comparable> {

        public Node<E> right, left;
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
