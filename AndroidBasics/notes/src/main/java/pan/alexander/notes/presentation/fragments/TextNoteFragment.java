package pan.alexander.notes.presentation.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Date;

import pan.alexander.notes.databinding.TextNoteFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.viewmodel.TextNoteViewModel;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        binding.textViewTextNoteDate.setText(formatTime(note.getTime()));
        binding.editTextNote.setText(note.getDescription());
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        return dateFormat.format(date);
    }

}
