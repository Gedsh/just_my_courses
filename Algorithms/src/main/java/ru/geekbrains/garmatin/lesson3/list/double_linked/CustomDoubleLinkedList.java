package ru.geekbrains.garmatin.lesson3.list.double_linked;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomDoubleLinkedList<E> {
    DoubleLink<E> first;
    DoubleLink<E> last;
    int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E link) {
        DoubleLink<E> last = this.last;
        DoubleLink<E> element = new DoubleLink<>(last, link, null);

        this.last = element;
        if (last == null) {
            first = element;
        } else {
            last.setNext(element);
        }

        size++;
    }

    public E remove(int index) {

        DoubleLink<E> element = getElement(index);
        DoubleLink<E> next = element.getNext();
        DoubleLink<E> previous = element.getPrevious();

        if (previous == null) {
            first = next;
        } else {
            previous.setNext(next);
            element.setPrevious(null);
        }

        if (next == null) {
            last = previous;
        } else {
            next.setPrevious(previous);
            element.setNext(null);
        }

        size--;

        return element.getValue();
    }

    public E get(int index) {
        return getElement(index).getValue();
    }

    private DoubleLink<E> getElement(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        DoubleLink<E> element;
        if (index < size / 2) {
            element = first;
            for (int i = 0; i < index; i++) {
                element = element.getNext();
            }
        } else {
            element = last;
            for (int i = size - 1; i > index; i--) {
                element = element.getPrevious();
            }
        }
        return element;
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("List is empty");
        }

        Iterator<E> iterator = iterator();

        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    public Iterator<E> iterator() {
        return new Iterate();
    }

    private class Iterate implements Iterator<E> {

        int cursor = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                CustomDoubleLinkedList.this.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
