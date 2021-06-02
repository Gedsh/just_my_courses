package pan.alexander.notes.presentation.fragments;

import androidx.cardview.widget.CardView;
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
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.TextNoteViewModel;
import pan.alexander.notes.utils.KeyboardUtils;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.presentation.fragments.NotesFragment.NOTE_DETAILS_ARGUMENT;
import static pan.alexander.notes.utils.AppConstants.DEFAULT_ANIMATION_DURATION;

public class TextNoteFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;
    private TextNoteViewModel textNoteViewModel;
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

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        textNoteViewModel = new ViewModelProvider(this).get(TextNoteViewModel.class);

        showNoteDetailsFromArguments();
        observeBottomNavigationViewShowed();
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
        binding.editTextNoteTitle.setText(note.getTitle());
        binding.cardTextNoteTitle.setCardBackgroundColor(Color.parseColor(note.getColor()));
        binding.editTextNoteDescription.setText(note.getDescription());
        binding.cardTextNoteDescription.setCardBackgroundColor(Color.parseColor(note.getColor()));
    }

    private void observeBottomNavigationViewShowed() {
        mainActivityViewModel.getBottomNavigationViewShowed().observe(getViewLifecycleOwner(),
                this::setDescriptionCardBottomMargin);
    }

    private void setDescriptionCardBottomMargin(int appBarHeight) {
        CardView cardTextNoteDescription = binding.cardTextNoteDescription;
        cardTextNoteDescription.postDelayed(() -> {
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) cardTextNoteDescription.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, appBarHeight);
            cardTextNoteDescription.requestLayout();
        }, appBarHeight != 0 ? DEFAULT_ANIMATION_DURATION : 0);
    }

}
