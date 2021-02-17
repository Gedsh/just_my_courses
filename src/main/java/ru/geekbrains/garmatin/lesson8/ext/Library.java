package ru.geekbrains.garmatin.lesson8.ext;

import ru.geekbrains.garmatin.lesson8.ext.archive.Archive;
import ru.geekbrains.garmatin.lesson8.ext.archive.Book;
import ru.geekbrains.garmatin.lesson8.ext.librarian.Librarian;
import ru.geekbrains.garmatin.lesson8.ext.reader.Reader;
import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoom;

public class Library {

    private final ReadingRoom readingRoom;
    private final Archive archive;

    private Librarian librarian;

    public Library(ReadingRoom readingRoom, Archive archive, Librarian librarian) {
        this.readingRoom = readingRoom;
        this.archive = archive;

        setLibrarian(librarian);
    }

    public void addReader(Reader reader, int ticket) {
        if (librarian.tryGivePlace(reader)) {
            reader.setReaderTicket(ticket);
            readingRoom.increaseNoise(reader);
            librarian.checkNoiseLevel(readingRoom);
        }
    }

    public void removeReader(Reader reader) {
        if (librarian.releasePlace(reader)) {
            reader.setReaderTicket(-1);
            readingRoom.decreaseNoise(reader);
        }
    }

    public boolean addBook(Book book) {
        return librarian.addBookToArchive(book);
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
        this.librarian.setReadingRoomJournal(readingRoom.getJournal());
        this.librarian.setArchiveJournal(archive.getArchiveJournal());
    }
}
