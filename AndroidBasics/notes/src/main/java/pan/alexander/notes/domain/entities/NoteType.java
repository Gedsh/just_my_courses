package pan.alexander.notes.domain.entities;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({
        NoteType.TEXT_NOTE,
        NoteType.LIST_NOTE
})

public @interface NoteType {
    int TEXT_NOTE = 1;
    int LIST_NOTE = 2;
}

