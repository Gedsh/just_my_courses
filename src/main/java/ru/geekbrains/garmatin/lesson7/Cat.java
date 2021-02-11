package ru.geekbrains.garmatin.lesson7;

public class Cat {
    private final String name;
    private final int appetite;
    private boolean satiety;

    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
    }

    public boolean eat(Plate p) {

        satiety = p.decreaseFood(appetite);

        if (satiety) {
            System.out.println("Кот " + name + " покушал. Он сытый.");
        } else {
            System.out.println("Кот " + name + " остался голодный. Хозяин, дай еды!");
        }

        return satiety;
    }
}
