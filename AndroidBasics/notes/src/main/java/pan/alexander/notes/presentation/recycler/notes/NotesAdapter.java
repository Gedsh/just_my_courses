package pan.alexander.notes.presentation.recycler.notes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

import dagger.Lazy;
import pan.alexander.notes.App;
import pan.alexander.notes.R;
import pan.alexander.notes.domain.MainInteractor;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.fragments.NotesFragment;

import static pan.alexander.notes.App.LOG_TAG;

public class NotesAdapter extends SelectableAdapter<NotesViewHolder> {
    private final Context context;
    private final List<Note> notes = new ArrayList<>();

    private final NotesViewHolder.ClickListener clickListener;
    private final OnTopItemChangedListener topItemChangedListener;
    private Drawable icTextNote;
    private Drawable icListNote;
    private final Lazy<MainInteractor> mainInteractor;

    public NotesAdapter(SparseBooleanArray selectedItems, NotesFragment notesFragment) {
        super(selectedItems);

        this.context = notesFragment.requireContext();
        this.clickListener = notesFragment;
        this.topItemChangedListener = notesFragment;

        mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();

        getRequiredDrawables();
    }

    public interface OnTopItemChangedListener {
        void onTopItemChanged(Note note);
    }

    private void getRequiredDrawables() {
        icTextNote = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_text_note, context.getTheme());
        icListNote = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_list_note, context.getTheme());
    }

    public void refreshNotesList(List<Note> notes) {

        if (notes.equals(this.notes)) {
            return;
        }

        if (!this.notes.isEmpty()) {
            this.notes.clear();
            Log.d(LOG_TAG, "Notes data set changed \n" + this.notes + "\n" + notes);
        }

        notifyDataSetChanged();

        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            this.notes.add(note);
            notifyItemInserted(i);
        }
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

    public void updateItem(int position, Note note) {
        notes.set(position, note);
        notifyItemChanged(position);
        mainInteractor.get().updateNoteInNotes(note);
    }

    public void removeItem(int position) {
        Note note = notes.remove(position);
        notifyItemRemoved(position);
        if (note.getTitle().isEmpty() && note.getDescription().isEmpty()) {
            mainInteractor.get().removeNoteFromNotes(note);
        } else {
            mainInteractor.get().moveNoteFromNotesToTrash(note);
        }

        removeLastEmptyItemIfNecessary();
    }

    public void removeItems(List<Integer> positions) {

        Collections.sort(positions, (lhs, rhs) -> rhs - lhs);

        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                if (count > 0) {
                    positions.subList(0, count).clear();
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        List<Note> removedNotes = new ArrayList<>();
        for (int i = 0; i < itemCount; ++i) {
            removedNotes.add(notes.remove(positionStart));
        }
        notifyItemRangeRemoved(positionStart, itemCount);
        mainInteractor.get().moveNotesFromNotesToTrash(removedNotes);

        removeLastEmptyItemIfNecessary();
    }

    private void removeLastEmptyItemIfNecessary() {
        if (getItemCount() == 1) {
            Note note = notes.get(0);
            if (note.getTitle().isEmpty() && note.getDescription().isEmpty()) {
                removeItem(0);
            }
        }
    }

    NotesViewHolder.ClickListener getClickListener() {
        return clickListener;
    }

    Drawable getIcTextNote() {
        return icTextNote;
    }

    Drawable getIcListNote() {
        return icListNote;
    }

}
