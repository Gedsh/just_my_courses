package ru.geekbrains.garmatin.lesson8.var1.obstacles;

public class Wall implements Obstacle {
    private final int height;

    public Wall(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean action(ActionInterface actionInterface) {
        return actionInterface.passObstacle(this);
    }
}
