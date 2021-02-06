package ru.geekbrains.garmatin.lesson6;

public class LessonSixth {
    public static void main(String[] args) {
        Animal[] animals = new Animal[5];
        addAnimals(animals);
        makeAnimalsRun(animals, 200);
        makeAnimalsSwim(animals, 2);
        printObjectsQuantity();
    }

    private static void addAnimals(Animal[] animals) {
        animals[0] = new Dog("Бобик", 500, 10);
        animals[1] = new Dog("Тузик", 700, 0);
        animals[2] = new Cat("Мурчик", 200, 0);
        animals[3] = new Cat("Пушистик", 100, 3);
        animals[4] = new Cat("Рыжик", 50, 0);
    }

    private static void makeAnimalsRun(Animal[] animals, int distance) {
        for (Animal animal: animals) {
            animal.run(distance);
        }
    }

    private static void makeAnimalsSwim(Animal[] animals, int distance) {
        for (Animal animal: animals) {
            animal.swim(distance);
        }
    }

    private static void printObjectsQuantity() {
        System.out.println("Котов " + Animal.cats + ", собак " + Animal.dogs + ", животных " + Animal.animals);
    }
}
