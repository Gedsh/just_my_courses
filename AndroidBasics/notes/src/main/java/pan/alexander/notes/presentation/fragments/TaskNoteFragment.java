package pan.alexander.notes.presentation.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pan.alexander.notes.App;
import pan.alexander.notes.databinding.TaskNoteFragmentBinding;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;
import pan.alexander.notes.domain.tasksnote.Task;
import pan.alexander.notes.presentation.recycler.tasks.OnStartDragListener;
import pan.alexander.notes.presentation.recycler.tasks.TaskItemTouchHelperCallback;
import pan.alexander.notes.presentation.recycler.tasks.TaskAdapter;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.presentation.viewmodel.TaskNoteViewModel;
import pan.alexander.notes.utils.Utils;

import static pan.alexander.notes.App.LOG_TAG;

public class TaskNoteFragment extends Fragment implements TaskAdapter.OnTasksChangedListener,
        OnStartDragListener {

    private static final int SLIDING_PANEL_ANIMATION_DURATION = 300;

    private MainActivityViewModel mainActivityViewModel;
    private TaskNoteViewModel taskNoteViewModel;
    private TaskNoteFragmentBinding binding;
    private TaskAdapter adapter;
    private Note receivedNote;
    private int receivedNotePosition;
    private Disposable disposable;
    private ItemTouchHelper itemTouchHelper;

    public static TaskNoteFragment getInstance() {
        return new TaskNoteFragment();
    }

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

        getCurrentNote();

        initRecycler();

        showNoteDetails();

        observeBottomNavigationViewShowed();

        observeKeyboardActivated();

        initOnItemTouchHelper();
    }

    private void getCurrentNote() {
        Pair<Integer, Note> currentNote = mainActivityViewModel.getCurrentNote();

        if (currentNote == null) {
            return;
        }

        receivedNotePosition = currentNote.first;
        receivedNote = currentNote.second;
    }

    private void initRecycler() {

        if (receivedNote == null) {
            return;
        }

        RecyclerView recyclerView = binding.recyclerViewTaskNote;
        int cardColor = Color.parseColor(receivedNote.getColor());
        adapter = new TaskAdapter(this, cardColor);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showNoteDetails() {

        if (receivedNote == null) {
            return;
        }

        int cardColor = Color.parseColor(receivedNote.getColor());

        binding.textViewTaskNoteDate.setText(Utils.formatTime(receivedNote.getTime(), true));
        binding.editTaskNoteTitle.setText(receivedNote.getTitle());
        binding.cardTaskNoteTitle.setCardBackgroundColor(cardColor);

        disposable = Single.fromCallable(() -> fromJsonToList(receivedNote.getDescription()))
                .subscribeOn(Schedulers.io())
                .delay(SLIDING_PANEL_ANIMATION_DURATION, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> adapter.refreshTasks(tasks, this));
    }

    @NonNull
    private List<Task> fromJsonToList(String description) {

        if (description == null || description.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Collection<Task>>() {
            }.getType();
            List<Task> savedTasks = gson.fromJson(description, type);

            if (savedTasks == null) {
                throw new IllegalArgumentException("Cannot convert json to class " + description);
            }

            return savedTasks;
        } catch (Exception e) {
            Log.e(LOG_TAG, "TaskNoteFragment fromJsonToList exception:", e);
        }

        return Collections.emptyList();
    }

    @Nullable
    private String fromListToGson(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "";
        }

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Collection<Task>>() {
            }.getType();
            return gson.toJson(tasks, type);
        } catch (Exception e) {
            Log.e(LOG_TAG, "TaskNoteFragment fromListToJson exception:", e);
        }

        return null;
    }

    private void observeBottomNavigationViewShowed() {
        mainActivityViewModel.getBottomNavigationViewShowed().observe(getViewLifecycleOwner(),
                this::setRecycleViewBottomPadding);
    }

    private void setRecycleViewBottomPadding(int appBarHeight) {
        RecyclerView recyclerView = binding.recyclerViewTaskNote;
        recyclerView.setPadding(0, 0, 0, appBarHeight);
    }

    private void observeKeyboardActivated() {
        mainActivityViewModel.getKeyboardActivated().observe(getViewLifecycleOwner(), active -> {
            if (!active) {
                saveChanges();
            }
        });
    }

    private void initOnItemTouchHelper() {
        ItemTouchHelper.Callback callback = new TaskItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTaskNote);
    }

    @Override
    public void onTasksChanged() {
        saveChanges();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (requireActivity().isChangingConfigurations()) {
            saveChanges();
        }
    }

    private void saveChanges() {

        if (receivedNote == null || adapter.getTasks() == null
                || mainActivityViewModel.getCurrentNote() == null
                || receivedNote.getId() != mainActivityViewModel.getCurrentNote().second.getId()) {
            return;
        }

        Editable title = binding.editTaskNoteTitle.getText();
        String description = fromListToGson(adapter.getTasks());
        int color = binding.cardTaskNoteTitle.getCardBackgroundColor().getDefaultColor();
        String colorHex = Utils.colorIntToHex(color);

        if (title == null || description == null) {
            return;
        }

        String titleText = title.toString().trim();
        if (titleText.isEmpty() && !description.isEmpty()) {
            titleText = generateTitle();
        }

        Note note = new Note(titleText,
                description.trim(),
                NoteType.LIST_NOTE,
                colorHex);
        note.setId(receivedNote.getId());

        mainActivityViewModel.setDisplayedNoteCallbackLiveData(new Pair<>(receivedNotePosition, note));
    }

    private String generateTitle() {
        Date currentDate = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return dateFormat.format(currentDate);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroyView() {
        removeEmptyNoteIfNecessary();

        super.onDestroyView();

        if (disposable != null) {
            disposable.dispose();
        }
        disposable = null;
        binding = null;
        adapter = null;
        itemTouchHelper = null;
    }

    private void removeEmptyNoteIfNecessary() {

        if (requireActivity().isChangingConfigurations()) {
            return;
        }

        if (receivedNote == null || adapter.getTasks() == null) {
            return;
        }

        Editable title = binding.editTaskNoteTitle.getText();
        String description = fromListToGson(adapter.getTasks());

        if (title == null || description == null) {
            return;
        }

        String titleText = title.toString().trim();
        String descriptionText = description.trim();
        if (titleText.isEmpty() && descriptionText.isEmpty()) {
            App.getInstance().getDaggerComponent().getMainInteractor().get()
                    .removeEmptyNotesFromNotesExceptLast();
        }
    }

}
