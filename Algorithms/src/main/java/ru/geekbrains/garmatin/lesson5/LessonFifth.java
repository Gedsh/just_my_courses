package ru.geekbrains.garmatin.lesson5;

public class LessonFifth {
    private static final int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);
        MyOwnDataType searchElement = array[array.length * 2 / 3];

        Task task = new Task();

        task.recursiveExample();

        task.showEndlessAndFiniteRecursion();

        task.showStackExample();

        task.recursionAndLoopExample();

        task.arraySearch(array, searchElement);

        task.arraySort(array);
    }
}
