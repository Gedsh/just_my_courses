package pan.alexander.notes.presentation.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.fragments.NotesFragment;

public class NotesAdapter extends SelectableAdapter<NotesViewHolder>{
    private final Context context;
    final List<Note> notes = new ArrayList<>();

    final NotesViewHolder.ClickListener clickListener;
    final OnTopItemChangedListener topItemChangedListener;
    Drawable icTextNote;
    Drawable icListNote;

    public NotesAdapter(SparseBooleanArray selectedItems, NotesFragment notesFragment) {
        super(selectedItems);

        this.context = notesFragment.requireContext();
        this.clickListener = notesFragment;
        this.topItemChangedListener = notesFragment;

        getRequiredDrawables();
    }

    private void getRequiredDrawables() {
        icTextNote = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_text_note, context.getTheme());
        icListNote = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_list_note, context.getTheme());
    }

    public void refreshNotesList(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        Collections.sort(this.notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_notes, parent, false);
        return new NotesViewHolder(view, this);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        observeScrollEvents(recyclerView, linearLayoutManager);
    }

    private void observeScrollEvents(@NonNull RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (visibleItemPosition >= 0) {
                    topItemChangedListener.onTopItemChanged(notes.get(visibleItemPosition));
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public interface OnTopItemChangedListener {
        void onTopItemChanged(Note note);
    }
}
