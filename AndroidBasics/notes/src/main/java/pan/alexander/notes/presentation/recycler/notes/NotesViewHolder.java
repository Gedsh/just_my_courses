package pan.alexander.notes.presentation.recycler.notes;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;
import pan.alexander.notes.utils.Utils;

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {
    private final NotesAdapter notesAdapter;
    private final CardView cardView;
    private final TextView textViewNoteTitle;
    private final TextView textViewNoteDate;
    private final ImageView imageSubmenu;

    public NotesViewHolder(@NonNull View itemView, NotesAdapter notesAdapter) {
        super(itemView);

        this.notesAdapter = notesAdapter;

        cardView = itemView.findViewById(R.id.cardNotesItem);
        cardView.setOnClickListener(this);
        cardView.setOnLongClickListener(this);
        textViewNoteTitle = itemView.findViewById(R.id.textViewNotesItemNoteTitle);
        textViewNoteDate = itemView.findViewById(R.id.textViewNotesItemNoteDate);
        imageSubmenu = itemView.findViewById(R.id.imageViewItemNoteSubmenu);
        imageSubmenu.setOnClickListener(this);
    }

    public void bind(int position) {

        if (position < 0) {
            return;
        }

        Note note = notesAdapter.getNotes().get(position);

        setCardText(note);

        setCardColorStateList(note);

        setNoteTypeDrawable(note);

        cardView.setSelected(notesAdapter.isSelected(position));

    }

    private void setCardText(Note note) {
        if (note.getTitle().isEmpty()
                && note.getDescription().isEmpty()
                && notesAdapter.getItemCount() > 1) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            textViewNoteTitle.setText(note.getTitle());
            textViewNoteDate.setText(Utils.formatTime(note.getTime(), false));
        }
    }

    private void setCardColorStateList(Note note) {
        int color = Color.parseColor(note.getColor());
        cardView.setCardBackgroundColor(Utils.calculateColorStateList(color));
    }

    private void setNoteTypeDrawable(Note note) {
        switch (note.getType()) {
            case NoteType.TEXT_NOTE:
            default:
                imageSubmenu.setImageDrawable(notesAdapter.getIcTextNote());
                break;
            case NoteType.LIST_NOTE:
                imageSubmenu.setImageDrawable(notesAdapter.getIcListNote());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) {
            return;
        }

        if (notesAdapter.getClickListener() == null) {
            return;
        }

        int id = v.getId();

        if (id == R.id.cardNotesItem) {
            notesAdapter.getClickListener().onItemClicked(position);
        } else if (id == R.id.imageViewItemNoteSubmenu) {
            notesAdapter.getClickListener().onItemLongClicked(position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) {
            return false;
        }

        if (v.getId() == R.id.cardNotesItem && notesAdapter.getClickListener() != null) {
            notesAdapter.getClickListener().onItemLongClicked(position);
        }

        return false;
    }

    public interface ClickListener {
        void onItemClicked(int position);

        void onItemLongClicked(int position);
    }
}
