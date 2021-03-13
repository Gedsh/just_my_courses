package ru.geekbrains.garmatin.lesson3;

import ru.geekbrains.garmatin.lesson3.list.double_linked.CustomDoubleLinkedList;
import ru.geekbrains.garmatin.lesson3.list.linked.CustomLinkedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Task {
    public void arrayToList(MyOwnDataType[] array,
                            ArrayList<MyOwnDataType> arrayList,
                            LinkedList<MyOwnDataType> linkedList) {
        System.out.println("Array to array list: " + Utils.checkTime(() ->
                Utils.arrayToArrayList(array, arrayList)));
        System.out.println("Array to linked list: " + Utils.checkTime(() ->
                Utils.arrayToLinkedList(array, linkedList)) + "\n");
    }

    public void addToList(MyOwnDataType elementToAdd,
                          ArrayList<MyOwnDataType> arrayList,
                          LinkedList<MyOwnDataType> linkedList) {
        System.out.println("Add to array list: " + Utils.checkTime(() ->
                arrayList.add(elementToAdd)));
        System.out.println("Add to linked list: " + Utils.checkTime(() ->
                linkedList.add(elementToAdd)) + "\n");
    }

    public void removeFromList(int indexToRemove,
                               ArrayList<MyOwnDataType> arrayList,
                               LinkedList<MyOwnDataType> linkedList) {
        System.out.println("Remove from array list: " + Utils.checkTime(() ->
                arrayList.remove(indexToRemove)));
        System.out.println("Remove from linked list: " + Utils.checkTime(() ->
                linkedList.remove(indexToRemove)) + "\n");
    }

    public void getFromList(int indexToGet,
                            ArrayList<MyOwnDataType> arrayList,
                            LinkedList<MyOwnDataType> linkedList) {
        System.out.println("Get from array list: " + Utils.checkTime(() ->
                arrayList.get(indexToGet)));
        System.out.println("Get from linked list: " + Utils.checkTime(() ->
                linkedList.get(indexToGet)) + "\n");
    }

    public void useCustomLinkedList(MyOwnDataType[] array,
                                    CustomLinkedList<MyOwnDataType> customLinkedList,
                                    MyOwnDataType elementToFind) {

        System.out.println("Add to custom linked list: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customLinkedList.insert(element);
            }
        }));

        customLinkedList.display();

        System.out.println("Delete from custom linked list: " + Utils.checkTime(() ->
                customLinkedList.delete()));

        customLinkedList.display();

        System.out.println("Find in custom linked list: " + Utils.checkTime(() ->
                System.out.println(customLinkedList.find(elementToFind))) + "\n");
    }

    public void useCustomDoubleLinkedList(MyOwnDataType[] array,
                                          CustomDoubleLinkedList<MyOwnDataType> customLinkedList,
                                          int indexToGet) {
        System.out.println("Add to custom doubly linked list: " + Utils.checkTime(() ->
        {
            for (MyOwnDataType element : array) {
                customLinkedList.add(element);
            }
        }));

        System.out.println("Get from custom doubly linked list: " + Utils.checkTime(() ->
                System.out.println(customLinkedList.get(indexToGet))));

        System.out.println("Remove from custom doubly linked list: " + Utils.checkTime(() ->
                System.out.println(customLinkedList.remove(indexToGet))) + "\n");

    }

    public void useCustomDoubleLinkedListIterator(CustomDoubleLinkedList<MyOwnDataType> customLinkedList) {
        customLinkedList.print();

        System.out.println("Use custom doubly linked list iterator to clear list: " + Utils.checkTime(() ->
        {
            Iterator<MyOwnDataType> iterator = customLinkedList.iterator();

            while (iterator.hasNext()) {
               iterator.next();
               iterator.remove();
            }
        }));

        customLinkedList.print();
    }

}
