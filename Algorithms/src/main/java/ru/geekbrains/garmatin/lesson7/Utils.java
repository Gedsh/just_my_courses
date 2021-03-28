package ru.geekbrains.garmatin.lesson7;

import ru.geekbrains.garmatin.lesson7.graph.Graph;

import java.util.Random;

public class Utils {
    private final static Random random = new Random();

    public static MyOwnDataType[] generateArray(int length) {

        MyOwnDataType[] array = new MyOwnDataType[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = new MyOwnDataType(i);
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

    public static void arrayToGraph(MyOwnDataType[] array, Graph<MyOwnDataType> graph) {
        for (int i = 0; i < array.length; i++) {
            graph.addVertex(array[i]);
            if (i == 1) {
                graph.addEdge(i, 0);
            } else if (i != 0) {
                int edge = random.nextInt(i - 1);
                graph.addEdge(i, edge);
            }
        }
    }

}
