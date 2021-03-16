package ru.geekbrains.garmatin.lesson4.linked;

public class CustomLinkedListForQueue<E> {
    Link<E> first;
    Link<E> last;

    public boolean isEmpty() {
        return first == null;
    }

    public void insert(E link) {
        Link<E> element = new Link<>(link);

        if (isEmpty()) {
            first = element;
        } else {
            last.setNext(element);
        }
        last = element;
    }

    public E delete() {
        Link<E> element = first;

        if (first.getNext() == null) {
            last = null;
        }
        first = first.getNext();
        return element.getValue();
    }

    public void display() {
        Link<E> current = first;
        while (current != null) {
            System.out.print(current.getValue() + " ");
            current = current.getNext();
        }
        System.out.println();
    }

    public E find(E search) {
        Link<E> current = first;

        while (current != null) {
            if (current.getValue().equals(search)) {
                return search;
            }

            current = current.getNext();
        }

        return null;

    }

}
