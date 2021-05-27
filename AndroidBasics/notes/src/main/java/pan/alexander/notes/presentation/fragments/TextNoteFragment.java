package pan.alexander.notes.presentation.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pan.alexander.notes.databinding.TextNoteFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.viewmodel.TextNoteViewModel;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.presentation.fragments.NotesFragment.NOTE_DETAILS_ARGUMENT;

public class TextNoteFragment extends Fragment {

    private TextNoteViewModel mViewModel;
    private TextNoteFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TextNoteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(TextNoteViewModel.class);
        showNoteDetailsFromArguments();
    }

    private void showNoteDetailsFromArguments() {
        Bundle arguments = getArguments();

        if (arguments == null) {
            return;
        }

        Note note = arguments.getParcelable(NOTE_DETAILS_ARGUMENT);

        if (note == null) {
            return;
        }

        binding.textViewTextNoteDate.setText(Utils.formatTime(note.getTime(), true));
        binding.editTextNote.setText(note.getDescription());
        binding.cardTextNote.setCardBackgroundColor(Color.parseColor(note.getColor()));
    }
}
