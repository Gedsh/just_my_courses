package ru.geekbrains.garmatin.lesson6;

import ru.geekbrains.garmatin.lesson6.tree.Tree;

public class LessonSixth {
    private static final int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);

        Tree<MyOwnDataType> tree = new Tree<>();
        for (MyOwnDataType element: array) {
            tree.insert(element);
        }

        MyOwnDataType searchElement = array[array.length * 2 / 3];
        MyOwnDataType insertElement = new MyOwnDataType(ARRAY_LENGTH + 1);

        Task task = new Task();

        task.showTreeExample();

        task.findElementInTree(tree, searchElement);
        task.insertElementInTree(tree, insertElement);

        task.showTree(tree);
        task.minElementInTree(tree);
        task.maxElementInTree(tree);

        task.deleteElementInTree(tree, searchElement);

        task.sortWithHeap(array);

        task.useBalancedTree(array);
    }
}
