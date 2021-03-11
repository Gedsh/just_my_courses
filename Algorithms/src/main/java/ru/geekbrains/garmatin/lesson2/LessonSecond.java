package ru.geekbrains.garmatin.lesson2;

import java.util.Arrays;

public class LessonSecond {
    public final static int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);

        MyOwnDataType searchElement = array[array.length * 2 / 3];

        Task task = new Task();

        MyOwnDataType[] arrayCopy = Arrays.copyOf(array, array.length);

        task.useArraysClass(arrayCopy, searchElement);

        task.arraySearch(arrayCopy, searchElement);

        task.useArraysSort(Arrays.copyOf(array, array.length));

        task.useBubbleSort(Arrays.copyOf(array, array.length));

        task.useSelectionSort(Arrays.copyOf(array, array.length));

        task.useInsertionSort(Arrays.copyOf(array, array.length));
    }

}
