package ru.geekbrains.garmatin.lesson2;

import java.util.Arrays;

public class Task {
    public void useArraysClass(MyOwnDataType[] array, MyOwnDataType searchElement) {
        System.out.println("Arrays toString: " + Utils.checkTime(() -> Arrays.toString(array)));
        System.out.println("Arrays copy: " + Utils.checkTime(() -> Arrays.copyOf(array, array.length)));
        System.out.println("Arrays sort: " + Utils.checkTime(() -> Arrays.sort(array)));
        System.out.println("Arrays search: " + Utils.checkTime(() ->
        {
            int index = Arrays.binarySearch(array, searchElement);

            if (index < 0) {
                System.out.println("Arrays search did not find element " + searchElement);
            } else {
                System.out.println("Arrays search found element " + array[index] + " index " + index);
            }
        }) + "\n");

    }

    public void arraySearch(MyOwnDataType[] array, MyOwnDataType searchElement) {
        System.out.println("Linear search: " + Utils.checkTime(() ->
        {
            int index = Utils.linearSearch(array, searchElement);

            if (index < 0) {
                System.out.println("Linear search did not find element " + searchElement);
            } else {
                System.out.println("Linear search found element " + array[index] + " index " + index);
            }
        }));

        System.out.println("Binary search: " + Utils.checkTime(() ->
        {
            int index = Utils.binarySearch(array, searchElement);

            if (index < 0) {
                System.out.println("Binary search did not find element " + searchElement);
            } else {
                System.out.println("Binary search found element " + array[index] + " index " + index);
            }
        }) + "\n");
    }

    public void useArraysSort(MyOwnDataType[] array) {
        System.out.println("Arrays sort: " + Utils.checkTime(() ->
                Arrays.sort(array)) + "\n");
    }

    public void useBubbleSort(MyOwnDataType[] array) {
        System.out.println("Bubble sort: " + Utils.checkTime(() ->
                Utils.bubbleSort(array)) + "\n");
    }

    public void useSelectionSort(MyOwnDataType[] array) {
        System.out.println("Selection sort: " + Utils.checkTime(() ->
                Utils.selectionSort(array)) + "\n");
    }

    public void useInsertionSort(MyOwnDataType[] array) {
        System.out.println("Insertion sort: " + Utils.checkTime(() ->
                Utils.insertionSort(array)) + "\n");
    }
}
