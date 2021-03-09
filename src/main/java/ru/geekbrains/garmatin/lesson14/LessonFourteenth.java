package ru.geekbrains.garmatin.lesson14;

public class LessonFourteenth {
    public static void main(String[] args) {

    }

    public int[] getElementsAfterLastFour(int[] arr) {

        int lastFourIndex = -1;

        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                lastFourIndex = i;
                break;
            }
        }

        if (lastFourIndex < 0) {
            throw new RuntimeException("Входной массив должен быть хотя-бы с одной четверкой!");
        }

        int[] outputArr = new int[arr.length - lastFourIndex - 1];
        System.arraycopy(arr, lastFourIndex + 1, outputArr, 0, outputArr.length);

        return outputArr;
    }

    public boolean isOneOrFourInArr(int[] arr) {
        boolean oneFound = false;
        boolean fourFound = false;

        for (int element : arr) {
            if (element == 1) {
                oneFound = true;
            } else if (element == 4) {
                fourFound = true;
            } else {
                return false;
            }
        }

        return oneFound && fourFound;
    }
}
