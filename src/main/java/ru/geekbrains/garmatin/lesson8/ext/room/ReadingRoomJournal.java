package ru.geekbrains.garmatin.lesson8.ext.room;

import ru.geekbrains.garmatin.lesson8.ext.archive.Book;
import ru.geekbrains.garmatin.lesson8.ext.reader.Reader;

import java.util.Random;

public class ReadingRoomJournal {

    Random random = new Random();

    private final ReadingRoom readingRoom;
    private final int commonCountPlaces;
    private final Reader[] readers;
    private int countAvailablePlace;

    public ReadingRoomJournal(ReadingRoom readingRoom) {
        this.readingRoom = readingRoom;
        this.commonCountPlaces = readingRoom.getCommonCountPlaces();
        this.countAvailablePlace = commonCountPlaces;
        this.readers = new Reader[commonCountPlaces];
    }

    public int givePlace(Reader reader) {

        if (commonCountPlaces <= 0) {
            System.err.println("Библиотекарь не должен искать место если уже нет мест!");
            return -1;
        }

        while (true) {
            int position = random.nextInt(commonCountPlaces);

            if (readers[position] == null) {
                readers[position] = reader;
                countAvailablePlace--;
                return position;
            }
        }
    }

    public void releasePlace(int position) {
        if (readers[position] != null) {
            readers[position] = null;
            countAvailablePlace++;
        }

    }

    public int findReader(Reader reader) {
        for (int i = 0; i < readers.length; i++) {
            if (readers[i] != null && readers[i].equals(reader)) {
                return i;
            }
        }

        return -1;
    }

    public int getCountAvailablePlace() {
        return countAvailablePlace;
    }
}
