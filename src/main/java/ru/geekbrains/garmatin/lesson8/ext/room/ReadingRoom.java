package ru.geekbrains.garmatin.lesson8.ext.room;

import ru.geekbrains.garmatin.lesson8.ext.reader.Reader;

public class ReadingRoom {

    private final int commonCountPlaces;
    private final ReadingRoomJournal journal;
    private final int maxNoiseValue;

    private int noiseValue;

    public ReadingRoom(int commonCountPlaces, int maxNoiseValue) {
        this.commonCountPlaces = commonCountPlaces;
        this.maxNoiseValue = maxNoiseValue;

        journal = new ReadingRoomJournal(this);
    }

    public void increaseNoise(Reader reader) {
        noiseValue += reader.getLoudness().getLevel();
    }

    public void decreaseNoise(Reader reader) {
        noiseValue -= reader.getLoudness().getLevel();
    }

    public ReadingRoomJournal getJournal() {
        return journal;
    }

    public int getCommonCountPlaces() {
        return commonCountPlaces;
    }

    public int getMaxNoiseValue() {
        return maxNoiseValue;
    }

    public int getNoiseValue() {
        return noiseValue;
    }
}
