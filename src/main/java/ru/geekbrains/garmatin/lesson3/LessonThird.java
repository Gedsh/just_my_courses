package ru.geekbrains.garmatin.lesson3;

import java.util.Random;
import java.util.Scanner;

public class LessonThird {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        guessNumber(scanner);

        guessWord(scanner);

        scanner.close();

    }

    private static void guessNumber(Scanner scanner) {

        final int MAX_GUESS_NUMBER = 9;
        final int MAX_TRY_COUNT = 3;

        final Random random = new Random();

        do {

            int guessNumber = random.nextInt(MAX_GUESS_NUMBER + 1);
            boolean guessed = false;

            System.out.printf("Угадайте число от 0 до %d. ", MAX_GUESS_NUMBER);

            for (int tryCount = MAX_TRY_COUNT; tryCount > 0; tryCount--) {

                String leavesStr = "осталась";
                String triesStr = "попытка";
                if (tryCount > 1) {
                    leavesStr = "осталось";
                    triesStr = "попытки";
                }

                System.out.printf("У вас %s %d %s ->> ", leavesStr, tryCount, triesStr);
                int answer = scanner.nextInt();
                if (answer == guessNumber) {
                    guessed = true;
                    break;
                } else if (answer > guessNumber) {
                    System.out.println("Число больше загаданного.");
                } else {
                    System.out.println("Число меньше загаданного.");
                }
            }

            if (guessed) {
                System.out.println("Поздравляю, вы угадали!");
            } else {
                System.out.printf("Вы проиграли. Загаданное число %d", guessNumber);
            }

            System.out.println("\nПовторить игру еще раз? 1 – да / 0 – нет");
        } while (scanner.nextInt() != 0);

        System.out.println();
    }

    private static void guessWord(Scanner scanner) {
        final String[] words = {"apple", "orange", "lemon", "banana", "apricot", "avocado", "broccoli", "carrot",
                "cherry", "garlic", "grape", "melon", "leak", "kiwi", "mango", "mushroom", "nut", "olive", "pea",
                "peanut", "pear", "pepper", "pineapple", "pumpkin", "potato"};

        final Random random = new Random();

        String guessWord = words[random.nextInt(words.length)];
        final int fixedWordLength = 15;

        boolean guessed = false;

        System.out.print("Угадайте слово ->> ");

        while (!guessed) {
            String answer = scanner.nextLine();

            if (answer.isEmpty()) {
                continue;
            }

            if (answer.equals(guessWord)) {
                guessed = true;
            } else {
                printSharpedLine(answer, guessWord, fixedWordLength);
            }

            if (guessed) {
                System.out.println("Поздравляю! Вы угадали.");
            } else {
                System.out.print("Вы не угадали попробуйте еще раз ->> ");
            }
        }
    }

    private static void printSharpedLine(String answer, String guessWord, int fixedWordLength) {
        while (guessWord.length() > fixedWordLength) {
            fixedWordLength *= 1.5;
        }

        for (int i = 0; i < fixedWordLength; i++) {
            if (i >= guessWord.length() || i >= answer.length()) {
                System.out.print("#");
            } else if (guessWord.charAt(i) == answer.charAt(i)) {
                System.out.print(guessWord.charAt(i));
            } else {
                System.out.print("#");
            }
        }
        System.out.println();
    }
}
