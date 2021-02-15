package ru.geekbrains.garmatin.lesson8.var1.participant_requirement;

public interface Participating extends Runnable, Jumping {
    boolean isDroppedOut();
    void setDroppedOut();
}
