package ru.geekbrains.garmatin.lesson8.ext.archive;

import java.util.Random;

public class ArchiveJournal {
    private final Archive archive;
    private final Book[] books;
    private final Random random = new Random();
    private final int commonBooksQuantity;
    private int countAvailableBooks;
    private final Book[] booksFullList;

    public ArchiveJournal(Archive archive) {
        this.archive = archive;
        this.commonBooksQuantity = archive.getCommonBooksQuantity();

        books = new Book[commonBooksQuantity];
        booksFullList = new Book[commonBooksQuantity];
    }

    public int findBook(Book book) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].equals(book)) {
                return i;
            }
        }

        return -1;
    }

    public int findBook(String title) {
        for (int i = 0; i < books.length; i++) {
            if (books[i] != null && books[i].getTitle().equals(title)) {
                return i;
            }
        }

        return -1;
    }

    public void registerBook(Book book) {
        if (countAvailableBooks == commonBooksQuantity) {
            System.err.println("Библиотекарь не должен пытаться регистрировать книгу если уже нет мест!");
        }

        while (true) {
            int position = random.nextInt(commonBooksQuantity);
            if (books[position] == null) {
                book.setArchiveNumber(position);
                books[position] = book;
                booksFullList[position] = book;

                countAvailableBooks++;

                break;
            }
        }
    }

    public Book getBook(String title) {
        Book book;

        int position = findBook(title);

        if (position >= 0) {
            book = books[position];
            books[position] = null;
            countAvailableBooks--;
            return book;
        }

        return null;
    }

    public void returnBook(Book book, int position) {
        if (position >= 0
                && position < commonBooksQuantity
                && books[position] == null) {
            books[position] = book;
            countAvailableBooks++;
        }
    }

    public int getCommonBooksQuantity() {
        return commonBooksQuantity;
    }

    public int getCountAvailableBooks() {
        return countAvailableBooks;
    }

    public Book[] getBooksFullList() {
        return booksFullList;
    }
}
