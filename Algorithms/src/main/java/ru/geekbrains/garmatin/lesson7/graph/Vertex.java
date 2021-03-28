package ru.geekbrains.garmatin.lesson7.graph;

public class Vertex<T> {
    T data;
    boolean wasVisited;

    public Vertex(T data) {
        this.data = data;
    }
}
