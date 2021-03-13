package ru.geekbrains.garmatin.lesson3;

import java.util.Objects;

public class MyOwnDataType implements Comparable<MyOwnDataType> {
    private final int primitive;
    private final String reference;

    public MyOwnDataType(int primitive) {
        this.primitive = primitive;
        this.reference = Utils.generateRandomString(primitive);
    }

    @Override
    public String toString() {
        return "MyOwnDataType{" +
                "primitive=" + primitive +
                ", reference='" + reference + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyOwnDataType that = (MyOwnDataType) o;
        return primitive == that.primitive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(primitive);
    }

    @Override
    public int compareTo(MyOwnDataType o) {
        return primitive - o.primitive;
    }
}
