package pan.alexander.notes.presentation.recycler;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Collections;

import pan.alexander.notes.R;
import pan.alexander.notes.domain.tasksnote.Task;

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TaskAdapter taskAdapter;
    private final CardView cardTaskNoteItem;
    private final ImageView moveTaskNoteItem;
    private final AppCompatEditText editTextTaskNoteItem;
    private final ImageView deleteTaskNoteItem;
    private final MaterialButton imageButtonAddTask;
    private final MaterialCheckBox checkBoxTaskNoteItem;

    public TaskViewHolder(@NonNull View itemView, TaskAdapter taskAdapter) {
        super(itemView);
        this.taskAdapter = taskAdapter;

        cardTaskNoteItem = itemView.findViewById(R.id.cardTaskNoteItem);
        moveTaskNoteItem = itemView.findViewById(R.id.moveTaskNoteItem);
        if (moveTaskNoteItem != null) {
            moveTaskNoteItem.setOnClickListener(this);
        }
        editTextTaskNoteItem = itemView.findViewById(R.id.editTextTaskNoteItem);
        deleteTaskNoteItem = itemView.findViewById(R.id.deleteTaskNoteItem);
        if (deleteTaskNoteItem != null) {
            deleteTaskNoteItem.setOnClickListener(this);
        }
        checkBoxTaskNoteItem = itemView.findViewById(R.id.checkBoxTaskNoteItem);
        if (checkBoxTaskNoteItem != null) {
            checkBoxTaskNoteItem.setOnClickListener(this);
        }
        imageButtonAddTask = itemView.findViewById(R.id.imageButtonAddTask);
        if (imageButtonAddTask != null) {
            imageButtonAddTask.setBackgroundColor(ColorUtils.blendARGB(taskAdapter.cardColor, Color.BLACK, .3f));
            imageButtonAddTask.setOnClickListener(this);
        }
    }

    public void bind(int position) {


        if (cardTaskNoteItem != null && editTextTaskNoteItem != null && checkBoxTaskNoteItem != null) {

            Task task = taskAdapter.tasks.get(position);

            cardTaskNoteItem.setCardBackgroundColor(taskAdapter.cardColor);
            editTextTaskNoteItem.setText(task.getText());
            editTextTaskNoteItem.setEnabled(!task.isDone());
            checkBoxTaskNoteItem.setChecked(task.isDone());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.moveTaskNoteItem) {
            Toast.makeText(v.getContext(), "Move task", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.deleteTaskNoteItem) {
            Toast.makeText(v.getContext(), "Delete task", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.imageButtonAddTask) {
            Toast.makeText(v.getContext(), "Add task", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.checkBoxTaskNoteItem) {
            int position = getAdapterPosition();
            Task task = taskAdapter.tasks.get(position);
            task.setDone(!task.isDone());
            taskAdapter.tasks.set(position, task);
            Collections.sort(taskAdapter.tasks);
            taskAdapter.notifyItemChanged(position);
            taskAdapter.notifyItemMoved(position, taskAdapter.tasks.indexOf(task));
        }
    }
}
