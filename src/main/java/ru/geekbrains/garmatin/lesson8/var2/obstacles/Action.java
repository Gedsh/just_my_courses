package ru.geekbrains.garmatin.lesson8.var2.obstacles;

import ru.geekbrains.garmatin.lesson8.var2.participant_requirement.Participating;

public class Action implements ActionInterface {

    private final Participating participator;

    public Action(Participating participator) {
        this.participator = participator;
    }


    @Override
    public boolean passObstacle(Track track) {
        return track.runTrack(participator);
    }

    @Override
    public boolean passObstacle(Wall wall) {
        return wall.jumpWall(participator);
    }
}
