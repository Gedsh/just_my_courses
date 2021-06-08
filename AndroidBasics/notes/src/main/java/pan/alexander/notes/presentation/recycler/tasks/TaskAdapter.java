package pan.alexander.notes.presentation.recycler.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import pan.alexander.notes.App;
import pan.alexander.notes.R;
import pan.alexander.notes.domain.tasksnote.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> implements ItemTouchHelperAdapter {
    private final List<TaskItem> taskItems = new ArrayList<>();
    private final int cardColor;
    private OnTasksChangedListener onTasksChangedListener;
    private final OnStartDragListener startDragListener;

    public TaskAdapter(OnStartDragListener startDragListener, int cardColor) {
        this.startDragListener = startDragListener;
        this.cardColor = cardColor;
        taskItems.add(createButtonItem());
    }

    public interface OnTasksChangedListener {
        void onTasksChanged();
    }

    public void refreshTasks(List<Task> tasks, OnTasksChangedListener listener) {
        taskItems.clear();
        notifyDataSetChanged();

        List<TaskItem> newItems = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            newItems.add(new TaskItem(tasks.get(i)));
        }
        newItems.add(createButtonItem());

        Collections.sort(newItems);

        for (int i = 0; i < newItems.size(); i++) {
            taskItems.add(newItems.get(i));
            notifyItemInserted(i);
        }

        if (newItems.size() == 1) {
            addTask("");
        }

        onTasksChangedListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (taskItems.get(position).buttonType) ? R.layout.recycle_item_add_task : R.layout.recycle_item_tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == R.layout.recycle_item_tasks) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_tasks, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_add_task, parent, false);
        }

        return new TaskViewHolder(view, viewType, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(position);
        requestFocusForNewTask(holder, position);
    }

    private void requestFocusForNewTask(TaskViewHolder holder, int position) {

        int buttonPosition = getButtonPosition();
        if (buttonPosition < 0 || position != buttonPosition - 1) {
            return;
        }

        TaskItem taskItem = taskItems.get(position);
        if (!taskItem.buttonType && taskItem.task.getText().isEmpty()) {
            View itemView = holder.itemView;
            itemView.requestFocus();
            InputMethodManager imm = (InputMethodManager) App.getInstance()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private int getButtonPosition() {
        for (int i = taskItems.size() - 1; i >= 0; i--) {
            if (taskItems.get(i).buttonType) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        TaskItem taskItemToPosition = taskItems.get(toPosition);
        if (taskItemToPosition.buttonType || taskItemToPosition.task.isDone()) {
            return;
        }

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                swapItemsOrderNumber(i, i + 1);
                Collections.swap(taskItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                swapItemsOrderNumber(i, i - 1);
                Collections.swap(taskItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        if (onTasksChangedListener != null) {
            onTasksChangedListener.onTasksChanged();
        }

    }

    private void swapItemsOrderNumber(int fromPosition, int toPosition) {
        TaskItem firstTaskItem = taskItems.get(fromPosition);
        Task firstTask = firstTaskItem.task;

        TaskItem secondTaskItem = taskItems.get(toPosition);
        Task secondTask = secondTaskItem.task;

        int firstTaskOrderNumber = firstTask.getOrderNumber();
        firstTask.setOrderNumber(secondTask.getOrderNumber());
        secondTask.setOrderNumber(firstTaskOrderNumber);

        firstTaskItem.task = firstTask;
        secondTaskItem.task = secondTask;

        taskItems.set(fromPosition, firstTaskItem);
        taskItems.set(toPosition, secondTaskItem);
    }

    @Override
    public void onItemDismiss(int position) {
        removeItem(position);
    }


    public List<Task> getTasks() {

        if (onTasksChangedListener == null) {
            return null;
        }

        List<Task> tasks = new ArrayList<>(taskItems.size());
        for (TaskItem taskItem : taskItems) {
            if (taskItem != null && !taskItem.buttonType && !taskItem.task.getText().isEmpty()) {
                tasks.add(taskItem.task);
            }
        }
        return tasks;
    }

    List<TaskItem> getTaskItems() {
        return taskItems;
    }

    int getCardColor() {
        return cardColor;
    }

    OnTasksChangedListener getOnTasksChangedListener() {
        return onTasksChangedListener;
    }

    OnStartDragListener getStartDragListener() {
        return startDragListener;
    }

    public void addTask(String text) {
        Task task = new Task(text, getNextTaskOrderNumber());
        TaskItem taskItem = new TaskItem(task);
        taskItems.add(taskItem);
        Collections.sort(taskItems);
        int position = taskItems.lastIndexOf(taskItem);
        notifyItemInserted(position);
    }

    private int getNextTaskOrderNumber() {
        if (taskItems.isEmpty()) {
            return 0;
        }

        for (int i = taskItems.size() - 1; i >= 0; i--) {
            TaskItem taskItem = taskItems.get(i);
            if (!taskItem.buttonType && !taskItem.task.isDone()) {
                return taskItems.get(i).task.getOrderNumber() + 1;
            }
        }

        for (int i = taskItems.size() - 1; i >= 0; i--) {
            TaskItem taskItem = taskItems.get(i);
            if (!taskItem.buttonType) {
                return taskItems.get(i).task.getOrderNumber() + 1;
            }
        }
        return 0;
    }

    public void removeItem(int position) {
        taskItems.remove(position);
        notifyItemRemoved(position);
    }

    private TaskItem createButtonItem() {
        TaskItem buttonItem = new TaskItem();
        buttonItem.buttonType = true;
        return buttonItem;
    }

    static class TaskItem implements Comparable<TaskItem> {
        Task task;
        boolean buttonType;

        public TaskItem(Task task) {
            this.task = task;
        }

        public TaskItem() {
        }

        @Override
        public int compareTo(TaskItem o) {
            if (!buttonType && !o.buttonType) {
                return task.compareTo(o.task);
            } else if (buttonType && !o.buttonType) {
                if (o.task.isDone()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (task.isDone()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaskItem taskItem = (TaskItem) o;
            return buttonType == taskItem.buttonType &&
                    Objects.equals(task, taskItem.task);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, buttonType);
        }
    }

}
