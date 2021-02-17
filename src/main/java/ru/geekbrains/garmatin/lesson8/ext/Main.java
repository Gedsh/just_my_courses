package ru.geekbrains.garmatin.lesson8.ext;

import ru.geekbrains.garmatin.lesson8.ext.archive.Archive;
import ru.geekbrains.garmatin.lesson8.ext.archive.Author;
import ru.geekbrains.garmatin.lesson8.ext.archive.Book;
import ru.geekbrains.garmatin.lesson8.ext.librarian.Librarian;
import ru.geekbrains.garmatin.lesson8.ext.librarian.RudeLibrarian;
import ru.geekbrains.garmatin.lesson8.ext.reader.LoudReader;
import ru.geekbrains.garmatin.lesson8.ext.reader.QuietReader;
import ru.geekbrains.garmatin.lesson8.ext.reader.Reader;
import ru.geekbrains.garmatin.lesson8.ext.reader.StandardReader;
import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoom;

import java.util.Random;

public class Main {

    private static final int LIBRARY_PLACES = 20;
    private static final int LIBRARY_BOOKS_QUANTITY = 15;
    private static final int READERS_QUANTITY = 23;
    private static final int MAX_NOISE_LEVEL = 18;

    private static final Random random = new Random();


    public static void main(String[] args) {
        // Библиотека, в которой есть читальный зал и архив с книгами
        // У каждой книги есть автор
        // В библиотеке есть библиотекарь, который выдает место посетителям
        // Посетитель может взять только одну книгу, другой не может взять ту же самую. (ДЗ)
        // В библиотеке только один экземпляр каждой книги

        // (ДЗ)
//         (1) Сделать тихого и шумного посетителя, доброго и злого библиотекаря.
//         Так же в зале есть уровень шума при добавлении нового читателя, если шумный, то увеличивается на 2
//         если обычный, то на 1
//         если тихий, то вообще не увеличиваем
//         если уровень шума выше знначения, которое указывается для каждого читального зала отдельно, то
//         библиотекарь на это реагирует если добрый то реагирует мягко, а злой реагирует грубо.
//
//         (2) Сделать архив с книгами по аналогии с читальным залом и добавить возможность
//         для библиотекаря выдавать книги читателю, если у него ещё нет никакой книги.
//         Так же проверять доступность книги при выдаче

        // is-a => Наследование
        // has-a => Содержит (ассоциация)
        // агрегация => part-of Не строгий
        // композиция => part-of Строго

        ReadingRoom readingRoom = new ReadingRoom(LIBRARY_PLACES, MAX_NOISE_LEVEL);
        Archive archive = new Archive(LIBRARY_BOOKS_QUANTITY);
        Librarian librarian = new RudeLibrarian();

        Library library = new Library(readingRoom, archive, librarian);

        fillLibrary(library);

        Reader[] readers = generateReaders();

        sendReadersToLibrary(library, readers);

        askLibrarianBringBooks(librarian, readers);

        readersReturnBooksToLibrary(librarian, readers);

        readersLeaveLibrary(library, readers);
    }

    private static void fillLibrary(Library library) {

        Book book;

        do {
            int rnd = random.nextInt(LIBRARY_BOOKS_QUANTITY);
            book = new Book("Название" + rnd, new Author("Автор" + rnd));
        } while (library.addBook(book));

        System.out.println();
    }

    private static Reader[] generateReaders() {
        Reader[] readers = new Reader[READERS_QUANTITY];
        int counter = 0;

        while (counter < readers.length) {

            readers[counter] = new QuietReader("Читатель" + counter);
            counter++;

            if (counter < readers.length) {
                readers[counter] = new StandardReader("Читатель" + counter);
                counter++;
            } else {
                break;
            }

            if (counter < readers.length) {
                readers[counter] = new LoudReader("Читатель" + counter);
                counter++;
            } else {
                break;
            }
        }

        return readers;
    }

    private static void sendReadersToLibrary(Library library, Reader[] readers) {
        for (int i = 0; i < readers.length; i++) {
            Reader reader = readers[i];
            library.addReader(reader, i);
        }

        System.out.println();
    }

    private static void askLibrarianBringBooks(Librarian librarian, Reader[] readers) {

        for (int i = 0; i < readers.length; i++) {
            Reader reader = readers[i];

            Book book = askLibrarianBringBook(librarian, reader);

            if (book != null) {
                librarian.giveBookToReader(book, reader);
            }

            if (i % 5 == 0 && librarian.getCountAvailableBooks() > 0) {
                book = askLibrarianBringBook(librarian, reader);

                if (book != null) {
                    librarian.giveBookToReader(book, reader);
                }
            }
        }

        System.out.println();
    }

    private static Book askLibrarianBringBook(Librarian librarian, Reader reader) {
        Book[] booksFullList = librarian.getBooksFullList();

        Book book;
        do {
            String title = booksFullList[random.nextInt(booksFullList.length)].getTitle();
            book = librarian.getBookFromArchive(reader, title);
        } while (book == null && librarian.getCountAvailableBooks() > 0);

        return book;
    }

    private static void readersReturnBooksToLibrary(Librarian librarian, Reader[] readers) {
        for (Reader reader : readers) {
            if (reader.getReaderTicket() < 0) {
                continue;
            }

            Book book = librarian.takeBookFromUser(reader);

            if (book != null) {
                librarian.returnBookToArchive(book);
            }
        }

        System.out.println();
    }

    private static void readersLeaveLibrary(Library library, Reader[] readers) {
        for (Reader reader : readers) {
            if (reader.getReaderTicket() >= 0) {
                library.removeReader(reader);
            }
        }
    }
}
