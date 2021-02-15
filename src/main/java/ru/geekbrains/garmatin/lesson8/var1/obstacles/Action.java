package ru.geekbrains.garmatin.lesson8.var1.obstacles;

import ru.geekbrains.garmatin.lesson8.var1.participant_requirement.Participating;

public class Action implements ActionInterface {

    private final Participating participator;

    public Action(Participating participator) {
        this.participator = participator;
    }


    @Override
    public boolean passObstacle(Track track) {
        return participator.run(track);
    }

    @Override
    public boolean passObstacle(Wall wall) {
        return participator.jump(wall);
    }
}
