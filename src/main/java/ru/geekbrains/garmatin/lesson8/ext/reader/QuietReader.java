package ru.geekbrains.garmatin.lesson8.ext.reader;

public class QuietReader extends Reader{
    public QuietReader( String fullName) {
        super(fullName);
    }

    @Override
    public Loudness getLoudness() {
        return Loudness.ZERO;
    }
}
