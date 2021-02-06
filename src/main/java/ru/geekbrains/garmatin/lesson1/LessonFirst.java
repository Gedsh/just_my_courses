package ru.geekbrains.garmatin.lesson1;

public class LessonFirst {
    public static void main(String[] args) {
        System.out.println(initiateVars());
        System.out.println(calculate(1.f, 2.f, 3.f, 4.f));
        System.out.println(task10and20(10, 40));
        isPositiveOrNegative(50);
        System.out.println(isNegative(-60));
        greetings("Вася");
        leapYear(2021);
    }

    private static String initiateVars() {
        byte by = 0x1;
        short s = 10;
        int i = 20;
        long l = 30L;
        float f = 40.f;
        double d = 50.;
        char c = 'a';
        boolean bool = true;

        return typeOf(by) + typeOf(s) + typeOf(i) + typeOf(l) + typeOf(f) + typeOf(d) + typeOf(c) + typeOf(bool);
    }

    public static float calculate(float a, float b, float c, float d) {
        return a * (b + (c / d));
    }

    public static boolean task10and20(int x1, int x2) {
        int sum = x1 + x2;
        return sum > 10 && sum <= 20;
    }

    public static void isPositiveOrNegative(int x) {
        if(x >= 0) {
            System.out.println("Число положительное");
        } else {
            System.out.println("Число отрицательное");
        }
    }

    public static boolean isNegative(int x) {
        return x < 0;
    }

    public static void greetings(String name) {
        System.out.println("Привет, " + name + "!");
    }

    private static void leapYear(int year) {
        System.out.println(year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
    }

    private static String typeOf(Object o) {
        return o + " " + o.getClass().getCanonicalName() + "\n";
    }
}
