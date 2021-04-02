package ru.geekbrains.garmatin.lesson8;

import ru.geekbrains.garmatin.lesson8.table.DoubleHashingHashTable;
import ru.geekbrains.garmatin.lesson8.table.Item;
import ru.geekbrains.garmatin.lesson8.table.LinearProbingHashTable;

public class Task {

    public void hashExample() {
        System.out.println("\nHashes are used to store sensitive information such as username and password" +
                " to ensure that data cannot be recovered from the hash.\n");
    }

    public void hashCollision() {
        System.out.println("Collision is a situation that occurs when two distinct pieces of data (keys) have the same hash value.\n");
    }

    public void hashFunctions() {
        System.out.println("Hash functions: (x>>15)^x, key%size\n");
    }

    public void useLinearProbingHashTable(LinearProbingHashTable<MyOwnDataType> hashTable, MyOwnDataType element) {
        System.out.println("Source linear probing hash table:");
        hashTable.display();

        System.out.println("Insert to linear probing hash table:");
        hashTable.insert(new Item<>(element));
        hashTable.display();

        System.out.printf("Find item: %s\n", hashTable.find(element));

        System.out.println("Delete item from linear probing hash table:");
        hashTable.delete(element);
        hashTable.display();

        System.out.println();
    }

    public void useDoubleHashingHashTable(DoubleHashingHashTable<MyOwnDataType> hashTable, MyOwnDataType element) {
        System.out.println("Source double hashing hash table:");
        hashTable.display();

        System.out.println("Insert to double hashing hash table:");
        hashTable.insert(new Item<>(element));
        hashTable.display();

        System.out.printf("Find item: %s\n", hashTable.find(element));

        System.out.println("Delete item from double hashing hash table:");
        hashTable.delete(element);
        hashTable.display();
    }
}
