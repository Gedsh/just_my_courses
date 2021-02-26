package ru.geekbrains.garmatin.lesson9;

public class LessonNinth {
    private static final int REQUIRED_ARR_SIZE = 4;

    public static void main(String[] args) {
        String[][] arr1 = {
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15"}
        };

        String[][] arr2 = {
                {"1", "2", "3"},
                {"3", "4", "5"},
                {"6", "7", "8"}
        };

        String[][] arr3 = {
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10a", "11", "12"},
                {"13", "14", "15", "16"}
        };

        String[][] arr4 = {
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"}
        };

        try {
            int summ1 = strArrayToIntSumm(arr1);
            System.out.println("Сумма: " + summ1);
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.err.println(e.getMessage());
        }

        try {
            int summ2 = strArrayToIntSumm(arr2);
            System.out.println("Сумма: " + summ2);
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.err.println(e.getMessage());
        }

        try {
            int summ3 = strArrayToIntSumm(arr3);
            System.out.println("Сумма: " + summ3);
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.err.println(e.getMessage());
        }

        try {
            int summ4 = strArrayToIntSumm(arr4);
            System.out.println("Сумма: " + summ4);
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.err.println(e.getMessage());
        }
    }

    private static int strArrayToIntSumm(String[][] strArray) {
        if (isArrayNotSquare(strArray)
                || strArray.length != REQUIRED_ARR_SIZE
                || strArray[0].length != REQUIRED_ARR_SIZE) {
            throw new MyArraySizeException();
        }

        int summ = 0;

        for (int i = 0; i < strArray.length; i++) {
            for (int j = 0; j < strArray[0].length; j++) {
                String element = strArray[i][j];
                try {
                    summ += Integer.parseInt(element);
                } catch (NumberFormatException ignored) {
                    throw new MyArrayDataException(element, i, j);
                }
            }
        }

        return summ;
    }

    private static boolean isArrayNotSquare(String[][] arr) {
        int length = arr.length;
        for (String[] row : arr) {
            if (row.length != length) {
                return true;
            }
        }
        return false;
    }
}
