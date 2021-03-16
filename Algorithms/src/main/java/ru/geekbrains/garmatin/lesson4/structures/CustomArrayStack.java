package ru.geekbrains.garmatin.lesson4.structures;

public class CustomArrayStack<E> {
    private final Object[] stack;
    private int top = -1;

    public CustomArrayStack(int maxSize) {
        this.stack = new Object[maxSize];
    }

    public void push(E element) {
        stack[++top] = element;
    }

    @SuppressWarnings("unchecked")
    public E pop() {
        return (E) stack[top--];
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        return (E) stack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}
