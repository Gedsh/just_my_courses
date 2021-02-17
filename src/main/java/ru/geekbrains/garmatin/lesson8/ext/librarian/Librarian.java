package ru.geekbrains.garmatin.lesson8.ext.librarian;

import ru.geekbrains.garmatin.lesson8.ext.archive.ArchiveJournal;
import ru.geekbrains.garmatin.lesson8.ext.archive.Book;
import ru.geekbrains.garmatin.lesson8.ext.reader.Reader;
import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoom;
import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoomJournal;

public abstract class Librarian {

    private ReadingRoomJournal readingRoomJournal;
    private ArchiveJournal archiveJournal;

    public abstract void checkNoiseLevel(ReadingRoom readingRoom);

    public boolean tryGivePlace(Reader reader) {
        int countAvailablePlace = readingRoomJournal.getCountAvailablePlace();

        if (countAvailablePlace == 0) {
            System.out.println(reader.getFullName() + ", мест в зале больше нет");
            return false;
        }

        int position = readingRoomJournal.findReader(reader);

        if (position >= 0) {
            System.out.println("Вы уже взяли одну книгу!");
            return false;
        }

        int appointedPosition = readingRoomJournal.givePlace(reader);

        if (appointedPosition >= 0) {
            System.out.println(reader.getFullName() + ", займите " + appointedPosition + " место");
        }

        return true;
    }


    public boolean releasePlace(Reader reader) {
        int position = readingRoomJournal.findReader(reader);

        if (position >= 0) {
            readingRoomJournal.releasePlace(position);

            System.out.println("Пользователь " + reader.getFullName() + " освободил " + position + " место");

            return true;
        }

        return false;
    }

    public boolean addBookToArchive(Book book) {

        int availablePlacesForBooks = archiveJournal.getCommonBooksQuantity() - archiveJournal.getCountAvailableBooks();

        if (availablePlacesForBooks == 0) {
            System.out.println("Архив заполнен");
            return false;
        }

        if (archiveJournal.findBook(book) >= 0) {
            System.out.println("Книга " + book + " уже есть в архиве");
            return true;
        }

        archiveJournal.registerBook(book);

        System.out.println("В библиотеку добавили новую книгу " + book);

        return true;
    }

    public Book getBookFromArchive(Reader reader, String title) {

        if (reader.getReaderTicket() < 0) {
            return null;
        }

        System.out.println("Пользователь " + reader.getFullName() + " попросил книгу с названием " + title);

        if (archiveJournal.getCountAvailableBooks() == 0) {
            System.out.println("Библиотекарь не может выдать книгу " + title + ". Архив пустой");
            return null;
        }

        Book book = archiveJournal.getBook(title);

        if (book == null) {
            System.out.println("Книги с названием " + title + " нет в архиве.");
        }

        return book;
    }

    public void returnBookToArchive(Book book) {
        int archiveNumber = book.getArchiveNumber();

        if (archiveNumber < 0) {
            System.err.println("У книги " + book + " нет архивного номера! Возможно она с другой библиотеки.");
            return;
        }

        archiveJournal.returnBook(book, archiveNumber);
    }

    public void giveBookToReader(Book book, Reader reader) {
        if (reader.getBook() == null) {
            reader.setBook(book);
            System.out.println("Пользователю " + reader.getFullName() + " выдана книга " + book.getTitle());
        } else {
            returnBookToArchive(book);
            System.out.println("Пользователь " + reader.getFullName() + " уже взял " + reader.getBook().getTitle()
                    + ". Книгу " + book.getTitle() + " вернули в архив.");
        }
    }

    public Book takeBookFromUser(Reader reader) {
        Book book = reader.getBook();
        if (book != null) {
            reader.setBook(null);
            System.out.println("Пользователь " + reader.getFullName() + " вернул книгу " + book.getTitle());
        } else {
            System.out.println("У пользователя " + reader.getFullName() + " нет книги!");
        }

        return book;
    }

    public Book[] getBooksFullList() {
        return archiveJournal.getBooksFullList();
    }

    public int getCountAvailableBooks() {
        return archiveJournal.getCountAvailableBooks();
    }

    public void setReadingRoomJournal(ReadingRoomJournal readingRoomJournal) {
        this.readingRoomJournal = readingRoomJournal;
    }

    public void setArchiveJournal(ArchiveJournal archiveJournal) {
        this.archiveJournal = archiveJournal;
    }
}
