package pan.alexander.notes.domain.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Trash {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String title;
    private final String description;
    @NoteType
    private final int type;
    private final long time;
    private final String color;

    public Trash(@NonNull String title,
                 @NonNull String description,
                 @NoteType int type,
                 long time,
                 @NonNull String color) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.time = time;
        this.color = color;
    }

    @Ignore
    private Trash(@NonNull Note note) {
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.type = note.getType();
        this.time = note.getTime();
        this.color = note.getColor();
    }

    public static Trash noteToTrash(Note note) {
        return new Trash(note);
    }

    public static List<Trash> notesToTrashes(List<Note> notes) {
        List<Trash> trashes = new ArrayList<>();
        for (Note note: notes) {
            trashes.add(new Trash(note));
        }
        return trashes;
    }

    @Override
    public String toString() {
        return "Trash{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", color='" + color + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public String getColor() {
        return color;
    }
}
