package ru.geekbrains.garmatin.lesson3;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public static String checkTime(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return System.nanoTime() - start + " nanoseconds";
    }

    public static void arrayToArrayList(MyOwnDataType[] array, ArrayList<MyOwnDataType> arrayList) {
        for (MyOwnDataType element: array) {
            arrayList.add(element);
        }
    }

    public static void arrayToLinkedList(MyOwnDataType[] array, LinkedList<MyOwnDataType> linkedList) {
        for (MyOwnDataType element: array) {
            linkedList.add(element);
        }
    }
}
