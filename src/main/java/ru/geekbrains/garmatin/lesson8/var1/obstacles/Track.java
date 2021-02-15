package ru.geekbrains.garmatin.lesson8.var1.obstacles;

public class Track implements Obstacle {
    private final int length;

    public Track(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean action(ActionInterface actionInterface) {
        return actionInterface.passObstacle(this);
    }
}
