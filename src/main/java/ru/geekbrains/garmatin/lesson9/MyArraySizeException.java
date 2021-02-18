package ru.geekbrains.garmatin.lesson9;

public class MyArraySizeException extends IllegalArgumentException {
    public MyArraySizeException() {
        super("Массив должен быть 4х4!");
    }
}
