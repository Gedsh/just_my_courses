package ru.geekbrains.garmatin.lesson8.table;

public class Item<T> {
    private final T data;

    public Item(T data) {
        this.data = data;
    }

    public T getKey() {
        return data;
    }

    @Override
    public String toString() {
        return "Item{" +
                "data=" + data +
                '}';
    }
}
