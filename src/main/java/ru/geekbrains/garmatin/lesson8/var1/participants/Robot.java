package ru.geekbrains.garmatin.lesson8.var1.participants;

import ru.geekbrains.garmatin.lesson8.var1.obstacles.Track;
import ru.geekbrains.garmatin.lesson8.var1.obstacles.Wall;
import ru.geekbrains.garmatin.lesson8.var1.participant_requirement.Participating;

public class Robot implements Participating {

    private final int runScore;
    private final int jumpScore;

    private boolean droppedOut;

    public Robot(int runScore, int jumpScore) {
        this.runScore = runScore;
        this.jumpScore = jumpScore;
    }


    @Override
    public boolean jump(Wall wall) {

        System.out.print("Робот прыгнул " + wall.getHeight());

        return wall.getHeight() <= jumpScore;
    }

    @Override
    public boolean run(Track track) {

        System.out.print("Робот бежал " + track.getLength());

        return track.getLength() <= runScore;
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
