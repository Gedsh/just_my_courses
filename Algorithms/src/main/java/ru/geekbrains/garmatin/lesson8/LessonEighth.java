package ru.geekbrains.garmatin.lesson8;

import ru.geekbrains.garmatin.lesson8.table.DoubleHashingHashTable;
import ru.geekbrains.garmatin.lesson8.table.LinearProbingHashTable;

public class LessonEighth {
    private static final int ARRAY_LENGTH = 15;
    private static final int HASH_TABLE_SIZE = 66;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);
        MyOwnDataType element = new MyOwnDataType(ARRAY_LENGTH);

        LinearProbingHashTable<MyOwnDataType> linearProbingHashTable = new LinearProbingHashTable<>(HASH_TABLE_SIZE);
        Utils.fillHashTable(array, linearProbingHashTable);

        DoubleHashingHashTable<MyOwnDataType> doubleHashingHashTable = new DoubleHashingHashTable<>(HASH_TABLE_SIZE);
        Utils.fillHashTable(array, doubleHashingHashTable);

        Task task = new Task();

        task.hashExample();

        task.hashCollision();

        task.hashFunctions();

        task.useLinearProbingHashTable(linearProbingHashTable, element);

        task.useDoubleHashingHashTable(doubleHashingHashTable, element);
    }
}
