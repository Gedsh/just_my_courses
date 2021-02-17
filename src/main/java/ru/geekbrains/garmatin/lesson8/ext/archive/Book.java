package ru.geekbrains.garmatin.lesson8.ext.archive;

import java.util.Objects;

public class Book {
    private final String title;
    private final Author author;
    private int archiveNumber = -1;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public int getArchiveNumber() {
        return archiveNumber;
    }

    public void setArchiveNumber(int archiveNumber) {
        this.archiveNumber = archiveNumber;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return title.equals(book.title) && author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author=" + author +
                '}';
    }
}
