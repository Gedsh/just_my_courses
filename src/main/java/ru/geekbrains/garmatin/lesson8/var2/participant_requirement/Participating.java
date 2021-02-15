package ru.geekbrains.garmatin.lesson8.var2.participant_requirement;

public interface Participating extends Runnable, Jumping {
    boolean isDroppedOut();
    void setDroppedOut();
}
