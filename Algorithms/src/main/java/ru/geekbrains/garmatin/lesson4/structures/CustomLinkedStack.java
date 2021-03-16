package ru.geekbrains.garmatin.lesson4.structures;

import ru.geekbrains.garmatin.lesson4.linked.CustomLinkedListForStack;

public class CustomLinkedStack<E> {
    CustomLinkedListForStack<E> list = new CustomLinkedListForStack<>();

    public void push(E element) {
        list.insert(element);
    }

    public E pop() {
        return list.delete();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
