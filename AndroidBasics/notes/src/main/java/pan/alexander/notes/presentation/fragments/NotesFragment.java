package pan.alexander.notes.presentation.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

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

import java.util.List;
import java.util.Locale;

import pan.alexander.notes.R;
import pan.alexander.notes.databinding.NotesFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.activities.ActionModeCallback;
import pan.alexander.notes.presentation.animation.ViewAnimation;
import pan.alexander.notes.presentation.pains.TwoPaneOnBackPressedCallback;
import pan.alexander.notes.presentation.recycler.NotesAdapter;
import pan.alexander.notes.presentation.recycler.NotesViewHolder;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.NotesViewModel;
import pan.alexander.notes.utils.Utils;

public class NotesFragment extends Fragment implements NotesViewHolder.ClickListener,
        NotesAdapter.OnTopItemChangedListener,
        ActionModeCallback.ActionModeFinishedListener {

    public static final String NOTE_DETAILS_ARGUMENT = "NOTE_DETAILS_ARGUMENT";

    private MainActivityViewModel mainActivityViewModel;
    private NotesViewModel notesViewModel;
    private NotesFragmentBinding binding;
    private NotesAdapter notesAdapter;
    private ActionMode actionMode;
    private boolean fabIsRotate;
    private Menu menu;

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
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        initSlidingPanelSlideListener();

        connectSlidingPanelToBackButton();

        initBottomAppBar();

        initFloatingButton();

        setFloatingButtonClickListener();

        initRecycler();

        observeNotesChanges();

        observeKeyboardActivated();
    }

    private void initSlidingPanelSlideListener() {
        binding.notesSlidingPaneLayout.addPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {

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
            }
        });
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!binding.notesSlidingPaneLayout.isOpen()) {
            return;
        }

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new TwoPaneOnBackPressedCallback(binding.notesSlidingPaneLayout));
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
            Toast.makeText(NotesFragment.this.getContext(), "To trash can", Toast.LENGTH_SHORT).show();
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

    private void initFloatingButton() {
        ViewAnimation.fabInit(binding.fabAddTextNote);
        ViewAnimation.fabInit(binding.fabAddListNote);
    }

    private void setFloatingButtonClickListener() {
        binding.fabAddNote.setOnClickListener(view -> {
            fabIsRotate = ViewAnimation.fabRotate(view, !fabIsRotate);

            if (fabIsRotate) {
                ViewAnimation.fabShowIn(binding.fabAddTextNote);
                ViewAnimation.fabShowIn(binding.fabAddListNote);
            } else {
                ViewAnimation.fabShowOut(binding.fabAddTextNote);
                ViewAnimation.fabShowOut(binding.fabAddListNote);
            }
        });

        binding.fabAddTextNote.setOnClickListener(v ->
                Toast.makeText(getContext(), "fab add text note", Toast.LENGTH_SHORT).show());

        binding.fabAddListNote.setOnClickListener(v ->
                Toast.makeText(getContext(), "fab add list note", Toast.LENGTH_SHORT).show());
    }

    private void initRecycler() {
        SparseBooleanArray selectedNotes = notesViewModel.getSelectedNotesIndices();
        RecyclerView notesRecycleView = binding.recyclerViewNotes;
        notesAdapter = new NotesAdapter(selectedNotes, this);
        notesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecycleView.setAdapter(notesAdapter);

        TextView textViewTopNoteDate = binding.textViewTopNoteDate;
        textViewTopNoteDate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textViewTopNoteDate.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                notesRecycleView.setPadding(0, textViewTopNoteDate.getHeight(), 0, 0);
                notesRecycleView.scrollToPosition(0);
            }
        });
    }

    private void observeNotesChanges() {
        Observer<List<Note>> notesObserver = notes -> {
            notesAdapter.refreshNotesList(notes);

            SparseBooleanArray selectedNotes = notesViewModel.getSelectedNotesIndices();
            if (selectedNotes.size() > 0) {
                activateActionMode(selectedNotes.size(), notes.size());
            }
        };

        LiveData<List<Note>> notesLiveData = notesViewModel.getNotesLiveData();
        notesLiveData.observe(getViewLifecycleOwner(), notesObserver);
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

        binding = null;
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
        Bundle arguments = new Bundle();
        arguments.putParcelable(NOTE_DETAILS_ARGUMENT, notesAdapter.getNotes().get(position));
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
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (isBottomNavigationViewVisible()) {
                    return;
                }

                ViewAnimation.bottomNavigationShow(binding.bottomNavigationView);
                ViewAnimation.fabParentLayoutUp(binding.fabParentLayout, binding.bottomNavigationView.getHeight());
                binding.recyclerViewNotes.setPadding(0, binding.recyclerViewNotes.getPaddingTop(), 0, binding.bottomNavigationView.getHeight());
                mainActivityViewModel.setBottomNavigationViewShowed(binding.bottomNavigationView.getHeight());
            }
        });
    }

    private boolean isBottomNavigationViewVisible() {
        return binding.bottomNavigationView.getTranslationY() == 0;
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

            if (activated) {
                hideBottomNavigationView(true);
            } else {
                showBottomNavigationView();
            }
        });
    }
}
