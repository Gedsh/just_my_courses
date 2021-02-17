package ru.geekbrains.garmatin.lesson8.ext.librarian;

import ru.geekbrains.garmatin.lesson8.ext.room.ReadingRoom;

public class RudeLibrarian extends Librarian {
    public void checkNoiseLevel(ReadingRoom readingRoom) {
        if (readingRoom.getNoiseValue() > readingRoom.getMaxNoiseValue()) {
            System.out.println("Библиотекарь бросил книгу в самого шумного посетителя");
        }
    }
}
