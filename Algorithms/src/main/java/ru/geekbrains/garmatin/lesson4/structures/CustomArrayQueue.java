package ru.geekbrains.garmatin.lesson4.structures;

public class CustomArrayQueue<E> {
    private final int maxSize;
    private final Object[] queue;
    private int front;
    private int rear = -1;
    private int size;

    public CustomArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new Object[maxSize];
    }

    public void insert(E element) {
        if (rear == maxSize - 1) {
            rear = -1;
        }
        queue[++rear] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E remove() {
        E element = (E) queue[front++];

        if (front == maxSize) {
            front = 0;
        }
        size --;

        return element;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        return (E) queue[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == maxSize;
    }

    public int getSize() {
        return size;
    }
}
