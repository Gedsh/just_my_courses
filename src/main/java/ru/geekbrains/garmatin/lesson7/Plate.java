package ru.geekbrains.garmatin.lesson7;

public class Plate {
    private int food;

    public Plate(int food) {
        this.food = food;
    }

    public boolean decreaseFood(int n) {
        if (food - n >= 0) {
            food -= n;
            return true;
        }

        return false;
    }

    public void putFood(int n) {
        if (n > 0) {
            System.out.println("В тарелку добавили " + n + " еды.");
            food += n;
        }
    }

    public void info() {
        System.out.println("В тарелке осталось " + food + " еды.");
    }
}
