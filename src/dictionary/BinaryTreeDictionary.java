package dictionary;

import java.util.Iterator;

public class BinaryTreeDictionary <K extends Comparable<K>, V> implements Dictionary<K, V> {


    private static class Node<K ,V> {
        Node<K,V> parent;
        K key;
        V value;
        Node<K,V> left;
        Node<K,V> right;
        private Node(K k, V v){
            key = k;
            value = v;
            left = null;
            right = null;
            parent = null;
        }
    }

    private Node<K,V> root = null;

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if(root != null)
            root.parent = null;
        return oldValue;
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    @Override
    public int size() {
        int size = 0;
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }


    @SuppressWarnings("unchecked")
    private V searchR(K key, Node<K,V> p){ //Help for search
        if (p == null)
            return null;
        else if(key.compareTo(p.key) < 0)
            return searchR(key, p.left);
        else if(key.compareTo(p.key) > 0)
            return searchR(key, p.right);
        else
            return p.value;
    }

    private V oldValue; // Rückgabeparameter

    @SuppressWarnings("unchecked")
    private Node<K,V> insertR(K key, V value, Node<K,V> p) { //Help fo insert
        if (p == null) {
            p = new Node(key, value);
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null)
                p.left.parent = p;
        } else if (key.compareTo(p.key) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null)
                p.right.parent = p;
        } else { // Schlüssel bereits vorhanden:
            oldValue = p.value;
            p.value = value;
        }
        return p;
    }



    //Help for remove
    @SuppressWarnings("unchecked")
    private Node<K,V> removeR(K key, Node<K,V> p) {
        if (p == null) { oldValue = null; }
        else if(key.compareTo(p.key) < 0) {
            p.left = removeR(key, p.left);
            if (p.left != null)
                p.left.parent = p;
        }
        else if (key.compareTo(p.key) > 0) {
            p.right = removeR(key, p.right);
            if (p.right != null)
                p.right.parent = p;
        }
        else if (p.left == null || p.right == null) {
            // p muss gelöscht werden
            // und hat ein oder kein Kind:
            oldValue = p.value;
            if (p.left != null) {
                p.left.parent = p.parent;
                p = p.left;
            } else if (p.right != null) {
                p.right.parent = p.parent;
                p = p.right;
            }
        } else {
            // p muss gelöscht werden und hat zwei Kinder:
            MinEntry<K,V> min = new MinEntry<K,V>();
            p.right = getRemMinR(p.right, min);
            if(p.right != null)
                p.right.parent = p;
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
        }
        return p;
    }


    //Delete smallest Key and return its Value
    private Node<K,V> getRemMinR(Node<K,V> p, MinEntry<K,V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;

        }
        else {
            p.left = getRemMinR(p.left, min);
            if (p.left != null)
                p.left.parent = p;
        }
        return p;
    }
    //Help Datatype
    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }


    public void prettyPrint() {

    }


}
