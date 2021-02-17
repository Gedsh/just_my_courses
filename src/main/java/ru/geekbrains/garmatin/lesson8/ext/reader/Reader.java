package ru.geekbrains.garmatin.lesson8.ext.reader;

import ru.geekbrains.garmatin.lesson8.ext.archive.Book;

import java.util.Objects;

public abstract class Reader {

    private int readerTicket = -1;
    private final String fullName;
    private Book book;


    public Reader(String fullName) {
        this.fullName = fullName;
    }

    public abstract Loudness getLoudness();

    public int getReaderTicket() {
        return readerTicket;
    }

    public void setReaderTicket(int readerTicket) {
        this.readerTicket = readerTicket;
    }

    public String getFullName() {
        return fullName;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return readerTicket == reader.readerTicket && fullName.equals(reader.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerTicket, fullName);
    }
}
