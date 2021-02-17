package ru.geekbrains.garmatin.lesson8.ext.archive;

public class Archive {
    private final ArchiveJournal archiveJournal;
    private final int commonBooksQuantity;

    public Archive(int commonBooksQuantity) {
        this.commonBooksQuantity = commonBooksQuantity;

        archiveJournal = new ArchiveJournal(this);
    }

    public ArchiveJournal getArchiveJournal() {
        return archiveJournal;
    }

    public int getCommonBooksQuantity() {
        return commonBooksQuantity;
    }
}
