package ru.geekbrains.garmatin.lesson11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Box<T extends Fruit> {
    private static final float ACCURACY = 0.001f;

    List<T> fruits = new ArrayList<>();

    private final int capacity;

    public Box(int capacity) {
        this.capacity = capacity;
    }

    public boolean addFruit(T fruit) {
        if (fruits.size() == capacity) {
            return false;
        } else {
            return fruits.add(fruit);
        }
    }

    public float getWeigh() {
        float weigh = .0f;
        for (T fruit: fruits) {
            weigh += fruit.getWeigh();
        }
        return weigh;
    }

    public boolean compare(Box<?> another) {
        if (another == null) {
            return false;
        }

        return Math.abs(this.getWeigh() - another.getWeigh()) < ACCURACY;
    }

    public void emptyBox(Box<T> another) {
        if (another == null) {
            return;
        }

        Iterator<T> iterator = fruits.listIterator();
        while (iterator.hasNext()) {
            if (another.addFruit(iterator.next())) {
                iterator.remove();
            } else {
                break;
            }
        }
    }
}
