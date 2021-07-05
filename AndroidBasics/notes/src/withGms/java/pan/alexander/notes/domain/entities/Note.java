package pan.alexander.notes.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Keep
public class Note implements Parcelable, Comparable<Note> {
    private String id;
    private String title;
    private String description;
    @NoteType
    private final int type;
    private long time;
    private String color;

    public Note(@NonNull String title,
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

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        type = in.readInt();
        time = in.readLong();
        color = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @NoteType
    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return time == note.time &&
                title.equals(note.title) &&
                description.equals(note.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeLong(time);
        dest.writeString(color);
    }

    @Override
    public int compareTo(Note o) {
        return (int) (o.time - time);
    }

    @NotNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", color='" + color + '\'' +
                '}';
    }
}
