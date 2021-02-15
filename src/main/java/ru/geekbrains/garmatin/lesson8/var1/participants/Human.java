package ru.geekbrains.garmatin.lesson8.var1.participants;

import ru.geekbrains.garmatin.lesson8.var1.obstacles.Track;
import ru.geekbrains.garmatin.lesson8.var1.obstacles.Wall;
import ru.geekbrains.garmatin.lesson8.var1.participant_requirement.Participating;

public class Human implements Participating {

    private final int runScore;
    private final int jumpScore;

    private boolean droppedOut;


    public Human(int runScore, int jumpScore) {
        this.runScore = runScore;
        this.jumpScore = jumpScore;
    }


    @Override
    public boolean jump(Wall wall) {

        System.out.print("Человек прыгнул " + wall.getHeight());

        return wall.getHeight() <= jumpScore;
    }

    @Override
    public boolean run(Track track) {

        System.out.print("Человек бежал " + track.getLength());

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
