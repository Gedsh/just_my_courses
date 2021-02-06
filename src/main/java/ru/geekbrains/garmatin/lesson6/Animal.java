package ru.geekbrains.garmatin.lesson6;

public abstract class Animal {
    static int cats;
    static int dogs;
    static int animals;

    protected String name;
    protected int runLimit;
    protected int swimLimit;

    public Animal(String name, int runLimit, int swimLimit) {
        this.name = name;
        this.runLimit = runLimit;
        this.swimLimit = swimLimit;

        if (this instanceof Cat) {
            cats++;
        } else if (this instanceof Dog) {
            dogs++;
        }

        animals++;
    }

    protected abstract void run(int distance);

    protected abstract void swim(int distance);
}
