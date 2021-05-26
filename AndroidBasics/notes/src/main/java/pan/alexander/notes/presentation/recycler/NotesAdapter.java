package pan.alexander.notes.presentation.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.entities.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    private final List<Note> notes = new ArrayList<>();
    private final MutableLiveData<Note> onClickLiveListener = new MutableLiveData<>();

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
        return new NotesViewHolder(view, onClickLiveListener, notes);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public LiveData<Note> getNoteOnClickLiveListener() {
        return onClickLiveListener;
    }
}
