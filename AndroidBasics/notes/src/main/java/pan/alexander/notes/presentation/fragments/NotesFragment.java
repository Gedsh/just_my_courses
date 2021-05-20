package pan.alexander.notes.presentation.fragments;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.databinding.NotesFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.recycler.NotesAdapter;
import pan.alexander.notes.presentation.viewmodel.NotesViewModel;

public class NotesFragment extends Fragment {

    public static final String NOTE_DETAILS_ARGUMENT = "NOTE_DETAILS_ARGUMENT";

    private NotesViewModel notesViewModel;
    private NotesFragmentBinding binding;
    private NotesAdapter notesAdapter;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        initRecycler();

        observeNotesChanges();

        observeRecyclerItemClick();
    }

    private void initRecycler() {
        RecyclerView notesRecycleView = binding.recyclerViewNotes;
        notesAdapter = new NotesAdapter();
        notesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecycleView.setAdapter(notesAdapter);
    }

    private void observeNotesChanges() {
        Observer<List<Note>> notesObserver = notes ->
                notesAdapter.refreshNotesList(notes);
        LiveData<List<Note>> notesLiveData = notesViewModel.getNotesLiveData();
        notesLiveData.observe(getViewLifecycleOwner(), notesObserver);
    }

    private void observeRecyclerItemClick() {
        Observer<Note> itemClickedObserver = this::showNoteDetails;
        LiveData<Note> onClickLiveListener = notesAdapter.getNoteOnClickLiveListener();
        onClickLiveListener.observe(getViewLifecycleOwner(), itemClickedObserver);
    }

    private void showNoteDetails(Note note) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(NOTE_DETAILS_ARGUMENT, note);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.selectedNoteDetails, TextNoteFragment.class, arguments);
        if (binding.notesSlidingPaneLayout.isOpen()) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        ft.commit();
        binding.notesSlidingPaneLayout.open();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
