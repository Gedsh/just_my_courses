package pan.alexander.notes.presentation.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.tasksnote.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    final List<Task> tasks = new ArrayList<>();
    int cardColor;

    public void refreshTasks(List<Task> tasks, int cardColor) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        Collections.sort(this.tasks);
        this.cardColor = cardColor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == tasks.size()) ? R.layout.recycle_item_add_task : R.layout.recycle_item_tasks;
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

        return new TaskViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return tasks.size() + 1;
    }
}
