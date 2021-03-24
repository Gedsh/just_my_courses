package ru.geekbrains.garmatin.lesson6.heap;

public class Heap<E extends Comparable<E>> {

    private int heapSize;

    public void sort(E[] array) {
        buildHeap(array);
        while (heapSize > 1) {
            swap(array, 0, heapSize - 1);
            heapSize--;
            heapify(array, 0);
        }
    }

    private void buildHeap(E[] array) {
        heapSize = array.length;
        for (int i = array.length / 2; i >= 0; i--) {
            heapify(array, i);
        }
    }

    private void heapify(E[] array, int i) {
        int l = left(i);
        int r = right(i);
        int largest = i;

        if (l < heapSize && array[i].compareTo(array[l]) < 0) {
            largest = l;
        }
        if (r < heapSize && array[largest].compareTo(array[r]) < 0) {
            largest = r;
        }
        if (i != largest) {
            swap(array, i, largest);
            heapify(array, largest);
        }
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
