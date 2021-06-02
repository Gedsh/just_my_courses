package pan.alexander.notes.presentation.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pan.alexander.notes.databinding.TaskNoteFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.tasksnote.Task;
import pan.alexander.notes.presentation.recycler.TaskAdapter;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.TaskNoteViewModel;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.App.LOG_TAG;
import static pan.alexander.notes.presentation.fragments.NotesFragment.NOTE_DETAILS_ARGUMENT;
import static pan.alexander.notes.utils.AppConstants.DEFAULT_ANIMATION_DURATION;

public class TaskNoteFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;
    private TaskNoteViewModel taskNoteViewModel;
    private TaskNoteFragmentBinding binding;
    private TaskAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TaskNoteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        taskNoteViewModel = new ViewModelProvider(this).get(TaskNoteViewModel.class);

        initRecycler();

        showNoteDetailsFromArguments();

        observeBottomNavigationViewShowed();
    }

    private void initRecycler() {
        RecyclerView recyclerView = binding.recyclerViewTaskNote;
        adapter = new TaskAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
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

        int cardColor = Color.parseColor(note.getColor());

        binding.textViewTaskNoteDate.setText(Utils.formatTime(note.getTime(), true));
        binding.editTaskNoteTitle.setText(note.getTitle());
        binding.cardTaskNoteTitle.setCardBackgroundColor(cardColor);
        adapter.refreshTasks(fromJsonToList(note.getDescription()), cardColor);
    }

    private List<Task> fromJsonToList(String description) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Collection<Task>>(){}.getType();
            List<Task> savedTasks = gson.fromJson(description, type);

            if (savedTasks == null) {
                throw new IllegalArgumentException("Cannot convert json to class " + description);
            }

            return savedTasks;
        } catch (Exception e) {
            Log.e(LOG_TAG, "TaskNoteFragment fromJsonToList exception: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private void observeBottomNavigationViewShowed() {
        mainActivityViewModel.getBottomNavigationViewShowed().observe(getViewLifecycleOwner(),
                this::setRecycleViewBottomPadding);
    }

    private void setRecycleViewBottomPadding(int appBarHeight) {
        RecyclerView recyclerView = binding.recyclerViewTaskNote;
        recyclerView.setPadding(0, 0, 0, appBarHeight);
    }
}
