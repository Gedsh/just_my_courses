package pan.alexander.notes.presentation.recycler;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;

public class NotesAdapter extends SelectableAdapter<NotesViewHolder>{
    final List<Note> notes = new ArrayList<>();
    final MutableLiveData<Note> onBindLiveListener = new MutableLiveData<>();

    final NotesViewHolder.ClickListener clickListener;

    public NotesAdapter(SparseBooleanArray selectedItems, NotesViewHolder.ClickListener clickListener) {
        super(selectedItems);

        this.clickListener = clickListener;
    }

    public void refreshNotesList(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
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

                int visibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (visibleItemPosition >= 0) {
                    onBindLiveListener.setValue(notes.get(visibleItemPosition));
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

    public LiveData<Note> getNoteOnBindLiveListener() {
        return onBindLiveListener;
    }
}
