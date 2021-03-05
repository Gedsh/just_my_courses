package ru.geekbrains.garmatin.lesson13;

import java.util.concurrent.CountDownLatch;

public class LessonThirteenth {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        final CountDownLatch waitStart = new CountDownLatch(CARS_COUNT);
        final CountDownLatch waitFinish = new CountDownLatch(CARS_COUNT);
        final CountDownLatch startOrder = new CountDownLatch(1);

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), waitStart, startOrder, waitFinish);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }

        try {
            waitStart.await();
        } catch (InterruptedException e) {
            System.err.println("CountDownLatch interrupted " + e.getMessage());
        }

        startOrder.countDown();

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            waitFinish.await();
        } catch (InterruptedException e) {
            System.err.println("CountDownLatch interrupted" + e.getMessage());
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
