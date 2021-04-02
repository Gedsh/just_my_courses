package ru.geekbrains.garmatin.lesson8;

import ru.geekbrains.garmatin.lesson8.table.HashTable;
import ru.geekbrains.garmatin.lesson8.table.Item;

import java.util.Random;

public class Utils {
    private final static Random random = new Random();

    public static MyOwnDataType[] generateArray(int length) {

        MyOwnDataType[] array = new MyOwnDataType[length];

        for (int i = 0; i < array.length; i++) {
            int rnd = random.nextInt(length);
            array[i] = new MyOwnDataType(rnd);
        }

        return array;
    }

    public static String generateRandomString(int randomInput) {
        int rnd = randomInput % 53;
        char base = (rnd < 26) ? 'A' : 'a';
        return String.valueOf((char) (base + rnd % 26));
    }

    public static void fillHashTable(MyOwnDataType[] array, HashTable<MyOwnDataType> hashTable) {
        for (MyOwnDataType element: array) {
            hashTable.insert(new Item<>(element));
        }
    }

}
