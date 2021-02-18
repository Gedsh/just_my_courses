package ru.geekbrains.garmatin.lesson9;

public class MyArrayDataException extends NumberFormatException {
    public MyArrayDataException(String errElement, int i, int j) {
        super("Невозможно преобразовать " + errElement + " элемент массива в число! Позиция " + i + " " + j);
    }
}
