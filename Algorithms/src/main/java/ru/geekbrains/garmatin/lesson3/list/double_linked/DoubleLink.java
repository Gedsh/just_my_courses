package ru.geekbrains.garmatin.lesson3.list.double_linked;

import java.util.Objects;

class DoubleLink<T> {
    private final T link;
    private DoubleLink<T> next;
    private DoubleLink<T> previous;

    public DoubleLink(DoubleLink<T> previous, T link, DoubleLink<T> next) {
        this.link = link;
        this.next = next;
        this.previous = previous;
    }

    public DoubleLink<T> getNext() {
        return next;
    }

    public void setNext(DoubleLink<T> next) {
        this.next = next;
    }

    public DoubleLink<T> getPrevious() {
        return previous;
    }

    public void setPrevious(DoubleLink<T> previous) {
        this.previous = previous;
    }

    public T getValue() {
        return link;
    }

    @Override
    public String toString() {
        return "DoubleLink{" +
                "link=" + link +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleLink<?> that = (DoubleLink<?>) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
