package ru.geekbrains.garmatin.lesson8.table;

public class DoubleHashingHashTable<E> implements HashTable<E> {
    private final Object[] hashArr;
    private final int arrSize;
    private final Item<E> nonItem;

    public DoubleHashingHashTable(int arrSize) {
        this.arrSize = arrSize;
        this.hashArr = new Object[arrSize];
        nonItem = new Item<E>(null);
    }

    @SuppressWarnings("unchecked")
    public void display() {
        for (int i = 0; i < arrSize; i++) {
            if (hashArr[i] != null) {
                System.out.printf(" %s ;", ((Item<E>)hashArr[i]).getKey());
            } else {
                System.out.print(" *** ;");
            }
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insert(Item<E> item) {
        E key = item.getKey();
        int hash = key.hashCode();
        int hashCompress = hashCompress(hash);
        int stepSize = hashStep(hash);

        while (hashArr[hashCompress] != null && ((Item<E>)hashArr[hashCompress]).getKey() != null) {
            hashCompress += stepSize;
            hashCompress = hashCompress(hashCompress);
        }

        hashArr[hashCompress] = item;
    }

    @SuppressWarnings("unchecked")
    public Item<E> delete(E key) {
        int hash = key.hashCode();
        int hashCompress = hashCompress(hash);
        int stepSize = hashStep(hash);

        while (hashArr[hashCompress] != null) {
            if (((Item<E>)hashArr[hashCompress]).getKey() == key) {
                Item<E> temp = (Item<E>) hashArr[hashCompress];
                hashArr[hashCompress] = nonItem;
                return temp;
            }
            hashCompress += stepSize;
            hashCompress = hashCompress(hashCompress);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Item<E> find(E key) {
        int hash = key.hashCode();
        int hashCompress = hashCompress(hash);
        int stepSize = hashStep(hash);

        while (hashArr[hashCompress] != null) {
            if (((Item<E>)hashArr[hashCompress]).getKey() == key) {
                return (Item<E>) hashArr[hashCompress];
            }
            hashCompress += stepSize;
            hashCompress = hashCompress(hashCompress);
        }

        return null;
    }

    private int hashCompress(int key) {
        return key % arrSize;
    }

    private int hashStep(int key) {
        return 5 - key % 5;
    }
}
