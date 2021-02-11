package ru.geekbrains.garmatin.lesson7;

public class LessonSeventh {
    public static void main(String[] args) {
        Plate plate = new Plate(100);
        Cat[] cats = new Cat[5];

        addCats(cats);
        makeCatsEat(cats, plate);
    }

    private static void addCats(Cat[] cats) {
        cats[0] = new Cat("Мурчик", 5);
        cats[1] = new Cat("Пушистик", 6);
        cats[2] = new Cat("Рыжик", 7);
        cats[3] = new Cat("Ловкач", 8);
        cats[4] = new Cat("Васька", 4);
    }
    
    private static void makeCatsEat(Cat[] cats, Plate plate) {
        boolean allCatsSatisfied = true;
        boolean canStopEating = false;

        while (true) {

            for (Cat cat : cats) {
                if (!cat.eat(plate) && allCatsSatisfied) {
                    allCatsSatisfied = false;
                }
                plate.info();
            }

            System.out.println();

            if (canStopEating) {
                System.out.println("Все! Коты переели :)");
                break;
            }

            if (!allCatsSatisfied) {
                System.out.println();

                plate.putFood(100);
                allCatsSatisfied = true;
                canStopEating = true;

                System.out.println();
            }
        }
    }
}
