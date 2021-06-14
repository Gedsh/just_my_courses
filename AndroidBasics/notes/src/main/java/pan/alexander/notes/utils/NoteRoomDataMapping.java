package pan.alexander.notes.utils;

import java.util.ArrayList;
import java.util.List;

import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

public class NoteRoomDataMapping {

    public static Trash noteToTrash(Note note) {
        return new Trash(
                note.getTitle(),
                note.getDescription(),
                note.getType(),
                note.getTime(),
                note.getColor());
    }

    public static List<Trash> notesToTrashes(List<Note> notes) {
        List<Trash> trashes = new ArrayList<>();
        for (Note note: notes) {
            Trash trash = new Trash(
                    note.getTitle(),
                    note.getDescription(),
                    note.getType(),
                    note.getTime(),
                    note.getColor());
            trashes.add(trash);
        }
        return trashes;
    }

    public static Note trashToNote(Trash trash) {
        return new Note(
                trash.getTitle(),
                trash.getDescription(),
                trash.getType(),
                trash.getTime(),
                trash.getColor());
    }

    public static List<Note> trashesToNotes(List<Trash> trashes) {
        List<Note> notes = new ArrayList<>();
        for (Trash trash: trashes) {
            Note note = new Note(
                    trash.getTitle(),
                    trash.getDescription(),
                    trash.getType(),
                    trash.getTime(),
                    trash.getColor());
            notes.add(note);
        }

        return notes;
    }

}
