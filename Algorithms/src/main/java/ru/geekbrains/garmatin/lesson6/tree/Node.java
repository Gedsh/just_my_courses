package ru.geekbrains.garmatin.lesson6.tree;

public class Node<T> {
    T data;
    Node<T> leftChild;
    Node<T> rightChild;

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
