package dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements dictionary.Dictionary<K,V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K,V>[] data;

    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    private int searchKey(K key) {
        int left = 0;
        int right = size - 1;
        while (right >= left) {
            int m = (left + right) / 2;
            if (key.compareTo(data[m].getKey()) < 0) {
                right = m-1;
            } else if (key.compareTo(data[m].getKey()) > 0) {
                left = m+1;
            } else {
                return m; //found key
            }
        }
        return -1; //key not found
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            return data[i].getValue();
        } else {
            return null;
        }
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        V r = data[i].getValue();
        for (int j = i; j < size-1; j++) {
            data[j] = data[j+1];
        }
        data[--size] = null;
        return r;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int newCapacity) {
        if (newCapacity < size) return;
        Entry[] old = data;
        data = new Entry[newCapacity];
        System.arraycopy(old, 0, data, 0, size);
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);// Vorhandener Eintrag wird überschrieben:
        if (i != -1) {
            //V r = data[i].getValue();
            return data[i].setValue(value);
        }// Neueintrag:
        if (data.length == size) {
            ensureCapacity(2 * size);
        }
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = new Entry<>(key, value);
        size++;
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Entry<K, V> next() {
                if (! hasNext()) throw new NoSuchElementException();
                return data[index++];
            }
        };
    }

}
