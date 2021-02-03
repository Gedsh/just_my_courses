package ru.geekbrains.garmatin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LessonSecond {
    private static int i, j = 0;

    public static void main(String[] args) {

        invertArray();

        fillArray();

        changeArray();

        fillDiagonal();

        printMaxMinArray();

        int[] arr1 = {2, 2, 2, 1, 2, 2, 10, 1};
        System.out.println(Arrays.toString(arr1) + " " + checkBalance(arr1));
        int[] arr2 = {1, 1, 1, 2, 1};
        System.out.println(Arrays.toString(arr2) + " " + checkBalance(arr2));
        int[] arr3 = { 1, 2, 3};
        System.out.println(Arrays.toString(arr3) + " " + checkBalance(arr3) + "\n");

        int[] arr4 = {3, 5, 6, 1};
        System.out.println(Arrays.toString(arr4) +"\n" + Arrays.toString(shiftArray(arr4, -2)));
    }

    private static void invertArray() {
        int[] arr = { 1, 0, 1, 0, 0, 1 };

        System.out.println(Arrays.toString(arr));

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1 - arr[i];
        }

        System.out.println(Arrays.toString(arr) + "\n");
    }

    private static void fillArray() {
        int[] arr = new int[8];
        for (int i = 0, j = 0; i < arr.length; i++, j += 3) {
            arr[i] = j;
        }

        System.out.println(Arrays.toString(arr) + "\n");
    }

    private static void changeArray() {
        int[] arr = { 1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1 };

        System.out.println(Arrays.toString(arr));

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 6) {
                arr[i] *= 2;
            }
        }

        System.out.println(Arrays.toString(arr) + "\n");
    }

    private static void fillDiagonal() {
        int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        for (int i = 0, j = arr[i].length - 1; i < arr.length; i++, j--) {
            arr[i][i] = 1;
            arr[i][j] = 1;
        }

        printArray(arr);
        System.out.println();
    }

    private static void printMaxMinArray() {
        int[] arr = { 1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1 };
        System.out.println(Arrays.toString(arr));
        System.out.println("Максимальный элемент " + Arrays.stream(arr).max().getAsInt());
        System.out.println("Минимальный элемент " + Arrays.stream(arr).min().getAsInt() + "\n");
    }

    private static boolean checkBalance(int[] arr) {
        int sumLeft;
        int sumRight;

        for (int i = 1; i < arr.length; i++) {
            sumLeft = Arrays.stream(arr, 0, i).sum();
            sumRight = Arrays.stream(arr, i, arr.length).sum();

            if (sumLeft == sumRight){
                return true;
            }
        }

        return false;
    }

    private static int[] shiftArray(int[] arr, int shift) {
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        Collections.rotate(list, shift);
        return list.stream().mapToInt(i -> i).toArray();
    }

    private static void printArray(int[][] arr) {
        if (i < arr.length) {
            if (j < arr[i].length) {
                System.out.print(arr[i][j] + " ");
                j++;
            } else {
                j = 0;
                i++;
                System.out.println();
            }
            printArray(arr);
        }
        i = 0;
        j = 0;
    }
}
