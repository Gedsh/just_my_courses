package ru.geekbrains.garmatin.lesson8.ext.reader;

public class StandardReader extends Reader{
    public StandardReader(String fullName) {
        super(fullName);
    }

    @Override
    public Loudness getLoudness() {
        return Loudness.LOW;
    }
}
