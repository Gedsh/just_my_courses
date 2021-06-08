package pan.alexander.notes.presentation.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import pan.alexander.notes.App;
import pan.alexander.notes.R;
import pan.alexander.notes.databinding.TextNoteFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.TextNoteViewModel;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.utils.AppConstants.DEFAULT_ANIMATION_DURATION;

public class TextNoteFragment extends Fragment implements View.OnLongClickListener {

    private static final int WORDS_TO_GENERATE_TITLE = 3;
    private static final int DELAY_BEFORE_OPEN_KEYBOARD = 600;

    private MainActivityViewModel mainActivityViewModel;
    private TextNoteViewModel textNoteViewModel;
    private TextNoteFragmentBinding binding;
    private Note receivedNote;
    private int receivedNotePosition;
    private boolean keyboardIsVisible;

    public static TextNoteFragment getInstance() {
        return new TextNoteFragment();
    }

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

        showNoteDetails(view);
        observeBottomNavigationViewShowed();
        observeKeyboardActivated();
        requestFocusIfDescriptionEmpty();
    }

    private void requestFocusIfDescriptionEmpty() {

        if (receivedNote == null || !receivedNote.getDescription().isEmpty()) {
            return;
        }

        EditText editTextNoteDescription = binding.editTextNoteDescription;
        editTextNoteDescription.postDelayed(() -> {
            if (isResumed()) {
                editTextNoteDescription.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

        }, DELAY_BEFORE_OPEN_KEYBOARD);

    }

    private void showNoteDetails(View view) {

        Pair<Integer, Note> currentNote = mainActivityViewModel.getCurrentNote();

        if (currentNote == null) {
            view.setVisibility(View.INVISIBLE);
            return;
        }

        receivedNotePosition = currentNote.first;
        receivedNote = currentNote.second;

        if (receivedNote == null) {
            return;
        }

        binding.textViewTextNoteDate.setText(Utils.formatTime(receivedNote.getTime(), true));
        binding.editTextNoteTitle.setText(receivedNote.getTitle());
        binding.cardTextNoteTitle.setCardBackgroundColor(Color.parseColor(receivedNote.getColor()));
        binding.editTextNoteDescription.setText(receivedNote.getDescription());
        binding.editTextNoteDescription.setOnLongClickListener(this);
        binding.cardTextNoteDescription.setCardBackgroundColor(Color.parseColor(receivedNote.getColor()));
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

    private void observeKeyboardActivated() {
        mainActivityViewModel.getKeyboardActivated().observe(getViewLifecycleOwner(), active -> {
            keyboardIsVisible = active;
            if (!active) {
                saveChanges();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (requireActivity().isChangingConfigurations()) {
            saveChanges();
        }
    }

    private void saveChanges() {

        if (receivedNote == null || mainActivityViewModel.getCurrentNote() == null
                || receivedNote.getId() != mainActivityViewModel.getCurrentNote().second.getId()) {
            return;
        }

        Editable title = binding.editTextNoteTitle.getText();
        Editable description = binding.editTextNoteDescription.getText();
        int color = binding.cardTextNoteDescription.getCardBackgroundColor().getDefaultColor();
        String colorHex = Utils.colorIntToHex(color);

        if (title == null || description == null) {
            return;
        }

        String titleText = title.toString().trim();
        String descriptionText = description.toString().trim();
        if (titleText.isEmpty() && !descriptionText.isEmpty()) {
            titleText = generateTitle(descriptionText);
        }

        Note note = new Note(titleText,
                descriptionText,
                NoteType.TEXT_NOTE,
                colorHex);
        note.setId(receivedNote.getId());

        mainActivityViewModel.setDisplayedNoteCallbackLiveData(new Pair<>(receivedNotePosition, note));
    }

    private String generateTitle(String description) {

        String[] words = description.split("\\s");
        if (words.length < WORDS_TO_GENERATE_TITLE) {
            return description;
        } else {
            StringBuilder title = new StringBuilder();
            for (int i = 0; i < WORDS_TO_GENERATE_TITLE; i++) {
                title.append(words[i]).append(" ");
            }
            return title.toString().trim();
        }
    }

    @Override
    public void onDestroyView() {
        removeEmptyNoteIfNecessary();

        super.onDestroyView();

        binding = null;
    }

    private void removeEmptyNoteIfNecessary() {

        if (requireActivity().isChangingConfigurations()) {
            return;
        }

        if (receivedNote == null) {
            return;
        }

        Editable title = binding.editTextNoteTitle.getText();
        Editable description = binding.editTextNoteDescription.getText();

        if (title == null || description == null) {
            return;
        }

        String titleText = title.toString().trim();
        String descriptionText = description.toString().trim();
        if (titleText.isEmpty() && descriptionText.isEmpty()) {
            App.getInstance().getDaggerComponent().getMainInteractor().get().removeEmptyNotesFromNotesExceptLast();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.editTextNoteDescription) {
            return !keyboardIsVisible;
        }
        return false;
    }
}
