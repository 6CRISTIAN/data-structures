package com.zentagroup.datastructures.redblacktree;


import java.util.*;
import java.util.function.Consumer;

public class RedBlackTree<E extends Comparable> implements Set<E> {
    private RBNode<E> root;

    @Override
    public int size() {
        return size(this.root);
    }

    private int size(RBNode<E> root) {
        if (null == root)
            return 0;
        eturn size (root.left) + size(root.right) + 1;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public boolean contains(Object o) {
        RBNode<E> search = this.root;
        while (null != search) {
            if (search.data.equals(o))
                return true;
            search = search.data.compareTo(o) > 0 ? search.left : search.right;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new nodeIterator(this.root);
    }

    @Override
    public Object[] toArray() {
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++)
            r[i] = it.next();
        return r;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> it = iterator();
        int i = 0;
        while (it.hasNext())
            a[i++] = (T) it.next();
        return a;
    }

    @Override
    public boolean add(E e) {
        if (null == root) {
            this.root = new RBNode(e, false);
            return true;
        }
        RBNode<E> newNode = new RBNode(e, true);
        newNode.parent = root;
        return add(newNode);
    }

    /**
     * search the appropriate position for the new node and solve case if his parent is red
     *
     * @param newNode
     * @return true if the node was added
     */
    private boolean add(RBNode<E> newNode) {
        if (newNode.data.compareTo(newNode.parent.data) == 0)
            return false;
        if (newNode.data.compareTo(newNode.parent.data) < 0) {
            if (null == newNode.parent.left) {
                newNode.parent.left = newNode;
            } else {
                newNode.parent = newNode.parent.left;
                return add(newNode);
            }
        } else if (newNode.data.compareTo(newNode.parent.data) > 0) {
            if (null == newNode.parent.right) {
                newNode.parent.right = newNode;
            } else {
                newNode.parent = newNode.parent.right;
                return add(newNode);
            }
        }
        if (newNode.parent.red)
            addCases(newNode.parent, isLeft(newNode));
        return true;
    }

    /**
     * changes the color of uncle, grandpa and parent, if necessary, execute other method to make rotations
     *
     * @param parent
     * @param newIsLeft
     */
    private void addCases(RBNode<E> parent, boolean newIsLeft) {
        if (null != parent.parent.left && null != parent.parent.right) {
            RBNode<E> uncle = sibling(parent);
            if (uncle.red) {
                uncle.red = false;
                parent.red = false;
                if (parent.parent != root)
                    parent.parent.red = true;
                if (null != parent.parent.parent) {
                    if (parent.parent.parent.red)
                        addCases(parent.parent.parent, parent.parent.parent.left == parent.parent);
                }
                return;
            }
        }
        runRotationsToAdd(parent, newIsLeft);
    }

    /**
     * run rotations to balance this tree, if parent and new node are side opposite sons, changes its colors
     *
     * @param parent
     * @param newIsLeft
     */
    private void runRotationsToAdd(RBNode<E> parent, boolean newIsLeft) {
        if (newIsLeft && isLeft(parent)) {
            rightRotation(parent.parent);
            parent.right.red = true;
            parent.red = false;
        } else if (!newIsLeft && !isLeft(parent)) {
            leftRotation(parent.parent);
            parent.left.red = true;
            parent.red = false;
        } else if (newIsLeft && !isLeft(parent)) {
            rightRotation(parent);
            addCases(parent.parent, false);
        } else if (!newIsLeft && isLeft(parent)) {
            leftRotation(parent);
            addCases(parent.parent, true);
        }
    }

    private void leftRotation(RBNode<E> n) {
        RBNode<E> aux = n.right.left;
        if (root == n) {
            n.right.parent = null;
            this.root = n.right;
        } else {
            n.right.parent = n.parent;
            if (isLeft(n))
                n.parent.left = n.right;
            else
                n.parent.right = n.right;
        }
        n.right.left = n;
        n.parent = n.right;
        n.right = aux;
        if (aux != null)
            aux.parent = n;
    }

    private void rightRotation(RBNode<E> n) {
        RBNode<E> aux = n.left.right;
        if (root == n) {
            n.left.parent = null;
            this.root = n.left;
        } else {
            n.left.parent = n.parent;
            if (isLeft(n))
                n.parent.left = n.left;
            else
                n.parent.right = n.left;
        }
        n.left.right = n;
        n.parent = n.left;
        n.left = aux;
        if (aux != null)
            aux.parent = n;
    }

    private boolean isLeft(RBNode<E> n) {
        Objects.requireNonNull(n);
        return n.parent != null && n.parent.left == n;
    }

    @Override
    public boolean remove(Object o) {
        RBNode<E> rm = this.root;
        while (null != rm) {
            if (rm.data.compareTo(o) == 0)
                break;
            rm = rm.data.compareTo(o) > 0 ? rm.left : rm.right;
        }
        if (null == rm)
            return false;
        proceedToRemove(rm);
        return true;
    }

    /**
     * all the methods to delete, just change the data of the node to be deleted by the data of its substitute node
     *
     * @param rm
     */
    private void proceedToRemove(RBNode<E> rm) {
        if (null == rm.left && null == rm.right) {
            rmChildlessNode(rm);
        } else if (null == rm.left) {
            rmNodeWithSingleChild(rm, false);
        } else if (null == rm.right) {
            rmNodeWithSingleChild(rm, true);
        } else {
            rmNodeWithChildren(rm);
        }
    }

    private void rmChildlessNode(RBNode<E> rm) {
        if (rm.red) {
            if (isLeft(rm))
                rm.parent.left = null;
            else
                rm.parent.right = null;
        } else {
            if (this.root == rm) {
                this.root = null;
            } else {
                if (isLeft(rm)) {
                    rm.parent.left = null;
                    removeCases(rm.parent.left, rm.parent.right);
                } else {
                    rm.parent.right = null;
                    removeCases(rm.parent.right, rm.parent.left);
                }
            }
        }
    }

    private void rmNodeWithSingleChild(RBNode<E> rm, boolean subsIsLeft) {
        RBNode<E> subs = subsIsLeft ? rm.left : rm.right;
        subs.red = false;
        if (rm == root) {
            this.root = subs;
            subs.parent = null;
        } else {
            subs.parent = rm.parent;
            if (isLeft(rm))
                rm.parent.left = subs;
            else
                rm.parent.right = subs;
        }
    }

    /**
     * this method is only called, when a node to remove has two children
     *
     * @param rm
     */
    private void rmNodeWithChildren(RBNode<E> rm) {
        RBNode<E> subs = rm.right;
        while (null != subs.left) {
            subs = subs.left;
        }
        if (rm.red && subs.red || !rm.red && subs.red) {
            rm.data = subs.data;
            rmSubstitute(rm, subs);
        } else if (rm.red) {
            rm.data = subs.data;
            if (rm.right == subs) {
                rmSubstitute(rm, subs);
                removeCases(rm.right, rm.left);
            } else {
                rmSubstitute(rm, subs);
                removeCases(subs.right, subs.parent.right);
            }
        } else {
            rm.data = subs.data;
            if (rm.right == subs) {
                rmSubstitute(rm, subs);
                removeCases(rm.right, rm.left);
            } else {
                rmSubstitute(rm, subs);
                removeCases(subs.right, subs.parent.right);
            }
        }
    }

    /**
     * depending on the colors of the substitute node, its sibling and the sibling's children,
     * a specific action is executed, This method analyzes the color state of the nodes mentioned above.
     * if any node is nil is considered as black color
     *
     * @param substitute
     * @param sibling
     */
    private void removeCases(RBNode<E> substitute, RBNode<E> sibling) {
        boolean substituteRed = null != substitute;
        if (null != substitute) {
            sibling = sibling(substitute);
            substituteRed = substitute.red;
        }
        if (substituteRed) {
            substitute.red = false;
            return;
        } else if (sibling.red) {
            sibling = rotateParentAndNewSibling(sibling);
            if (root == sibling)
                return;
        }
        if (1 == getColorOfChildren(sibling)) {
            newSubstituteAndSetColorSibling(substitute, substituteRed, sibling);
            return;
        }
        if (1 != getColorOfChildren(sibling)) {
            balanceSiblingWithRedSons(sibling);
        }
    }

    /**
     * this method only analyzes if all the nodes are black and
     * if one or both of the sibling's child nodes are red, to execute a specific function
     *
     * @param sibling
     */
    private void balanceSiblingWithRedSons(RBNode<E> sibling) {
        if (!isLeft(sibling) && 3 == getColorOfChildren(sibling) ||
                isLeft(sibling) && 2 == getColorOfChildren(sibling))
            sibling = rotateAndSetNewSibling(sibling);
        if (!isLeft(sibling) && 2 == getColorOfChildren(sibling) ||
                !isLeft(sibling) && 0 == getColorOfChildren(sibling))
            setSiblingColorAndRotateParent(sibling);
        else if (isLeft(sibling) && 3 == getColorOfChildren(sibling) ||
                isLeft(sibling) && 0 == getColorOfChildren(sibling))
            setSiblingColorAndRotateParent(sibling);
    }

    /**
     * this is execute when substitute is black and its sibling is red,
     * change sibling color and rotate his parent, if sibling becomes root and has a red
     * grandson, it changes colors of the new children, calling the changeGrandsonColor method
     *
     * @param sibling
     * @return sibling to be used in case it is due by removeCases
     */
    private RBNode<E> rotateParentAndNewSibling(RBNode<E> sibling) {
        sibling.red = false;
        sibling.parent.red = true;
        if (!isLeft(sibling)) {
            leftRotation(sibling.parent);
            if (root == sibling) {
                if (changeGrandsonAndChildColor(sibling, false)) {
                    return sibling;
                }
            }
            sibling = sibling.left.right;

        } else {
            rightRotation(sibling.parent);
            if (root == sibling) {
                if (changeGrandsonAndChildColor(sibling, true)) {
                    return sibling;
                }
            }
            sibling = sibling.right.left;
        }
        return sibling;
    }

    /**
     * change color of the grandchild and child of root to keep the parameters of a black red tree
     *
     * @param sibling
     * @param isLeft
     * @return true if any node is updated
     */
    private boolean changeGrandsonAndChildColor(RBNode<E> sibling, boolean isLeft) {
        if (!isLeft && null != sibling.left.right && null == sibling.left.right.left
                && null == sibling.left.right.right) {
            sibling.left.red = false;
            sibling.left.right.red = true;
            return true;
        } else if (isLeft && null != sibling.right.left && null == sibling.right.left.left
                && null == sibling.right.left.right) {
            sibling.right.red = false;
            sibling.right.left.red = true;
            return true;
        }
        return false;
    }

    /**
     * validate if the child of the root is black, but it is not, color it black
     * this only use when parent of 'n' is root, and substitute, sibling and sibling's children are black
     *
     * @param n
     */
    private void validateColorRootChild(RBNode<E> n) {
        if (null != n.left && 3 == getColorOfChildren(n.left)) {
            if (!n.left.red)
                n.red = true;
        } else if (null != n.right) {
            if (!n.right.red)
                n.red = true;
        }
    }

    /**
     * set red to sibling and set a new substitute (it'll be parent of substitute) if necessary change the
     * color of new substitute, finally, verify if the new assignments comply the conditions for other method.
     *
     * @param subs
     * @param subsRed
     * @param sibling
     */
    private void newSubstituteAndSetColorSibling(RBNode<E> subs, boolean subsRed, RBNode<E> sibling) {
        if (root == sibling.parent.parent) {
            if (isLeft(sibling.parent)) {
                validateColorRootChild(sibling.parent.parent.right);
            } else {
                validateColorRootChild(sibling.parent.parent.left);
            }
        }
        sibling.red = true;
        subs = sibling.parent;
        subsRed = subs.red;
        if (null != subs.parent) {
            sibling = sibling(subs);
        }
        if (subsRed)
            subs.red = false;
        if (!subsRed && !sibling.red && 1 != getColorOfChildren(sibling)) {
            balanceSiblingWithRedSons(sibling);
        }
    }

    /**
     * changes color and rotate sibling, then set a new sibling
     *
     * @param sibling
     * @return new sibling
     */
    private RBNode<E> rotateAndSetNewSibling(RBNode<E> sibling) {
        sibling.red = true;
        if (isLeft(sibling)) {
            if (null != sibling.right)
                sibling.right.red = false;
            leftRotation(sibling);
        } else {
            if (null != sibling.left)
                sibling.left.red = false;
            rightRotation(sibling);
        }
        sibling = sibling.parent;
        return sibling;
    }

    /**
     * set a new color of sibling, its parent and its kid. finally run a rotation to parent
     *
     * @param sibling
     */
    private void setSiblingColorAndRotateParent(RBNode<E> sibling) {
        sibling.red = sibling.parent.red;
        sibling.parent.red = false;
        if (isLeft(sibling)) {
            if (null != sibling.left) {
                sibling.left.red = false;
            }
            rightRotation(sibling.parent);
        } else {
            if (null != sibling.right) {
                sibling.right.red = false;
            }
            leftRotation(sibling.parent);
        }
    }

    /**
     * This method gets color of the children of any node, return 4 possible status: 0, 1, 2, or 3
     * 0 -> left.red - right.red, 1 -> left.black - right.black
     * 2 -> left.black - right.red, 3 -> left.red - right.black
     * if any son is nil is considered as black color
     *
     * @param n
     * @return the color of children of any node. represented by a number
     */
    private byte getColorOfChildren(RBNode<E> n) {
        if (null == n.left && null == n.right) {
            return 1;
        } else if (null == n.right) {
            return (byte) (n.left.red ? 3 : 1);
        } else if (null == n.left) {
            return (byte) (n.right.red ? 2 : 1);
        } else {
            if (n.left.red && n.right.red)
                return 0;
            else if (!n.left.red && !n.right.red)
                return 1;
            else if (!n.left.red)
                return 2;
            else
                return 3;
        }
    }

    private void rmSubstitute(RBNode<E> rm, RBNode<E> subs) {
        if (rm.right != subs) {
            subs.parent.left = subs.right;
            if (null != subs.right)
                subs.right.parent = subs.parent;
        } else {
            rm.right = subs.right;
            if (null != subs.right)
                subs.right.parent = rm;
        }
    }

    private RBNode<E> sibling(RBNode<E> n) {
        Objects.requireNonNull(n.parent);
        return n == n.parent.left ? n.parent.right : n.parent.left;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int previousSize = size();
        for (E e : c) {
            add(e);
        }
        return previousSize != size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean update = false;
        for (Object e : c)
            if (!contains(e)) {
                add((E) e);
                update = true;
            }
        int i = -1;
        while (iterator().hasNext())
            if (!c.contains(++i)) {
                remove(i);
                update = true;
            }
        return update;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int previousSize = size();
        for (Object o : c)
            remove(o);
        return previousSize != size();
    }

    @Override
    public void clear() {
        this.root = null;
    }

    public void preOrder() {
        this.preOrder(this.root);
    }

    public void inOrder() {
        inOrder(this.root);
        //forEachInOrder(System.out::println);
    }

    public void forEachInOrder(Consumer<E> action) {
        forEachInOrder(this.root, action);
    }

    private void forEachInOrder(RBNode<E> node, Consumer<E> action) {
        Objects.requireNonNull(node);
        if (null != node.left)
            forEachInOrder(node.left, action);
        action.accept(node.data);
        if (null != node.right)
            forEachInOrder(node.right, action);
    }

    public void postOrder() {
        this.postOrder(this.root);
    }

    private void inOrder(RBNode<E> node) {
        if (node.left != null)
            inOrder(node.left);
        System.out.println(node.data + " " + node.red);
        if (node.right != null)
            inOrder(node.right);
    }

    private void preOrder(RBNode<E> node) {
        System.out.println(node.data);
        if (node.left != null)
            preOrder(node.left);
        if (node.right != null)
            preOrder(node.right);
    }

    private void postOrder(RBNode<E> node) {
        if (node.left != null)
            postOrder(node.left);
        if (node.right != null)
            postOrder(node.right);
        System.out.println(node.data);
    }

    private class RBNode<E extends Comparable> {
        public RBNode<E> parent, left, right;
        public boolean red;
        public E data;

        public RBNode(E data, boolean red) {
            this.data = data;
            this.red = red;
        }
    }

    public class nodeIterator implements Iterator<E> {
        private Queue<E> queue = new LinkedList();

        public nodeIterator(RBNode<E> root) {
            getTreeValues();
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public E next() {
            return queue.poll();
        }

        private void getTreeValues() {
            forEachInOrder(n -> queue.add(n));
        }
    }
}