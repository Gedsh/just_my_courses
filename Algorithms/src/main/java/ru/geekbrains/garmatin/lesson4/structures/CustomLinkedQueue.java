package ru.geekbrains.garmatin.lesson4.structures;

import ru.geekbrains.garmatin.lesson4.linked.CustomLinkedListForQueue;

public class CustomLinkedQueue<E> {
    CustomLinkedListForQueue<E> list = new CustomLinkedListForQueue<>();

    public void insert(E element) {
        list.insert(element);
    }

    public E delete() {
        return list.delete();
    }

    public void display() {
        list.display();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
