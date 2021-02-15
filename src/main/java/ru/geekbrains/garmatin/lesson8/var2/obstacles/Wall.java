package ru.geekbrains.garmatin.lesson8.var2.obstacles;

import ru.geekbrains.garmatin.lesson8.var2.participant_requirement.Participating;

public class Wall implements Obstacle {
    private final int height;

    public Wall(int height) {
        this.height = height;
    }

    public boolean jumpWall(Participating jumping) {
        return jumping.jump(height);
    }

    @Override
    public boolean action(ActionInterface actionInterface) {
        return actionInterface.passObstacle(this);
    }
}
