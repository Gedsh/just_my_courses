package ru.geekbrains.garmatin.lesson5;

import java.util.Random;
import java.util.Stack;

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

    public static int sumRange(int range) {
        if (range > 0) {
            return range + sumRange(range - 1);
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    public static int almostEndlessCountdown(int start) {
        return almostEndlessCountdown(start - 1);
    }

    public static int finiteCountdown(int start) {
        if (start == 1) {
            return 1;
        }

        return finiteCountdown(start - 1);
    }

    public static String showStack() {
        Stack<String> stack = new Stack<>();
        stack.add("showStack()");
        stack.add("A(x)");
        stack.add("B(x)");

        return String.format("\n %s\n %s\n %s\n", stack.pop(), stack.pop(), stack.pop());
    }

    public static String showRecursiveStack() {
        Stack<String> stack = new Stack<>();
        stack.add("showRecursiveStack()");

        int result = factorial(5, stack);

        StringBuilder stringBuilder = new StringBuilder();
        while (!stack.isEmpty()) {
            stringBuilder.append("\n").append(stack.pop());
        }

        stringBuilder.append("\n").append("Result: ").append(result).append("\n");

        return stringBuilder.toString();
    }

    private static int factorial(int value, Stack<String> stack) {
        stack.add(String.format("factorial(%d)", value));

        if (value == 1) {
            return 1;
        }
        return value * factorial(value - 1, stack);
    }

    public static int sumBoundedRangeWithRecursion(int start, int end) {
        if (end > start) {
            return end + sumBoundedRangeWithRecursion(start, end - 1);
        } else {
            return end;
        }
    }

    public static int sumBoundedRangeWithLoop(int start, int end) {
        int sum = 0;

        for (int i = start; i <= end; i++) {
            sum += i;
        }

        return sum;
    }

    public static int recursiveBinarySearch(MyOwnDataType[] array, MyOwnDataType elementToSearch) {
        return recursiveBinarySearchHelper(array, elementToSearch, 0, array.length - 1);
    }

    private static int recursiveBinarySearchHelper(MyOwnDataType[] array,
                                                   MyOwnDataType elementToSearch,
                                                   int firstIndex,
                                                   int lastIndex) {
        if (firstIndex > lastIndex) {
            return -1;
        }

        int middleIndex = (lastIndex + firstIndex) / 2;

        if (array[middleIndex].equals(elementToSearch)) {
            return middleIndex;
        } else if (array[middleIndex].compareTo(elementToSearch) < 0) {
            return recursiveBinarySearchHelper(array, elementToSearch, middleIndex + 1, lastIndex);
        } else if (array[middleIndex].compareTo(elementToSearch) > 0) {
            return recursiveBinarySearchHelper(array, elementToSearch, firstIndex, middleIndex - 1);
        }

        return -1;
    }

    public static int loopBinarySearch(MyOwnDataType[] array, MyOwnDataType elementToSearch) {

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

    public static void recursiveMergeSort(MyOwnDataType[] array) {
        int n = array.length;
        MyOwnDataType[] temp = new MyOwnDataType[n];
        mergeSortHelper(array, 0, n - 1, temp);
    }

    private static void mergeSortHelper(MyOwnDataType[] elements,
                                        int firstIndex, int lastIndex, MyOwnDataType[] temp) {
        if (firstIndex < lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            mergeSortHelper(elements, firstIndex, middleIndex, temp);
            mergeSortHelper(elements, middleIndex + 1, lastIndex, temp);
            merge(elements, firstIndex, middleIndex, lastIndex, temp);
        }
    }

    private static void merge(MyOwnDataType[] elements, int firstIndex,
                              int middleIndex, int lastIndex, MyOwnDataType[] temp) {
        int i = firstIndex;
        int j = middleIndex + 1;
        int k = firstIndex;

        while (i <= middleIndex && j <= lastIndex) {
            if (elements[i].compareTo(elements[j]) < 0) {
                temp[k] = elements[i];
                i++;
            } else {
                temp[k] = elements[j];
                j++;
            }
            k++;
        }

        while (i <= middleIndex) {
            temp[k] = elements[i];
            i++;
            k++;
        }

        while (j <= lastIndex) {
            temp[k] = elements[j];
            j++;
            k++;
        }

        for (k = firstIndex; k <= lastIndex; k++) {
            elements[k] = temp[k];
        }
    }
}
