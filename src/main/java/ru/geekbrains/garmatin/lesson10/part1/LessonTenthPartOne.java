package ru.geekbrains.garmatin.lesson10.part1;

import java.util.*;

public class LessonTenthPartOne {

    public static void main(String[] args) {
        List<String> words = getWords();
        Map<String, Integer> wordsToQuantity = new HashMap<>();
        words.forEach(word -> {
            Integer quantity = wordsToQuantity.get(word);
            if (quantity != null) {
                wordsToQuantity.put(word, ++quantity);
            } else {
                wordsToQuantity.put(word, 1);
            }
        });

        System.out.println(wordsToQuantity);
    }


    private static List<String> getWords() {
        return Arrays.asList(
                "Антон", "Антон", "Антон", "Антон",
                "Густав", "Густав", "Густав",
                "Чингиз", "Чингиз",
                "Михаил", "Михаил",
                "Григорий", "Григорий",
                "Спартак",
                "Добрыня",
                "Яков",
                "Авраам",
                "Фархад",
                "Мансур",
                "Георгий");
    }
}
