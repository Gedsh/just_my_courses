package ru.geekbrains.garmatin.lesson4;

import ru.geekbrains.garmatin.lesson4.structures.CustomArrayQueue;
import ru.geekbrains.garmatin.lesson4.structures.CustomArrayStack;
import ru.geekbrains.garmatin.lesson4.structures.CustomLinkedQueue;
import ru.geekbrains.garmatin.lesson4.structures.CustomLinkedStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

public class LessonFourth {
    private static final int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);

        Task task = new Task();

        CustomArrayStack<MyOwnDataType> arrayStack = new CustomArrayStack<>(array.length);
        task.useCustomArrayStack(array, arrayStack);

        CustomArrayQueue<MyOwnDataType> arrayQueue = new CustomArrayQueue<>(array.length);
        task.useCustomArrayQueue(array, arrayQueue);

        Deque<MyOwnDataType> arrayDeque = new ArrayDeque<>();
        task.useArrayDeque(array, arrayDeque);

        Queue<MyOwnDataType> priorityQueue = new PriorityQueue<>();
        task.usePriorityQueue(array, priorityQueue);

        CustomLinkedStack<MyOwnDataType> linkedStack = new CustomLinkedStack<>();
        task.useCustomLinkedStack(array, linkedStack);

        CustomLinkedQueue<MyOwnDataType> linkedQueue = new CustomLinkedQueue<>();
        task.useCustomLinkedQueue(array, linkedQueue);
    }

}
