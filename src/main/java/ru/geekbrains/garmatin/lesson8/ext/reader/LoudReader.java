package ru.geekbrains.garmatin.lesson8.ext.reader;

public class LoudReader extends Reader {
    public LoudReader(String fullName) {
        super(fullName);
    }

    @Override
    public Loudness getLoudness() {
        return Loudness.HIGH;
    }
}
