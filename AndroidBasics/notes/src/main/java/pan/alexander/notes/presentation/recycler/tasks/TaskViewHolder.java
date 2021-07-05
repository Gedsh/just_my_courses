package pan.alexander.notes.presentation.recycler.tasks;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Collections;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.tasksnote.Task;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        TextWatcher, ItemTouchHelperViewHolder, View.OnTouchListener {

    private static final char NEW_LINE_SYMBOL = '\n';
    private static final String NEW_LINE_SYMBOL_STRING = "\n";
    private static final int CARD_DRAG_ACTION_TRANSPARENCY = 215;
    private static final float ADD_TASK_BUTTON_DARKNESS_RATIO = .3f;

    private final TaskAdapter taskAdapter;
    private CardView cardTaskNoteItem;
    private AppCompatEditText editTextTaskNoteItem;
    private MaterialCheckBox checkBoxTaskNoteItem;

    @SuppressLint("ClickableViewAccessibility")
    public TaskViewHolder(@NonNull View itemView, int viewType, TaskAdapter taskAdapter) {
        super(itemView);
        this.taskAdapter = taskAdapter;

        if (viewType == R.layout.recycle_item_tasks) {
            cardTaskNoteItem = itemView.findViewById(R.id.cardTaskNoteItem);

            ImageView moveTaskNoteItem = itemView.findViewById(R.id.moveTaskNoteItem);
            moveTaskNoteItem.setOnTouchListener(this);

            editTextTaskNoteItem = itemView.findViewById(R.id.editTextTaskNoteItem);
            editTextTaskNoteItem.addTextChangedListener(this);

            ImageView deleteTaskNoteItem = itemView.findViewById(R.id.deleteTaskNoteItem);
            deleteTaskNoteItem.setOnClickListener(this);

            checkBoxTaskNoteItem = itemView.findViewById(R.id.checkBoxTaskNoteItem);
            checkBoxTaskNoteItem.setOnClickListener(this);
        } else {
            MaterialButton imageButtonAddTask = itemView.findViewById(R.id.imageButtonAddTask);
            imageButtonAddTask.setBackgroundColor(
                    ColorUtils.blendARGB(taskAdapter.getCardColor(),
                            Color.BLACK, ADD_TASK_BUTTON_DARKNESS_RATIO));
            imageButtonAddTask.setOnClickListener(this);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind(int position) {

        TaskAdapter.TaskItem taskItem = taskAdapter.getTaskItems().get(position);

        if (position == NO_POSITION || taskItem.buttonType) {
            return;
        }

        Task task = taskItem.task;

        cardTaskNoteItem.setCardBackgroundColor(taskAdapter.getCardColor());
        editTextTaskNoteItem.setText(task.getText());
        editTextTaskNoteItem.setEnabled(!task.isDone());
        checkBoxTaskNoteItem.setChecked(task.isDone());
    }

    @Override
    public void onClick(View v) {

        int position = getAdapterPosition();
        TaskAdapter.OnTasksChangedListener onTasksChangedListener = taskAdapter.getOnTasksChangedListener();
        if (position == NO_POSITION || onTasksChangedListener == null) {
            return;
        }

        int id = v.getId();

        if (id == R.id.deleteTaskNoteItem) {
            taskAdapter.removeItem(position);
            onTasksChangedListener.onTasksChanged();
        } else if (id == R.id.imageButtonAddTask) {
            taskAdapter.addTask("");
        } else if (id == R.id.checkBoxTaskNoteItem) {
            List<TaskAdapter.TaskItem> taskItems = taskAdapter.getTaskItems();
            TaskAdapter.TaskItem taskItem = taskItems.get(position);
            Task task = taskItem.task;
            task.setDone(!task.isDone());
            taskItem.task = task;
            taskItems.set(position, taskItem);
            Collections.sort(taskItems);
            taskAdapter.notifyItemChanged(position);
            taskAdapter.notifyItemMoved(position, taskAdapter.getTaskItems().lastIndexOf(taskItem));
            onTasksChangedListener.onTasksChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Stub
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before == 0 && count == 1 && s.charAt(start) == NEW_LINE_SYMBOL) {
            Editable text = editTextTaskNoteItem.getText();
            if (text != null) {
                text.replace(start, start + 1, "");
            }
            taskAdapter.addTask("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        int position = getAdapterPosition();
        if (position == NO_POSITION || s == null) {
            return;
        }

        String text = s.toString();
        String nextTaskText = "";

        if (text.contains(NEW_LINE_SYMBOL_STRING)) {
            int newLineSymbolIndex = text.indexOf(NEW_LINE_SYMBOL_STRING);
            nextTaskText = text.substring(newLineSymbolIndex + 1).trim();
            text = text.substring(0, newLineSymbolIndex).trim();
        }

        List<TaskAdapter.TaskItem> taskItems = taskAdapter.getTaskItems();
        TaskAdapter.TaskItem taskItem = taskItems.get(position);
        Task task = taskItem.task;
        task.setText(text);
        taskItem.task = task;
        taskItems.set(position, taskItem);

        if (!nextTaskText.isEmpty()) {
            editTextTaskNoteItem.setText(text);
            String finalNextTaskText = nextTaskText;
            editTextTaskNoteItem.post(() -> taskAdapter.addTask(finalNextTaskText));
        }
    }

    @Override
    public void onItemSelected(int actionState) {
        if (editTextTaskNoteItem.isFocused()) {
            return;
        }

        editTextTaskNoteItem.setTextIsSelectable(true);
        editTextTaskNoteItem.setTextIsSelectable(false);

        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
            cardTaskNoteItem.setCardBackgroundColor(ColorUtils.
                    setAlphaComponent(taskAdapter.getCardColor(), CARD_DRAG_ACTION_TRANSPARENCY));
        }
    }

    @Override
    public void onItemClear() {
        editTextTaskNoteItem.setTextIsSelectable(true);
        editTextTaskNoteItem.setFocusable(true);
        editTextTaskNoteItem.setClickable(true);
        cardTaskNoteItem.setCardBackgroundColor(taskAdapter.getCardColor());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            taskAdapter.getStartDragListener().onStartDrag(this);
        }
        return false;
    }
}
