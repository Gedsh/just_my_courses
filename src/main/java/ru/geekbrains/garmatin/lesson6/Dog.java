package ru.geekbrains.garmatin.lesson6;

public class Dog extends Animal{

    public Dog(String name, int runLimit, int swimLimit) {
        super(name, runLimit, swimLimit);
    }

    @Override
    public void run(int distance) {
        if (distance < 0 || distance > runLimit) {
            System.out.println("Собака " + name + " не умеет бежать " + distance +  " метров!");
        } else {
            System.out.println("Собака " + name + " пробежал " + distance + " метров");
        }
    }

    @Override
    public void swim(int distance) {
        if (distance < 0 || distance > swimLimit) {
            System.out.println("Собака " + name + " не умеет плыть " + distance +  " метра!");
        } else {
            System.out.println("Собака " + name + " проплыл " + distance + " метра");
        }
    }
}
