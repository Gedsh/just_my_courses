package ru.geekbrains.garmatin.lesson10.part2;

public class LessonTenthPartTwo {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Иванов", 12345678);
        phoneBook.add("Петров", 23456789);
        phoneBook.add("Сидоров", 345678901);
        phoneBook.add("Иванов", 456789012);
        phoneBook.add("Петров", 567890123);

        System.out.println("Иванов" + phoneBook.get("Иванов"));
        System.out.println("Петров" + phoneBook.get("Петров"));
        System.out.println("Сидоров" + phoneBook.get("Сидоров"));
    }
}
