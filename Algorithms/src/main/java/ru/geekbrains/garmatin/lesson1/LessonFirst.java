package ru.geekbrains.garmatin.lesson1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LessonFirst {

    public static void main(String[] args) {
        algorithmsInLife();

        System.out.println();

        algorithmsInJava();

        System.out.println();

        primitiveAndReferenceVars();

        System.out.println();

        compareVars();

        System.out.println();
    }

    private static void algorithmsInLife() {
        System.out.println("\tАлгоритмы в жизни - любые действия, нуждающиеся в определенной последовательности." +
                " Например, надо сначала залить воду в чайник, а только потом его включить," +
                " иначе кофе не выйдет :)");
        System.out.println("\tСтруктуры данных - улицы, ячейки камеры хранения и т.д.");
    }

    private static void algorithmsInJava() {
        System.out.println("\tАлгоритмы в java - поиск минимального и максимального значений," +
                " сортировка, балансировка деревьев, нахождения кратчайшего пути в графах.");
        System.out.println("\tСтруктуры данных - массив, список, очередь, стек, дерево, граф.");
    }

    private static void primitiveAndReferenceVars() {
        MyOwnDataType myOwnDataType = new MyOwnDataType(10, "Hello", null);
        myOwnDataType.showData(myOwnDataType.getPrimitive());
        myOwnDataType.showData(myOwnDataType.getReference());
        myOwnDataType.showData(myOwnDataType);
    }

    private static void compareVars() {
        List<MyOwnDataType> list = new ArrayList<MyOwnDataType>() {{
            add(new MyOwnDataType(1, "q", null));
            add(new MyOwnDataType(2, "w", null));
            add(new MyOwnDataType(3, "e", null));
            add(new MyOwnDataType(4, "r", null));
            add(new MyOwnDataType(5, "t", null));
            add(new MyOwnDataType(6, "y", null));
            add(new MyOwnDataType(7, "u", null));
            add(new MyOwnDataType(8, "i", null));
            add(new MyOwnDataType(9, "o", null));

        }};

        MyOwnDataType toFind = new MyOwnDataType(9, "o", null);

        long startTime = System.nanoTime();

        for (MyOwnDataType myOwnDataType: list) {
            if (myOwnDataType.equals(toFind)) {
                System.out.println(myOwnDataType);
            }
        }

        System.out.printf("Врямя поиска %d наносекунд.", System.nanoTime() - startTime);

    }
}

class MyOwnDataType {
    private final int primitive;
    private final String reference;
    private final MyOwnDataType myOwnDataType;

    public MyOwnDataType(int primitive, String reference, MyOwnDataType myOwnDataType) {
        this.primitive = primitive;
        this.reference = reference;
        this.myOwnDataType = myOwnDataType;
    }

    public void showData(Object anyData) {
        System.out.println(anyData);
    }

    public int getPrimitive() {
        return primitive;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "MyOwnDataType{" +
                "primitive=" + primitive +
                ", reference='" + reference + '\'' +
                ", myOwnDataType=" + myOwnDataType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyOwnDataType that = (MyOwnDataType) o;
        return primitive == that.primitive && Objects.equals(reference, that.reference) && Objects.equals(myOwnDataType, that.myOwnDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primitive, reference, myOwnDataType);
    }
}
