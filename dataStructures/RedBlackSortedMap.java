package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.Serial;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * RedBlack Tree Sorted Map
 * @author Tomás Silvestre & Ricardo Laur
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class RedBlackSortedMap<K extends Comparable<K>, V> extends AdvancedBSTree<K, V> {

    public RedBlackSortedMap() {
        super();
    }

    /**
     * Insert or update a key-value pair in the map.
     * @param key Key to insert or update
     * @param value Value to insert
     * Time complexity: O(log n)
     * @return The old value if the key already existed, or null otherwise
     */
    @Override
    public V put(K key, V value) {
        V old = super.put(key, value);
        if (old == null) {
            currentSize++;
        }
        @SuppressWarnings("unchecked")
        RBNode<Entry<K, V>> node = (RBNode<Entry<K, V>>) super.locateNode(root, key);
        if (node != null) recolorAndRotate(node);
        return old;
    }

    private void recolorAndRotate(RBNode<Entry<K,V>> node){
        RBNode<Entry<K,V>> parent = node.getParent();
        if(node!=null && parent.getColor() == Color.RED){
            RBNode<Entry<K,V>> gParent = node.getParent().getParent();
            RBNode<Entry<K,V>> uncle = parent.isLeftChild() ? gParent.getRightChild() :
                    gParent.getLeftChild();
            if(uncle != null && uncle.getColor() == Color.RED){
                recoloring(parent,uncle,gParent);
            } else if(parent.isLeftChild()){
                handleLeftSituation(node,parent,gParent);
            } else if(!parent.isLeftChild()){
                handleRightSituation(node,parent,gParent);
            }
        }
        ((RBNode<Entry<K,V>>) root).setColor(Color.BLACK);
    }

    private void recoloring(RBNode<Entry<K,V>> parent, RBNode<Entry<K,V>> uncle,
                                  RBNode<Entry<K,V>> gParent){
        uncle.flipColor();
        parent.flipColor();
        gParent.flipColor();
        recolorAndRotate(gParent);
    }

    @SuppressWarnings("unchecked")
    private void handleLeftSituation(RBNode<Entry<K,V>> node, RBNode<Entry<K,V>> parent,
                                     RBNode<Entry<K,V>> gParent){
        if(!node.isLeftChild()){
            rotateLeft(parent);
        }
        parent.flipColor();
        gParent.flipColor();
        rotateRight(gParent);
        recolorAndRotate(node.isLeftChild() ? parent : gParent);
    }

    @SuppressWarnings("unchecked")
    private void handleRightSituation(RBNode<Entry<K,V>> node, RBNode<Entry<K,V>> parent,
                                     RBNode<Entry<K,V>> gParent){
        if(node.isLeftChild()){
            rotateRight(parent);
        }
        parent.flipColor();
        gParent.flipColor();
        rotateLeft(gParent);
        recolorAndRotate(node.isLeftChild() ? gParent : parent);
    }

    @Override
    protected BTNode<Entry<K, V>> createNode(Entry<K, V> entry, BTNode<Entry<K, V>> parent) {
        @SuppressWarnings("unchecked")
        RBNode<Entry<K, V>> rbParent = (parent instanceof RBNode) ? (RBNode<Entry<K, V>>) parent : null;
        return new RBNode<>(entry, rbParent);
    }

    @Override
    public V remove(K key) throws NoSuchElementException {
        @SuppressWarnings("unchecked")
        BTNode<Entry<K, V>> found = super.locateNode(root, key);
        if (found == null) throw new NoSuchElementException();

        @SuppressWarnings("unchecked")
        RBNode<Entry<K, V>> z = (RBNode<Entry<K, V>>) found;
        V oldValue = z.getElement().value();

        RBNode<Entry<K, V>> y = z;
        Color yOriginalColor = y.getColor();
        RBNode<Entry<K, V>> x = null;

        if (z.getLeftChild() == null) {
            Node<Entry<K, V>> rawRight = z.getRightChild();
            x = (rawRight instanceof RBNode) ? (RBNode<Entry<K, V>>) rawRight : null;
            transplant(z, rawRight);
        } else if (z.getRightChild() == null) {
            Node<Entry<K, V>> rawLeft = z.getLeftChild();
            x = (rawLeft instanceof RBNode) ? (RBNode<Entry<K, V>>) rawLeft : null;
            transplant(z, rawLeft);
        } else {
            @SuppressWarnings("unchecked")
            BTNode<Entry<K, V>> succBt = (BTNode<Entry<K, V>>) z.getRightChild();
            BTNode<Entry<K, V>> succ = succBt.furtherLeftElement();

            if (!(succ instanceof RBNode)) {
                throw new IllegalStateException();
            }

            @SuppressWarnings("unchecked")
            RBNode<Entry<K, V>> ySucc = (RBNode<Entry<K, V>>) succ;
            y = ySucc;
            yOriginalColor = y.getColor();

            Node<Entry<K, V>> yRightRaw = y.getRightChild();
            x = (yRightRaw instanceof RBNode) ? (RBNode<Entry<K, V>>) yRightRaw : null;

            if (y.getParent() == z) {
                if (x != null) x.setParent(y);
            } else {
                transplant(y, y.getRightChild());
                y.setRightChild(z.getRightChild());
                if (y.getRightChild() != null) ((BTNode<Entry<K, V>>) y.getRightChild()).setParent(y);
            }

            transplant(z, y);
            y.setLeftChild(z.getLeftChild());
            if (y.getLeftChild() != null) ((BTNode<Entry<K, V>>) y.getLeftChild()).setParent(y);
            y.setColor(z.getColor());
        }

        if (yOriginalColor == Color.BLACK) {
            RBNode<Entry<K, V>> xParent = (x != null && x.getParent() instanceof RBNode)
                    ? (RBNode<Entry<K, V>>) x.getParent()
                    : (root instanceof RBNode ? (RBNode<Entry<K, V>>) ((BTNode<Entry<K, V>>) root) : null);
            deleteFixup(x, xParent);
        }

        if (root != null && root instanceof RBNode) ((RBNode<Entry<K, V>>) root).setColor(Color.BLACK);

        currentSize--;
        return oldValue;
    }

    private void deleteFixup(RBNode<Entry<K, V>> x, RBNode<Entry<K, V>> parent) {
        while ((x == null ? Color.BLACK : x.getColor()) == Color.BLACK && x != root) {
            RBNode<Entry<K, V>> w;
            if (parent != null && parent.getLeftChild() == x) {
                Node<Entry<K, V>> rawW = parent.getRightChild();
                w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;

                if (w != null && w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    parent.setColor(Color.RED);
                    rotateLeft(parent);
                    rawW = parent.getRightChild();
                    w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;
                }

                boolean wLeftBlack = (w == null) || (w.getLeftChild() == null) || !((w.getLeftChild() instanceof RBNode)) || (((RBNode<Entry<K,V>>)w.getLeftChild()).getColor() == Color.BLACK);
                boolean wRightBlack = (w == null) || (w.getRightChild() == null) || !((w.getRightChild() instanceof RBNode)) || (((RBNode<Entry<K,V>>)w.getRightChild()).getColor() == Color.BLACK);

                if (w != null && wLeftBlack && wRightBlack) {
                    if (w != null) w.setColor(Color.RED);
                    x = parent;
                    parent = (x != null && x.getParent() instanceof RBNode) ? (RBNode<Entry<K,V>>) x.getParent() : null;
                } else {
                    if (w != null && wRightBlack) {
                        if (w.getLeftChild() instanceof RBNode) ((RBNode<Entry<K,V>>) w.getLeftChild()).setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        rotateRight(w);
                        rawW = parent.getRightChild();
                        w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;
                    }
                    if (w != null) w.setColor(parent.getColor());
                    parent.setColor(Color.BLACK);
                    if (w != null && w.getRightChild() instanceof RBNode) ((RBNode<Entry<K,V>>) w.getRightChild()).setColor(Color.BLACK);
                    rotateLeft(parent);
                    x = (root instanceof RBNode) ? (RBNode<Entry<K,V>>) root : null;
                    parent = (x != null && x.getParent() instanceof RBNode) ? (RBNode<Entry<K,V>>) x.getParent() : null;
                }
            } else {
                Node<Entry<K, V>> rawW = (parent != null) ? parent.getLeftChild() : null;
                w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;

                if (w != null && w.getColor() == Color.RED) {
                    w.setColor(Color.BLACK);
                    parent.setColor(Color.RED);
                    rotateRight(parent);
                    rawW = parent.getLeftChild();
                    w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;
                }

                boolean wLeftBlack = (w == null) || (w.getLeftChild() == null) || !((w.getLeftChild() instanceof RBNode)) || (((RBNode<Entry<K,V>>)w.getLeftChild()).getColor() == Color.BLACK);
                boolean wRightBlack = (w == null) || (w.getRightChild() == null) || !((w.getRightChild() instanceof RBNode)) || (((RBNode<Entry<K,V>>)w.getRightChild()).getColor() == Color.BLACK);

                if (w != null && wLeftBlack && wRightBlack) {
                    if (w != null) w.setColor(Color.RED);
                    x = parent;
                    parent = (x != null && x.getParent() instanceof RBNode) ? (RBNode<Entry<K,V>>) x.getParent() : null;
                } else {
                    if (w != null && wLeftBlack) {
                        if (w.getRightChild() instanceof RBNode) ((RBNode<Entry<K,V>>) w.getRightChild()).setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        rotateLeft(w);
                        rawW = parent.getLeftChild();
                        w = (rawW instanceof RBNode) ? (RBNode<Entry<K, V>>) rawW : null;
                    }
                    if (w != null) w.setColor(parent.getColor());
                    parent.setColor(Color.BLACK);
                    if (w != null && w.getLeftChild() instanceof RBNode) ((RBNode<Entry<K,V>>) w.getLeftChild()).setColor(Color.BLACK);
                    rotateRight(parent);
                    x = (root instanceof RBNode) ? (RBNode<Entry<K,V>>) root : null;
                    parent = (x != null && x.getParent() instanceof RBNode) ? (RBNode<Entry<K,V>>) x.getParent() : null;
                }
            }
        }
        if (x != null) x.setColor(Color.BLACK);
    }
}
