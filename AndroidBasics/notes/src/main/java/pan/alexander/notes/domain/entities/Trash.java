package pan.alexander.notes.domain.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
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
