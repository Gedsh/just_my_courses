package ru.geekbrains.garmatin.lesson8.ext.librarian;

import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoom;

public class GentleLibrarian extends Librarian {
    public void checkNoiseLevel(ReadingRoom readingRoom) {
        if (readingRoom.getNoiseValue() > readingRoom.getMaxNoiseValue()) {
            System.out.println("Библиотекарь просит вести себя потише");
        }
    }
}
