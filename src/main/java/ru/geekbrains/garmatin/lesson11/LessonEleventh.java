package ru.geekbrains.garmatin.lesson11;

import java.util.ArrayList;
import java.util.Arrays;

public class LessonEleventh {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(exchangeArrElements(new Integer[]{1, 2, 3, 4, 5}, 0, 4)));
        System.out.println();

        System.out.println(arrayToList(new Integer[]{1, 2, 3, 4, 5}));
        System.out.println();

        Box<Apple> appleBox1 = new Box<>(15);
        Box<Orange> orangeBox1 = new Box<>(20);
        Box<Apple> appleBox2 = new Box<>(5);
        Box<Orange> orangeBox2 = new Box<>(10);

        for (int i = 0; i < 15; i++) {
            appleBox1.addFruit(new Apple());
        }
        System.out.println("Вес первой коробки яблок " + appleBox1.getWeigh());

        for (int i = 0; i < 10; i++) {
            orangeBox1.addFruit(new Orange());
        }
        System.out.println("Вес первой коробки апельсин " + orangeBox1.getWeigh());

        System.out.println("Вес коробок " + (appleBox1.compare(orangeBox1) ? "одинаковый" : "разный"));

        appleBox1.emptyBox(appleBox2);
        System.out.println("Вес первой коробки яблок " + appleBox1.getWeigh());
        System.out.println("Вес второй коробки яблок " + appleBox2.getWeigh());

        orangeBox1.emptyBox(orangeBox2);
        System.out.println("Вес первой коробки апельсин " + orangeBox1.getWeigh());
        System.out.println("Вес второй коробки апельсин " + orangeBox2.getWeigh());
    }

    private static <T> T[] exchangeArrElements(T[] arr, int elementIndex1, int elementIndex2) {
        if (elementIndex1 < 0 || elementIndex1 >= arr.length
                || elementIndex2 < 0 || elementIndex2 >= arr.length) {
            System.err.println("Impossible to exchange!");
            return arr;
        }

        T tmp = arr[elementIndex1];
        arr[elementIndex1] = arr[elementIndex2];
        arr[elementIndex2] = tmp;
        return arr;
    }

    private static <T> ArrayList<T> arrayToList(T[] arr) {
        return Arrays.stream(arr).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
