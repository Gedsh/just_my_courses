package ru.geekbrains.garmatin.lesson4;

import ru.geekbrains.garmatin.lesson4.structures.CustomArrayQueue;
import ru.geekbrains.garmatin.lesson4.structures.CustomArrayStack;
import ru.geekbrains.garmatin.lesson4.structures.CustomLinkedQueue;
import ru.geekbrains.garmatin.lesson4.structures.CustomLinkedStack;

import java.util.Deque;
import java.util.Queue;

public class Task {

    public void useCustomArrayStack(MyOwnDataType[] array,
                                    CustomArrayStack<MyOwnDataType> customArrayStack) {
        System.out.println("Push to stack and peek: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customArrayStack.push(element);
                System.out.format("%s ", customArrayStack.peek());
            }
            System.out.println();
        }));

        System.out.println("Pop from stack: " + Utils.checkTime(() ->
        {
            while (!customArrayStack.isEmpty()) {
                customArrayStack.pop();
            }
            System.out.println("Custom stack based on array is empty");
        }) + "\n");

    }

    public void useCustomArrayQueue(MyOwnDataType[] array,
                                    CustomArrayQueue<MyOwnDataType> customArrayQueue) {
        System.out.println("Insert to queue and peek: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customArrayQueue.insert(element);
                System.out.format("%s ", customArrayQueue.peek());
            }
            System.out.println("\nQueue is " + (customArrayQueue.isFull() ? "full" : "not full"));
        }));

        System.out.println("Remove from queue: " + Utils.checkTime(() ->
        {
            while (!customArrayQueue.isEmpty()) {
                customArrayQueue.remove();
            }

            if (customArrayQueue.getSize() == 0) {
                System.out.println("Custom queue based on array is empty");
            }
        }) + "\n");

    }

    public void useArrayDeque(MyOwnDataType[] array,
                                    Deque<MyOwnDataType> arrayDeque) {
        System.out.println("Add first to deque: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                arrayDeque.addFirst(element);
            }
        }));

        System.out.println("Poll first from deque: " + Utils.checkTime(() ->
        {
            while (!arrayDeque.isEmpty()) {
                arrayDeque.pollFirst();
            }
        }));

        System.out.println("Add last to deque: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                arrayDeque.addLast(element);
            }
        }));

        System.out.println("Poll last from deque: " + Utils.checkTime(() ->
        {
            while (!arrayDeque.isEmpty()) {
                arrayDeque.pollLast();
            }
        }));

        System.out.println("Add to deque and peek: " + Utils.checkTime(() ->
        {
            arrayDeque.add(array[0]);
            System.out.printf("Peek first: %s\n", arrayDeque.peekFirst());
            System.out.printf("Peek last: %s\n", arrayDeque.peekLast());
        }) + "\n");

    }

    public void usePriorityQueue(MyOwnDataType[] array,
                              Queue<MyOwnDataType> priorityQueue) {
        System.out.println("Add to priority queue: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                priorityQueue.offer(element);
            }
        }));

        System.out.println("Poll from priority queue: " + Utils.checkTime(() ->
        {
            while (!priorityQueue.isEmpty()) {
                System.out.printf("%s ", priorityQueue.poll());
            }
            System.out.println();
        }) + "\n");
    }

    public void useCustomLinkedStack(MyOwnDataType[] array,
                                     CustomLinkedStack<MyOwnDataType> customLinkedStack) {
        System.out.println("Push to custom linked stack: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customLinkedStack.push(element);
            }
        }));

        System.out.println("Pop from custom linked stack: " + Utils.checkTime(() ->
        {
            while (!customLinkedStack.isEmpty()) {
                System.out.printf("%s ", customLinkedStack.pop());
            }
            System.out.println();
        }) + "\n");
    }

    public void useCustomLinkedQueue(MyOwnDataType[] array,
                                     CustomLinkedQueue<MyOwnDataType> customLinkedQueue) {
        System.out.println("Insert to custom linked queue: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customLinkedQueue.insert(element);
            }
        }));

        System.out.println("Delete from custom linked queue: " + Utils.checkTime(() ->
        {
            while (!customLinkedQueue.isEmpty()) {
                System.out.printf("%s ", customLinkedQueue.delete());
            }
            System.out.println();
        }) + "\n");
    }

}
