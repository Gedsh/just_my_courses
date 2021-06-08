package pan.alexander.notes.domain.tasksnote;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Task implements Comparable<Task> {
    private String text;
    private int orderNumber;
    private boolean isDone;

    public Task(String text, int orderNumber) {
        this.text = text;
        this.orderNumber = orderNumber;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public int compareTo(Task o) {
        if (isDone && !o.isDone) {
            return 1;
        } else if (!isDone && o.isDone) {
            return -1;
        }
        return orderNumber - o.orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return orderNumber == task.orderNumber &&
                isDone == task.isDone &&
                text.equals(task.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, orderNumber, isDone);
    }

    @NotNull
    @Override
    public String toString() {
        return "Task{" +
                "text='" + text + '\'' +
                ", orderNumber=" + orderNumber +
                ", isDone=" + isDone +
                '}';
    }
}
