package ru.geekbrains.garmatin.lesson2;

import java.util.Arrays;
import java.util.Random;

public class Utils {
    private final static Random random = new Random();

    public static MyOwnDataType[] generateArray(int length) {

        MyOwnDataType[] array = new MyOwnDataType[length];

        for (int i = 0; i < array.length; i++) {
            int rnd = random.nextInt(length);
            array[i] = new MyOwnDataType(rnd);
        }

        //System.out.println(Arrays.toString(array));

        return array;
    }

    public static String generateRandomString(int randomInput) {
        int rnd = randomInput % 53;
        char base = (rnd < 26) ? 'A' : 'a';
        return String.valueOf((char) (base + rnd % 26));
    }

    public static int linearSearch(MyOwnDataType[] array, MyOwnDataType elementToSearch) {

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(elementToSearch)) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(MyOwnDataType[] array, MyOwnDataType elementToSearch) {

        int firstIndex = 0;
        int lastIndex = array.length - 1;

        while (firstIndex <= lastIndex) {
            int middleIndex = (lastIndex + firstIndex) / 2;

            if (array[middleIndex].equals(elementToSearch)) {
                return middleIndex;
            } else if (array[middleIndex].compareTo(elementToSearch) < 0) {
                firstIndex = middleIndex + 1;
            } else if (array[middleIndex].compareTo(elementToSearch) > 0) {
                lastIndex = middleIndex - 1;
            }
        }

        return -1;
    }

    public static void bubbleSort(MyOwnDataType[] array) {
        for (int i = array.length - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    swapArrElements(array, j, j + 1);
                }
            }
        }
        //System.out.println(Arrays.toString(array));
    }

    public static void selectionSort(MyOwnDataType[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;

            for (int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[min]) < 0) {
                    min = j;
                }
            }

            swapArrElements(array, i, min);
        }
        //System.out.println(Arrays.toString(array));
    }

    public static void insertionSort(MyOwnDataType[] array) {
        for (int i = 1; i < array.length; i++) {
            MyOwnDataType buffer = array[i];

            int j = i - 1;
            while (j >= 0 && array[j].compareTo(buffer) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = buffer;
        }
        //System.out.println(Arrays.toString(array));
    }

    private static void swapArrElements(MyOwnDataType[] array, int index1, int index2) {
        MyOwnDataType buffer = array[index1];
        array[index1] = array[index2];
        array[index2] = buffer;
    }

    public static String checkTime(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return System.nanoTime() - start + " nanoseconds";
    }
}
