package pan.alexander.notes.presentation.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
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
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;
import pan.alexander.notes.App;
import pan.alexander.notes.R;
import pan.alexander.notes.databinding.NotesFragmentBinding;
import pan.alexander.notes.domain.MainInteractor;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;
import pan.alexander.notes.presentation.activities.ActionModeCallback;
import pan.alexander.notes.presentation.animation.ViewAnimation;
import pan.alexander.notes.presentation.pains.TwoPaneOnBackPressedCallback;
import pan.alexander.notes.presentation.recycler.notes.NotesAdapter;
import pan.alexander.notes.presentation.recycler.notes.NotesViewHolder;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.NotesViewModel;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.presentation.viewmodel.MainActivityViewModel.DEFAULT_BOTTOM_NAVIGATION_VIEW_HEIGHT;

public class NotesFragment extends Fragment implements NotesViewHolder.ClickListener,
        NotesAdapter.OnTopItemChangedListener,
        ActionModeCallback.ActionModeFinishedListener {

    public static final int NEW_NOTE_POSITION = 0;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MainActivityViewModel mainActivityViewModel;
    private NotesViewModel notesViewModel;
    private NotesFragmentBinding binding;
    private NotesAdapter notesAdapter;
    private ActionMode actionMode;
    private boolean fabIsRotate;
    private Menu menu;
    private TwoPaneOnBackPressedCallback twoPaneOnBackPressedCallback;
    private Lazy<MainInteractor> mainInteractor;
    private String defaultNoteColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotesFragmentBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.notes_action_bar_menu, menu);
        initSearch(menu);
    }

    private void initSearch(Menu menu) {
        MenuItem menuSearch = menu.findItem(R.id.mainMenuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(requireContext(), "Text submit", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(requireContext(), "Text change", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void toggleMenuWhenNoteDetailsDisplayed(boolean showFullMenu) {
        if (menu != null) {
            menu.findItem(R.id.mainMenuColor).setVisible(showFullMenu);
            menu.findItem(R.id.mainMenuView).setVisible(showFullMenu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mainMenuColor) {
            Toast.makeText(requireContext(), "Select color", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mainMenuView) {
            Toast.makeText(requireContext(), "Select view mode", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mainMenuSync) {
            Toast.makeText(requireContext(), "Sync", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();
        defaultNoteColor = Utils.colorIntToHex(ContextCompat.getColor(requireContext(),
                R.color.default_note_color));

        setBottomNavigationViewInvisible();

        initRecycler();

        initSlidingPanelOnGlobalLayoutListener();

        initBottomAppBar();

        initFloatingButton();

        setFloatingButtonClickListener();

    }

    private void initSlidingPanelOnGlobalLayoutListener() {
        SlidingPaneLayout slidingPaneLayout = binding.notesSlidingPaneLayout;

        slidingPaneLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                slidingPaneLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                toggleViewDependOnSlidingPanelIsSlidable();
                toggleConnectionSlidingPanelToBackButton();
                showCurrentNoteDetailsIfSlidingPanelIsSlidable();
                initSlidingPanelSlideListener();
                setRecycleViewPaddingDependOnTopTextView();
                observeNotesChanges();
                observeKeyboardActivated();
                observeCurrentNoteChanges();
            }
        });
    }

    private void initSlidingPanelSlideListener() {
        binding.notesSlidingPaneLayout.addPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {
                //Stub
            }

            @Override
            public void onPanelOpened(@NonNull View panel) {
                showBottomNavigationView();
                ViewAnimation.fabLayoutHide(binding.fabParentLayout);
                binding.notesSlidingPaneLayout.requestLayout();
                toggleMenuWhenNoteDetailsDisplayed(false);
            }

            @Override
            public void onPanelClosed(@NonNull View panel) {
                hideBottomNavigationView(false);
                ViewAnimation.fabLayoutShow(binding.fabParentLayout);
                toggleMenuWhenNoteDetailsDisplayed(true);
                mainActivityViewModel.clearCurrentNote();
            }
        });
    }

    private void toggleConnectionSlidingPanelToBackButton() {
        if (binding.notesSlidingPaneLayout.isSlideable()) {
            connectSlidingPanelToBackButton();
        } else {
            disconnectSlidingPanelToBackButton();
        }
    }

    private void showCurrentNoteDetailsIfSlidingPanelIsSlidable() {

        if (notesAdapter.getSelectedItemCount() == 0
                && mainActivityViewModel.getCurrentNote() != null
                && binding.notesSlidingPaneLayout.isOpen()
                && binding.notesSlidingPaneLayout.isSlideable()) {
            binding.notesSlidingPaneLayout.openPane();
            if (twoPaneOnBackPressedCallback != null) {
                twoPaneOnBackPressedCallback.onPanelOpened(binding.notesSlidingPaneLayout);
            }
        }
    }

    private void toggleViewDependOnSlidingPanelIsSlidable() {
        if (!binding.notesSlidingPaneLayout.isOpen()) {
            return;
        }

        if (binding.notesSlidingPaneLayout.isSlideable()) {
            showBottomNavigationView();
            ViewAnimation.fabLayoutHide(binding.fabParentLayout);
            toggleMenuWhenNoteDetailsDisplayed(false);
        } else {
            hideBottomNavigationView(false);
            ViewAnimation.fabLayoutShow(binding.fabParentLayout);
            toggleMenuWhenNoteDetailsDisplayed(true);
        }
        binding.notesSlidingPaneLayout.requestLayout();
    }


    private void connectSlidingPanelToBackButton() {
        twoPaneOnBackPressedCallback = new TwoPaneOnBackPressedCallback(binding.notesSlidingPaneLayout);
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(), twoPaneOnBackPressedCallback);
    }

    private void disconnectSlidingPanelToBackButton() {
        if (twoPaneOnBackPressedCallback != null) {
            twoPaneOnBackPressedCallback.remove();
        }
    }

    private void initBottomAppBar() {
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bottomNavigationView.setTranslationY(bottomNavigationView.getHeight());
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item
                -> handleBottomNavigationMenuClicks(item.getItemId()));
    }

    private boolean handleBottomNavigationMenuClicks(int menuId) {

        if (menuId == R.id.bottomMenuArchive) {
            Toast.makeText(NotesFragment.this.getContext(), "To archive", Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuId == R.id.bottomMenuDelete) {
            if (actionMode != null) {
                removeSelectedItems(notesViewModel.getSelectedNotesIndices());
            } else if (mainActivityViewModel.getCurrentNote() != null) {
                removeCurrentItem(mainActivityViewModel.getCurrentNote().first);
            }
            return true;
        } else if (menuId == R.id.bottomMenuColor) {
            Toast.makeText(NotesFragment.this.getContext(), "Change color", Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuId == R.id.bottomMenuRemind) {
            Toast.makeText(NotesFragment.this.getContext(), "Remind", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void removeSelectedItems(SparseBooleanArray selectedNotes) {

        if (selectedNotes.size() == 1) {
            notesAdapter.removeItem(selectedNotes.keyAt(0));
        } else if (selectedNotes.size() > 1) {
            List<Integer> items = new ArrayList<>(selectedNotes.size());
            for (int i = 0; i < selectedNotes.size(); i++) {
                items.add(selectedNotes.keyAt(i));
            }
            notesAdapter.removeItems(items);
        }

        mainActivityViewModel.clearCurrentNote();

        actionMode.finish();

        int firstVisibleItemPosition = RecyclerView.NO_POSITION;
        if (binding.recyclerViewNotes.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) binding.recyclerViewNotes.getLayoutManager())
                    .findFirstVisibleItemPosition();
        }

        if (!binding.notesSlidingPaneLayout.isSlideable() && firstVisibleItemPosition >= 0) {
            showNoteDetails(firstVisibleItemPosition);
        }
    }

    private void removeCurrentItem(int currentNotePosition) {

        if (mainActivityViewModel.getCurrentNote() == null
                || notesAdapter.getNotes().get(currentNotePosition).getId()
                != mainActivityViewModel.getCurrentNote().second.getId()) {
            return;
        }

        notesAdapter.removeItem(currentNotePosition);
        binding.notesSlidingPaneLayout.close();
        mainActivityViewModel.clearCurrentNote();
    }

    private void initFloatingButton() {
        ViewAnimation.fabInit(binding.fabAddTextNote);
        ViewAnimation.fabInit(binding.fabAddListNote);
    }

    private void setFloatingButtonClickListener() {
        binding.fabAddNote.setOnClickListener(this::toggleFab);

        binding.fabAddTextNote.setOnClickListener(v -> {
            Note note = new Note("", "", NoteType.TEXT_NOTE, defaultNoteColor);
            mainInteractor.get().addNoteToNotes(note);
            toggleFab(binding.fabAddNote);
        });

        binding.fabAddListNote.setOnClickListener(v -> {
            Note note = new Note("", "", NoteType.LIST_NOTE, defaultNoteColor);
            mainInteractor.get().addNoteToNotes(note);
            toggleFab(binding.fabAddNote);
        });
    }

    private void toggleFab(View view) {
        fabIsRotate = ViewAnimation.fabRotate(view, !fabIsRotate);

        if (fabIsRotate) {
            ViewAnimation.fabShowIn(binding.fabAddTextNote);
            ViewAnimation.fabShowIn(binding.fabAddListNote);
        } else {
            ViewAnimation.fabShowOut(binding.fabAddTextNote);
            ViewAnimation.fabShowOut(binding.fabAddListNote);
        }
    }

    private void closeFabIfRequired(View view) {
        if (fabIsRotate) {
            fabIsRotate = ViewAnimation.fabRotate(view, false);
            ViewAnimation.fabShowOut(binding.fabAddTextNote);
            ViewAnimation.fabShowOut(binding.fabAddListNote);
        }
    }

    private void initRecycler() {
        SparseBooleanArray selectedNotes = notesViewModel.getSelectedNotesIndices();
        RecyclerView notesRecycleView = binding.recyclerViewNotes;
        notesAdapter = new NotesAdapter(selectedNotes, this);
        notesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecycleView.setAdapter(notesAdapter);
    }

    private void setRecycleViewPaddingDependOnTopTextView() {
        TextView textViewTopNoteDate = binding.textViewTopNoteDate;
        binding.recyclerViewNotes.setPadding(0, textViewTopNoteDate.getHeight(), 0, 0);
    }

    private void observeNotesChanges() {
        Observer<List<Note>> notesObserver = notes -> {
            Collections.sort(notes);

            int oldNotesSize = notesAdapter.getNotes().size();
            notesAdapter.refreshNotesList(notes);
            int newNotesSize = notesAdapter.getNotes().size();

            if (newNotesSize - oldNotesSize == 1) {
                noteAdded();
            } else if (mainActivityViewModel.getCurrentNote() != null) {
                updateCurrentNotePosition(mainActivityViewModel.getCurrentNote().second);
            }

            if (newNotesSize == 0
                    || newNotesSize == 1 && notes.get(0).getTitle().isEmpty()
                    && notes.get(0).getDescription().isEmpty()) {
                binding.textViewTopNoteDate.setText("");
            }

            SparseBooleanArray selectedNotes = notesViewModel.getSelectedNotesIndices();
            if (selectedNotes.size() > 0) {
                activateActionMode(selectedNotes.size(), notes.size());
            } else {
                showFirstNoteDetailsIfSlidingPanelIsNotSlidable();
            }
        };

        LiveData<List<Note>> notesLiveData = notesViewModel.getNotesLiveData();
        notesLiveData.observe(getViewLifecycleOwner(), notesObserver);
    }

    private void noteAdded() {
        showNoteDetails(NEW_NOTE_POSITION);
        String time = Utils.formatTime(notesAdapter.getNotes().get(NEW_NOTE_POSITION).getTime(), false);
        binding.textViewTopNoteDate.setText(time);
        binding.recyclerViewNotes.smoothScrollToPosition(NEW_NOTE_POSITION);
    }

    private void updateCurrentNotePosition(Note currentNote) {
        int newPosition = notesAdapter.getNotes().indexOf(currentNote);
        if (newPosition >= 0) {
            mainActivityViewModel.setCurrentNote(newPosition, currentNote);
        }
    }

    private void showFirstNoteDetailsIfSlidingPanelIsNotSlidable() {
        if (!binding.notesSlidingPaneLayout.isSlideable()
                && mainActivityViewModel.getCurrentNote() == null
                && !notesAdapter.getNotes().isEmpty()) {
            showNoteDetails(0);
        }
    }

    @Override
    public void onTopItemChanged(Note note) {
        showTopNoteDate(note);
    }

    private void showTopNoteDate(Note note) {
        binding.textViewTopNoteDate.setText(Utils.formatTime(note.getTime(), false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        compositeDisposable.clear();
        binding = null;
        notesAdapter = null;
        menu = null;
        twoPaneOnBackPressedCallback = null;
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode == null) {
            showNoteDetails(position);
        } else {
            toggleSelection(position);
        }
    }

    private void showNoteDetails(int position) {

        Fragment fragment;
        if (notesAdapter.getNotes().isEmpty()) {
            fragment = TextNoteFragment.getInstance();
        } else {
            Note note = notesAdapter.getNotes().get(position);

            mainActivityViewModel.setCurrentNote(position, note);

            fragment = createFragmentDependOnNoteType(note.getType());
        }

        FragmentTransaction ft = getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.selectedNoteDetails, fragment);
        if (binding.notesSlidingPaneLayout.isOpen()) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        ft.commit();
        binding.notesSlidingPaneLayout.open();
        closeFabIfRequired(binding.fabAddNote);
    }

    private Fragment createFragmentDependOnNoteType(@NoteType int noteType) {
        switch (noteType) {
            case NoteType.TEXT_NOTE:
                return TextNoteFragment.getInstance();
            case NoteType.LIST_NOTE:
                return TaskNoteFragment.getInstance();
            default:
                throw new IllegalArgumentException("NotesFragment: unable to create fragment for undefined note type");
        }
    }

    @Override
    public void onItemLongClicked(int position) {
        if (actionMode == null) {
            initActionMode();
        }

        toggleSelection(position);
    }

    private void initActionMode() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        actionMode = appCompatActivity.startSupportActionMode(new ActionModeCallback(this));

        showBottomNavigationView();
    }

    private void showBottomNavigationView() {

        if (isBottomNavigationViewVisible()) {
            return;
        }

        ViewAnimation.bottomNavigationShow(binding.bottomNavigationView);
        ViewAnimation.fabParentLayoutUp(binding.fabParentLayout, binding.bottomNavigationView.getHeight());
        binding.recyclerViewNotes.setPadding(0, binding.recyclerViewNotes.getPaddingTop(), 0, binding.bottomNavigationView.getHeight());
        mainActivityViewModel.setBottomNavigationViewShowed(binding.bottomNavigationView.getHeight());
    }

    private boolean isBottomNavigationViewVisible() {
        LiveData<Integer> bottomNavigationViewShowed = mainActivityViewModel.getBottomNavigationViewShowed();
        return bottomNavigationViewShowed.getValue() != null
                && bottomNavigationViewShowed.getValue() > 0;
    }

    private void setBottomNavigationViewInvisible() {
        mainActivityViewModel.setBottomNavigationViewShowed(DEFAULT_BOTTOM_NAVIGATION_VIEW_HEIGHT);
    }

    private void toggleSelection(int position) {
        notesAdapter.toggleSelection(position);
        int selectedItemsCount = notesAdapter.getSelectedItemCount();
        int totalItemsCount = notesAdapter.getItemCount();

        if (selectedItemsCount == 0) {
            actionMode.finish();
        } else {
            activateActionMode(selectedItemsCount, totalItemsCount);
        }
    }

    private void activateActionMode(int selectedItemsCount, int totalItemsCount) {
        if (actionMode == null) {
            initActionMode();
        }

        actionMode.setTitle(String.format(Locale.getDefault(), "%d/%d", selectedItemsCount, totalItemsCount));
        actionMode.invalidate();
    }

    @Override
    public void onActionModeFinished() {
        hideBottomNavigationView(false);
        notesAdapter.clearSelection();
        actionMode = null;
    }

    private void hideBottomNavigationView(boolean hideImmediately) {
        if (!isBottomNavigationViewVisible()) {
            return;
        }

        ViewAnimation.bottomNavigationHide(binding.bottomNavigationView, hideImmediately);
        ViewAnimation.fabParentLayoutDown(binding.fabParentLayout, binding.bottomNavigationView.getHeight());
        binding.recyclerViewNotes.setPadding(0, binding.recyclerViewNotes.getPaddingTop(), 0, 0);
        mainActivityViewModel.setBottomNavigationViewShowed(0);
    }

    private void observeKeyboardActivated() {
        mainActivityViewModel.getKeyboardActivated().observe(getViewLifecycleOwner(), activated -> {

            if (!binding.notesSlidingPaneLayout.isOpen()) {
                return;
            }

            if (!activated) {
                binding.selectedNoteDetails.requestFocus();
            }

            if (!binding.notesSlidingPaneLayout.isSlideable()) {
                return;
            }

            if (activated) {
                hideBottomNavigationView(true);
            } else {
                showBottomNavigationView();

            }
        });
    }

    private void observeCurrentNoteChanges() {
        mainActivityViewModel.getDisplayedNoteCallbackLiveData().observe(getViewLifecycleOwner(), data -> {

            Pair<Integer, Note> displayedIndexToNote = mainActivityViewModel.getCurrentNote();

            if (data == null || displayedIndexToNote == null) {
                return;
            }

            int displayedNoteIndex = displayedIndexToNote.first;
            int updatedNoteIndex = data.first;

            if (updatedNoteIndex >= notesAdapter.getNotes().size()
                    || displayedNoteIndex != updatedNoteIndex) {
                return;
            }

            Note displayedNote = displayedIndexToNote.second;
            Note updatedNote = data.second;

            if (displayedNote.getId() != updatedNote.getId()
                    || displayedNote.getTitle().equals(updatedNote.getTitle())
                    && displayedNote.getDescription().equals(updatedNote.getDescription())
                    && displayedNote.getColor().toLowerCase(Locale.ENGLISH)
                    .equals(updatedNote.getColor().toLowerCase(Locale.ENGLISH))) {
                return;
            }

            mainActivityViewModel.setCurrentNote(displayedNoteIndex, updatedNote);
            notesAdapter.updateItem(displayedNoteIndex, updatedNote);
        });
    }
}
