package ru.geekbrains.garmatin.lesson8.var2.participants;

import ru.geekbrains.garmatin.lesson8.var2.participant_requirement.Participating;

public class Robot implements Participating {

    private final int runScore;
    private final int jumpScore;

    private boolean droppedOut;

    public Robot(int runScore, int jumpScore) {
        this.runScore = runScore;
        this.jumpScore = jumpScore;
    }


    @Override
    public boolean jump(int height) {

        System.out.print("Робот прыгнул " + height);

        return jumpScore >= height;
    }

    @Override
    public boolean run(int length) {

        System.out.print("Робот бежал " + length);

        return runScore >= length;
    }

    @Override
    public boolean isDroppedOut() {
        return droppedOut;
    }

    @Override
    public void setDroppedOut() {
        this.droppedOut = true;
    }
}
