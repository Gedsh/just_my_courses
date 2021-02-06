package ru.geekbrains.garmatin.lesson6;

public class Cat extends Animal{

    public Cat(String name, int runLimit, int swimLimit) {
        super(name, runLimit, swimLimit);
    }

    @Override
    public void run(int distance) {
        if (distance < 0 || distance > runLimit) {
            System.out.println("Кот " + name + " не умеет бежать " + distance +  " метров!");
        } else {
            System.out.println("Кот " + name + " пробежал " + distance + " метров");
        }
    }

    @Override
    public void swim(int distance) {
        if (distance < 0 || distance > swimLimit) {
            System.out.println("Кот " + name + " не умеет плыть " + distance +  " метра!");
        } else {
            System.out.println("Кот " + name + " проплыл " + distance + " метра");
        }
    }
}
