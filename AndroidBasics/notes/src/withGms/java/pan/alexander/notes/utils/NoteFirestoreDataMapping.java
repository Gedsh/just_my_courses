package pan.alexander.notes.utils;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pan.alexander.notes.data.database.FirestoreFields;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;

public class NoteFirestoreDataMapping {

    @NonNull
    public static Note documentToNote(@NonNull String id, @NonNull Map<String, Object> document) {

        String title = (String) document.get(FirestoreFields.TITLE);
        String description = (String) document.get(FirestoreFields.DESCRIPTION);
        @NoteType Long type = (Long) document.get(FirestoreFields.TYPE);
        Timestamp timestamp = (Timestamp) document.get(FirestoreFields.TIME);
        String color = (String) document.get(FirestoreFields.COLOR);

        Note note = new Note(
                title != null ? title : "",
                description != null ? description : "",
                type != null ? type.intValue() : NoteType.TEXT_NOTE,
                timestamp != null ? timestamp.toDate().getTime() : System.currentTimeMillis(),
                color != null ? color : ""
        );
        note.setId(id);

        return note;
    }

    @NonNull
    public static List<Note> snapshotToNotes(QuerySnapshot snapshot) {

        if (snapshot == null) {
            return Collections.emptyList();
        }

        List<Note> notes = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            notes.add(documentToNote(document.getId(), document.getData()));
        }

        return notes;
    }

    @NonNull
    public static Map<String, Object> noteToDocument(@NonNull Note note) {
        Map<String, Object> doc = new HashMap<>();
        doc.put(FirestoreFields.TITLE, note.getTitle());
        doc.put(FirestoreFields.DESCRIPTION, note.getDescription());
        doc.put(FirestoreFields.TYPE, note.getType());
        doc.put(FirestoreFields.TIME, new Date(note.getTime()));
        doc.put(FirestoreFields.COLOR, note.getColor());
        return doc;
    }
}
