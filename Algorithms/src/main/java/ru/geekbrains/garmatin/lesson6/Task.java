package ru.geekbrains.garmatin.lesson6;

import ru.geekbrains.garmatin.lesson6.heap.Heap;
import ru.geekbrains.garmatin.lesson6.tree.Tree;

import java.util.*;

public class Task {

    public void showTreeExample() {
        List<String> list = new ArrayList<>();
        System.out.printf("ArrayList hierarchy as part of the Java class tree %s\n\n", Utils.getSuperClasses(list));
    }

    public void findElementInTree(Tree<MyOwnDataType> tree, MyOwnDataType element) {
        System.out.println("Tree find: " + Utils.checkTime(() ->
                System.out.printf("Result: %s\n", tree.find(element))));
    }

    public void insertElementInTree(Tree<MyOwnDataType> tree, MyOwnDataType element) {
        System.out.println("Tree insert: " + Utils.checkTime(() ->
                tree.insert(element)) + "\n");
    }

    public void showTree(Tree<MyOwnDataType> tree) {
        System.out.println("Show tree: " + Utils.checkTime(tree::displayTree) + "\n");
    }

    public void minElementInTree(Tree<MyOwnDataType> tree) {
        System.out.println("Min tree element: " + Utils.checkTime(() ->
                System.out.println(tree.min())) + "\n");
    }

    public void maxElementInTree(Tree<MyOwnDataType> tree) {
        System.out.println("Max tree element: " + Utils.checkTime(() ->
                System.out.println(tree.max())) + "\n");
    }

    public void deleteElementInTree(Tree<MyOwnDataType> tree, MyOwnDataType element) {
        System.out.println("Tree delete: " + Utils.checkTime(() ->
                tree.delete(element)) + "\n");
        tree.displayTree();
        System.out.println();
    }

    public void sortWithHeap(MyOwnDataType[] array) {
        MyOwnDataType[] copy1 = Arrays.copyOf(array, array.length);
        MyOwnDataType[] copy2 = Arrays.copyOf(array, array.length);
        Heap<MyOwnDataType> heap = new Heap<>();

        System.out.println(Arrays.toString(copy1));
        System.out.println("Heap sort: " + Utils.checkTime(() ->
                heap.sort(copy1)));
        System.out.println(Arrays.toString(copy1));
        System.out.println("Tim sort: " + Utils.checkTime(() ->
                Arrays.sort(copy2)));
        System.out.println(Arrays.toString(copy2) + "\n");
    }

    public void useBalancedTree(MyOwnDataType[] array) {
        Set<MyOwnDataType> balancedTreeSet = new TreeSet<>();
        System.out.println("A Red-Black tree based Set: " + Utils.checkTime(() ->
                balancedTreeSet.addAll(Arrays.asList(array))));
        System.out.println(balancedTreeSet);
    }

}
