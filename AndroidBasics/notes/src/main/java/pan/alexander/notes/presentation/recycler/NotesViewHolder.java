package pan.alexander.notes.presentation.recycler;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.utils.Utils;

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private final NotesAdapter notesAdapter;
    private final TextView textViewNotesItemNoteTitle;
    private final TextView textViewNotesItemNoteDate;
    private final View selectedOverlay;

    public NotesViewHolder(@NonNull View itemView, NotesAdapter notesAdapter) {
        super(itemView);

        this.notesAdapter = notesAdapter;

        FrameLayout frameLayoutItemNote = itemView.findViewById(R.id.frameLayoutItemNote);
        frameLayoutItemNote.setOnClickListener(this);
        frameLayoutItemNote.setOnLongClickListener(this);
        textViewNotesItemNoteTitle = itemView.findViewById(R.id.textViewNotesItemNoteTitle);
        textViewNotesItemNoteDate = itemView.findViewById(R.id.textViewNotesItemNoteDate);
        selectedOverlay = itemView.findViewById(R.id.selectedOverlay);
    }

    public void bind(int position) {

        if (position < 0) {
            return;
        }

        Note note = notesAdapter.notes.get(position);

        textViewNotesItemNoteTitle.setText(note.getTitle());
        if (textViewNotesItemNoteDate != null) {
            textViewNotesItemNoteDate.setText(Utils.formatTime(note.getTime()));
        }

        if (notesAdapter.isSelected(position)) {
            selectedOverlay.setVisibility(View.VISIBLE);
        } else {
            selectedOverlay.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) {
            return;
        }

        if (v.getId() == R.id.frameLayoutItemNote && notesAdapter.clickListener != null) {
            notesAdapter.clickListener.onItemClicked(position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) {
            return false;
        }

        if (v.getId() == R.id.frameLayoutItemNote && notesAdapter.clickListener != null) {
            notesAdapter.clickListener.onItemLongClicked(position);
        }

        return false;
    }

    public interface ClickListener {
        void onItemClicked(int position);

        void onItemLongClicked(int position);
    }
}
