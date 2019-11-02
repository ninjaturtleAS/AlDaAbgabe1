package dictionary;


import java.util.Iterator;

public class BinaryTreeDictionary <K extends Comparable<K>, V> implements Dictionary<K, V> {


    private static class Node<K ,V> {
        Node<K,V> parent;
        K key;
        V value;
        Node<K,V> left;
        Node<K,V> right;
        int height;
        private Node(K k, V v){
            key = k;
            value = v;
            left = null;
            right = null;
            parent = null;
            height = 0;
        }
    }

    private int getHeight(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }

    private Node<K,V> root = null;
    private int size = 0;

    @Override
    public int size() {
        return this.size;
    }

    private V oldValue; // Rückgabeparameter

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        size += 1;
        if(root != null)
            root.parent = null;
        return oldValue;
    }

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
        p = balance(p);
        return p;
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

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


    @Override
    public V remove(K key) {
        root = removeR(key, root);
        this.size -= 1;
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    //Help for remove
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
                p = p.left;
            } else{
                p = p.right;
            }
        } else {
            // p muss gelöscht werden und hat zwei Kinder:
            MinEntry<K,V> min = new MinEntry<>();
            p.right = getRemMinR(p.right, min);
            if(p.right != null)
                p.right.parent = p;
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
        }
        p = balance(p);
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
        p = balance(p);
        return p;
    }


    //Help Datatype
    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }


    void prettyPrint() {
        prettyPrintR(0, root);
    }

    private void prettyPrintR(int d, Node p) {
        if (p == null)
            return;
        for (int i = 0; i < d; i++) {
            System.out.print("    ");
        }
        System.out.println(p.key + ": " + p.value);
        prettyPrintR(d + 1, p.right);
        prettyPrintR(d + 1, p.left);
    }


    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else
                p = rotateLeftRight(p);
        }
        else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else
                p = rotateRightLeft(p);
        }
        return p;
    }


    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;
        q.right = p;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }


    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        p.right = q.left;
        q.left = p;
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) + 1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) + 1;
        return q;
    }
    private Node<K,V> rotateLeftRight(Node<K,V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }
    private Node<K,V> rotateRightLeft(Node<K,V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            Node<K, V> n = null;

            @Override
            public boolean hasNext() {
                if (root == null)
                    return false;
                else if (n == null)
                    return true;
                else if (n.right != null) {
                    return true;
                } else
                    return parentOfLeftMostAncestor(n) != null;
            }

            @Override
            public Entry<K, V> next() {
                if (n == null)
                    n = leftMostDescendant(root);
                if (n.right != null)
                    n = leftMostDescendant(n.right);
                else {
                    n = parentOfLeftMostAncestor(n);
                }

                if (n == null)
                    n = root;
                return new Entry<>(n.key, n.value);
            }
        };
    }

    private Node<K,V> leftMostDescendant(Node<K,V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K,V> parentOfLeftMostAncestor(Node<K,V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent; // kann auch null sein
    }


}
