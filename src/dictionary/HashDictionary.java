package dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashDictionary<K, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 17; //Primzahl
    private int size;
    private int numberOfEntrys; // load factor = size*1.0/numberOfEntrys
    private LinkedList<Entry<K, V>>[] table; //Generic Array of LL


    //initialize
    @SuppressWarnings("unchecked")
    HashDictionary() {
        size = DEF_CAPACITY;
        numberOfEntrys = 0;
        table = new LinkedList[DEF_CAPACITY];
        for( int i = 0; i < DEF_CAPACITY; ++i) {
            table[i] = new LinkedList<>();
        }
    }

    //find hascode
    private int hash (K key) {
        int adr = key.hashCode();
        if (adr < 0) {
            adr = -adr;
        }
        return adr % size;
    }

    //find Key return Value
    @Override
    public V search(K key) {
        int indexOfKey = hash(key);
        if (table[indexOfKey].isEmpty()) return null;
        for (Entry<K,V> e: table[indexOfKey]) {
            if (key.equals(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }

    //remove key return value
    @Override
    public V remove(K key) {
        int indexToRemove = hash(key);
        if (indexToRemove >= size) return null;
        if (table[indexToRemove].isEmpty()) return null;
        V retVal;
        numberOfEntrys--;
        for (int i = 0; i < table[indexToRemove].size();  ++i) {
            if (key.equals(table[indexToRemove].get(i).getKey())) {
                retVal = table[indexToRemove].get(i).getValue();
                table[indexToRemove].remove(i);
                return retVal;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }
    @SuppressWarnings("unchecked")


    //if it gets too big
    private void ensureCapacityAndReorder(int newSize) {
        if (newSize < size) return;
        LinkedList<Entry<K, V>>[] old = table;
        table = new LinkedList[newSize];
        for (int i = 0; i < newSize; ++i) {
            table[i] = new LinkedList<>();
        }
        // reorder
        numberOfEntrys = 0;
        int oldSize = size;
        size = newSize;
        for (int i = 0; i < oldSize; ++i) {
            for (Entry<K, V> e : old[i]) {
                int newIndex = hash(e.getKey());
                table[newIndex].add(e);
                numberOfEntrys++;
            }
        }
    }

    //test if Primzahl
    private boolean isPrime(int value) {
        if (value <= 2) {
            return (value == 2);
        }
        for (int i = 2; i * i <= value; i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    //If key exists write new value return old
    @Override
    public V insert(K key, V value) {

        int indexToInsert = hash(key);
        // key schon vorhanden->vorhandener value wird überschrieben
        if (search(key) != null) {
            for (int n = 0; n < table[indexToInsert].size(); ++n) {
                if (table[indexToInsert].get(n).getKey().equals(key)) {
                    return table[indexToInsert].get(n).setValue(value);
                }
            }
        }
        if (numberOfEntrys != 0) {
            //check if enough space
            double loadFactor = numberOfEntrys * 1.0 / size;
            if (loadFactor > 3) {
                boolean primeFound = false;
                //find primzahl double the size
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    if (isPrime(2 * size + i)) {
                        ensureCapacityAndReorder(2 * size + i);
                        primeFound = true;
                        break;
                    }
                }
                // bis 2 * size + Integer.MAX_VALUE wurde keine Primzahl gefunden
                if (!primeFound) {
                    ensureCapacityAndReorder(2 * size);
                }
            }
        }
        if (indexToInsert >= size) return null;
        table[indexToInsert].add(new Entry<>(key, value));
        numberOfEntrys++;
        return null;
    }

    @Override
    public Iterator <Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int index = 0;
            private int listIndex = 0;
            @Override
            public boolean hasNext() {
                // mehr als ein Listenelement am aktuellen index
                if (!table[index].isEmpty() && listIndex < table[index].size()) {
                    return true;
                }
                // ueberpruefen ob table bis zum Ende leer ist
                for (int i = index + 1; i < size; ++i) {
                    if (!table[i].isEmpty()) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public Entry<K, V> next() {
                if (!hasNext()) throw new NoSuchElementException();
                // Liste am Index war nicht fertig durchlaufen
                if (listIndex < table[index].size()) {
                    return table[index].get(listIndex++);
                }
                // Liste am Index war leer
                int i;
                for (i = ++index; i < size; ++i) {
                    if (!table[i].isEmpty()) {
                        break;
                    } else {
                        ++index;
                    }
                }
                listIndex = 1;
                return table[i].getFirst();
            }
        };
    }
}
