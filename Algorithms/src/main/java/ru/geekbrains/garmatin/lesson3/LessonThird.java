package ru.geekbrains.garmatin.lesson3;

import ru.geekbrains.garmatin.lesson3.list.double_linked.CustomDoubleLinkedList;
import ru.geekbrains.garmatin.lesson3.list.linked.CustomLinkedList;

import java.util.ArrayList;
import java.util.LinkedList;

public class LessonThird {
    private static final int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);

        MyOwnDataType element = array[array.length * 2 / 3];

        Task task = new Task();

        ArrayList<MyOwnDataType> arrayList = new ArrayList<>();
        LinkedList<MyOwnDataType> linkedList = new LinkedList<>();

        task.arrayToList(array, arrayList, linkedList);

        task.addToList(element, arrayList, linkedList);
        task.removeFromList(1, arrayList, linkedList);
        task.getFromList(array.length * 2 / 3, arrayList, linkedList);

        CustomLinkedList<MyOwnDataType> customLinkedList = new CustomLinkedList<>();
        task.useCustomLinkedList(array, customLinkedList, element);

        CustomDoubleLinkedList<MyOwnDataType> customDoubleLinkedList = new CustomDoubleLinkedList<>();
        task.useCustomDoubleLinkedList(array, customDoubleLinkedList, array.length * 2 / 3);

        task.useCustomDoubleLinkedListIterator(customDoubleLinkedList);
    }
}
