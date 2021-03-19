package ru.geekbrains.garmatin.lesson5;

import java.util.Arrays;

public class Task {

    public void recursiveExample() {
        System.out.printf("Recursive example: %d\n\n", Utils.sumRange(10));
    }

    public void showEndlessAndFiniteRecursion() {
        System.out.println("Almost endless countdown: uncomment corresponding line in Task.java");
        //System.out.printf("Almost endless countdown: %s\n", Utils.almostEndlessCountdown(100));
        System.out.printf("Finite countdown: %s\n\n", Utils.finiteCountdown(100));
    }

    public void showStackExample() {
        System.out.printf("Stack example: %s", Utils.showStack());
        System.out.printf("Recursive Stack example: %s\n", Utils.showRecursiveStack());
    }

    public void recursionAndLoopExample() {
        System.out.println("Recursion example: " + Utils.checkTime(() ->
                System.out.printf("Result: %d\n", Utils.sumBoundedRangeWithRecursion(5, 10))));
        System.out.println("Loop example: " + Utils.checkTime(() ->
                System.out.printf("Result: %d\n", Utils.sumBoundedRangeWithLoop(5, 10))) + "\n");
    }

    public void arraySearch(MyOwnDataType[] array, MyOwnDataType searchElement) {

        Arrays.sort(array);

        System.out.println("Recursive binary search: " + Utils.checkTime(() ->
        {
            int index = Utils.recursiveBinarySearch(array, searchElement);

            if (index < 0) {
                System.out.println("Recursive binary search did not find element " + searchElement);
            } else {
                System.out.println("Recursive binary search found element " + array[index] + " index " + index);
            }
        }));

        System.out.println("Loop binary search: " + Utils.checkTime(() ->
        {
            int index = Utils.loopBinarySearch(array, searchElement);

            if (index < 0) {
                System.out.println("Loop binary search did not find element " + searchElement);
            } else {
                System.out.println("Loop binary search found element " + array[index] + " index " + index);
            }
        }) + "\n");
    }

    public void arraySort(MyOwnDataType[] array) {
        MyOwnDataType[] copy = Arrays.copyOf(array, array.length);
        System.out.println("Merge sort: " + Utils.checkTime(() ->
                Utils.recursiveMergeSort(copy)));
        System.out.println(Arrays.toString(copy));

        System.out.println("Tim sort: " + Utils.checkTime(() ->
                Arrays.sort(array)));
        System.out.println(Arrays.toString(array));
    }

}
