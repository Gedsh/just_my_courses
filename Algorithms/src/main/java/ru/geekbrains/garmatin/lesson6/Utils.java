package ru.geekbrains.garmatin.lesson6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static List<Class> getSuperClasses(Object o) {
        List<Class> classList = new ArrayList<Class>();
        Class clazz = o.getClass();
        Class superclass = clazz.getSuperclass();
        classList.add(clazz);
        while (superclass != null) {
            clazz = superclass;
            superclass = clazz.getSuperclass();
            classList.add(clazz);
        }
        return classList;
    }
}
