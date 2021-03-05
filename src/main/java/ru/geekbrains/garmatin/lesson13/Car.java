package ru.geekbrains.garmatin.lesson13;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static final AtomicBoolean winner = new AtomicBoolean();

    private static int CARS_COUNT;
    private final Race race;
    private final int speed;
    private final String name;

    private final CountDownLatch waitStart;
    private final CountDownLatch startOrder;
    private final CountDownLatch waitFinish;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed,
               CountDownLatch waitStart,
               CountDownLatch startOrder,
               CountDownLatch waitFinish) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;

        this.waitStart = waitStart;
        this.startOrder = startOrder;
        this.waitFinish = waitFinish;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            waitStart.countDown();
            startOrder.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        if (winner.compareAndSet(false, true)) {
            System.out.println(this.name + " WIN");
        }

        waitFinish.countDown();
    }
}
