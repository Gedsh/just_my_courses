package ru.geekbrains.garmatin.lesson8.ext.reader;

public enum Loudness {

    HIGH(2),
    LOW(1),
    ZERO(0);

    private final int level;

    Loudness(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
