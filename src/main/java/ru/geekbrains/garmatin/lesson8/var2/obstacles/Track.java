package ru.geekbrains.garmatin.lesson8.var2.obstacles;

import ru.geekbrains.garmatin.lesson8.var2.participant_requirement.Participating;

public class Track implements Obstacle {
    private final int length;

    public Track(int length) {
        this.length = length;
    }

    public boolean runTrack(Participating runnable) {
        return runnable.run(length);
    }


    @Override
    public boolean action(ActionInterface actionInterface) {
        return actionInterface.passObstacle(this);
    }
}
