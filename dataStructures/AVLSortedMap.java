package dataStructures;
/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{


    @Override
    protected BTNode<Entry<K,V>> createNode(Entry<K,V> entry, BTNode<Entry<K,V>> parent) {
        AVLNode<Entry<K,V>> p = (parent == null) ? null : (AVLNode<Entry<K,V>>) parent;
        return new AVLNode<>(entry, p);
    }

    @Override
    public V put(K key, V value) {
        V old = super.put(key, value); // uses createNode -> AVLNode

        // locate the node we just inserted/updated (safe traversal from root)
        AVLNode<Entry<K,V>> node = locateNode((BTNode<Entry<K,V>>) root, key);
        if (node != null) rebalanceUp(node);
        return old;
    }

    @Override
    public V remove(K key) {
        // locate node before removal so we can have the parent to start rebalancing
        AVLNode<Entry<K,V>> node = locateNode((BTNode<Entry<K,V>>) root, key);
        if (node == null) return null;
        AVLNode<Entry<K,V>> parent = (AVLNode<Entry<K,V>>) node.getParent();

        V old = super.remove(key); // structural removal implemented in BSTSortedMap

        // rebalance from parent (or root)
        if (parent != null) rebalanceUp(parent);
        else if (root != null) rebalanceUp((AVLNode<Entry<K,V>>) root);

        return old;
    }

    /**
     * Locate node by key starting at startNode (BTNode) and return it as AVLNode (or null).
     * We use a BTNode parameter to avoid casting problems if root isn't already AVLNode.
     */
    private AVLNode<Entry<K,V>> locateNode(BTNode<Entry<K,V>> startNode, K key) {
        BTNode<Entry<K,V>> current = startNode;
        while (current != null) {
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) return (AVLNode<Entry<K,V>>) current;
            current = (cmp < 0) ? (BTNode<Entry<K,V>>) current.getLeftChild()
                    : (BTNode<Entry<K,V>>) current.getRightChild();
        }
        return null;
    }

    private AVLNode<Entry<K, V>> tallerChild(AVLNode<Entry<K, V>> n) {
        AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) n.getLeftChild();
        AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) n.getRightChild();
        if (left.getHeight() >= right.getHeight()) return left;
        else return right;
    }

    /**
     * Walk up from node, update heights, and when |balance|>1 perform tri-node restructure.
     */
    private void rebalanceUp(AVLNode<Entry<K,V>> node) {
        AVLNode<Entry<K, V>> cur = node;
        while (cur != null) {
            // recompute height for cur (children might have changed)
            cur.updateHeight();
            int bf = cur.getBalanceFactor();
            if (Math.abs(bf) > 1) {
                // find y = taller child of cur (z), x = taller child of y
                AVLNode<Entry<K, V>> y = tallerChild(cur);
                AVLNode<Entry<K, V>> x = (y == null) ? null : tallerChild(y);

                // restructure requires x to be a BTNode; in degenerate cases x could be null
                // but for a valid AVL imbalance x should not be null. Still, guard anyway.
                BTNode<Entry<K, V>> newRoot;
                if (x != null) {
                    newRoot = restructure((BTNode<Entry<K, V>>) x);
                } else if (y != null) {
                    // fallback: if x is null (very rare), call restructure on y to do single rotation
                    newRoot = restructure((BTNode<Entry<K, V>>) y);
                } else {
                    // nothing to do (shouldn't happen), move up
                    cur = (AVLNode<Entry<K, V>>) cur.getParent();
                    continue;
                }

                // update heights in the rotated subtree: children then root
                AVLNode<Entry<K, V>> newRootAVL = (AVLNode<Entry<K, V>>) newRoot;
                AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) newRootAVL.getLeftChild();
                AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) newRootAVL.getRightChild();
                if (left != null) left.updateHeight();
                if (right != null) right.updateHeight();
                newRootAVL.updateHeight();

                // continue from parent of the subtree root (could be null -> done)
                cur = (AVLNode<Entry<K, V>>) newRootAVL.getParent();
            } else {
                // no imbalance here: move to parent
                cur = (AVLNode<Entry<K, V>>) cur.getParent();
            }
        }
    }
}