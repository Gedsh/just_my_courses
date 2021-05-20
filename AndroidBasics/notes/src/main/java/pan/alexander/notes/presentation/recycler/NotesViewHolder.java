package pan.alexander.notes.presentation.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView textViewNotesItemNoteTitle;
    private final TextView textViewNotesItemNoteDate;
    private final MutableLiveData<Note> onClickLiveListener;
    private final List<Note> notes;

    public NotesViewHolder(@NonNull View itemView, MutableLiveData<Note> onClickLiveListener, List<Note> notes) {
        super(itemView);

        this.onClickLiveListener = onClickLiveListener;
        this.notes = notes;

        LinearLayoutCompat linearLayoutItemNote = itemView.findViewById(R.id.linearLayoutItemNote);
        linearLayoutItemNote.setOnClickListener(this);
        textViewNotesItemNoteTitle = itemView.findViewById(R.id.textViewNotesItemNoteTitle);
        textViewNotesItemNoteDate = itemView.findViewById(R.id.textViewNotesItemNoteDate);
    }

    public void bind(Note note) {
        textViewNotesItemNoteTitle.setText(note.getTitle());
        textViewNotesItemNoteDate.setText(formatTime(note.getTime()));
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        return dateFormat.format(date);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) {
            return;
        }

        if (v.getId() == R.id.linearLayoutItemNote) {
            onClickLiveListener.setValue(notes.get(getAdapterPosition()));
        }
    }
}
