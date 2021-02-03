package ru.geekbrains.garmatin;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class LessonForth {

    private static final int MAP_SIZE = 3;
    private static final char CELL_EMPTY = '*';
    private static final char HUMAN_SYMBOL = 'X';
    private static final char COMPUTER_SYMBOL = 'O';
    private static final float ABSOLUTE_WIN_PROBABILITY = 0.95f;
    private static final Random random = new Random();
    private static char[][] map;
    private static boolean winImpossible;

    private enum Strategy {
        RANDOM,
        HORIZONTAL,
        VERTICAL,
        DIAGONAL_ONE,
        DIAGONAL_TWO
    }


    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            initMap();
            printMap();

            while (true) {
                humanTurn(scanner);
                printMap();

                if (checkWin(HUMAN_SYMBOL)) {
                    System.out.println("Выиграл человек!");
                    break;
                }

                if (isMapFull() || winImpossible) {
                    System.out.println("Ничья");
                    break;
                }

                aiTurn();
                printMap();

                if (checkWin(COMPUTER_SYMBOL)) {
                    System.out.println("Выиграл ИИ!");
                    break;
                }

                if (isMapFull() || winImpossible) {
                    System.out.println("Ничья");
                    break;
                }

            }

            System.out.println("Конец игры");
        }
    }

    private static boolean checkWin(char symbol) {
        return isHorizontalWin(symbol) || isVerticalWin(symbol) || isDiagonalWin(symbol);
    }

    private static boolean isHorizontalWin(char symbol) {
        int horizontalCells = 0;

        for (char[] chars : map) {

            for (int i = 0; i < map.length; i++) {
                if (chars[i] == symbol) {
                    horizontalCells++;
                }
            }

            if (horizontalCells == map.length) {
                return true;
            } else {
                horizontalCells = 0;
            }
        }

        return false;
    }

    private static boolean isVerticalWin(char symbol) {
        int verticalCells = 0;

        for (int i = 0; i < map.length; i++) {

            for (char[] chars : map) {
                if (chars[i] == symbol) {
                    verticalCells++;
                }
            }

            if (verticalCells == map.length) {
                return true;
            } else {
                verticalCells = 0;
            }
        }

        return false;
    }

    private static boolean isDiagonalWin(char symbol) {
        int firstDiagonalCells = 0;
        int secondDiagonalCells = 0;

        for (int i = 0, j = map.length - 1; i < map.length; i++, j--) {
            if (map[i][i] == symbol) {
                firstDiagonalCells++;
            }
            if (map[i][j] == symbol) {
                secondDiagonalCells++;
            }
        }

        return firstDiagonalCells == map.length || secondDiagonalCells == map.length;
    }

    private static void initMap() {

        map = new char[MAP_SIZE][MAP_SIZE];

        Arrays.stream(map).forEach(arr -> Arrays.fill(arr, CELL_EMPTY));
    }

    private static void printMap() {
        System.out.print("\t");
        IntStream.range(0, map.length).forEach(i -> System.out.print(i + 1 + " "));
        System.out.println();

        IntStream.range(0, map.length).forEach(i -> {
            System.out.print(i + 1 + "\t");
            IntStream.range(0, map.length).forEach(j -> System.out.print(map[i][j] + " "));
            System.out.println();
        });
    }

    private static void humanTurn(Scanner scanner) {
        int x = -1, y = -1;

        do {
            System.out.println("Введите координаты в формате X Y ->> ");
            try {
                x = scanner.nextInt();
                y = scanner.nextInt();
            } catch (InputMismatchException ignored) {
                scanner.next();
            }

        } while (isCellNotValid(y - 1, x - 1));

        map[y - 1][x - 1] = HUMAN_SYMBOL;
    }

    private static void aiTurn() {
        Runnable action = () -> computerTurn(COMPUTER_SYMBOL);
        float humanWinProbability;
        float computerWinProbability;

        System.out.println("До хода AI:");

        Pair<Runnable, Float> humanNextStep = getNextHumanStep();
        humanWinProbability = humanNextStep.getValue();
        if (humanWinProbability > ABSOLUTE_WIN_PROBABILITY) {
            action = humanNextStep.getKey();
        }

        Pair<Runnable, Float> aiNextStep = getNextAIStep();
        computerWinProbability = aiNextStep.getValue();
        if (humanWinProbability <= ABSOLUTE_WIN_PROBABILITY
                || computerWinProbability > ABSOLUTE_WIN_PROBABILITY) {
            action = aiNextStep.getKey();
        }

        action.run();

        System.out.println("После хода AI:");

        Pair<Runnable, Float> humanPossibleStep = getNextHumanStep();
        float humanWinPossibility = humanPossibleStep.getValue();

        if (humanWinProbability == 0 && computerWinProbability == 0
                || humanWinPossibility == 0 && computerWinProbability == 0) {
            winImpossible = true;
        }
    }

    private static Pair<Runnable, Float> getNextAIStep() {
        return getNextStep(COMPUTER_SYMBOL, COMPUTER_SYMBOL);
    }

    private static Pair<Runnable, Float> getNextHumanStep() {
        return getNextStep(HUMAN_SYMBOL, COMPUTER_SYMBOL);
    }

    private static Pair<Runnable, Float> getNextStep(char symbolToCheck, char symbolToPut) {
        float bestWinProbability = 0;
        Map<Runnable, Strategy> useFeatureMap = new HashMap<>();
        useFeatureMap.put(() -> computerTurn(symbolToPut), Strategy.RANDOM);

        Pair<Integer, Float> horizontalWinProbability = getLineWithBestHorizontalWinProbability(symbolToCheck);
        Pair<Integer, Float> verticalWinProbability = getLineWithBestVerticalWinProbability(symbolToCheck);
        float diagonalOneWinProbability = getDiagonalOneWinProbability(symbolToCheck);
        float diagonalTwoWinProbability = getDiagonalSecondWinProbability(symbolToCheck);

        if (horizontalWinProbability.getValue() >= bestWinProbability) {
            if (horizontalWinProbability.getValue() > bestWinProbability) {
                useFeatureMap.clear();
            }

            bestWinProbability = horizontalWinProbability.getValue();

            useFeatureMap.put(() -> putSymbolToHorizontalLine(
                    horizontalWinProbability.getKey(), symbolToPut), Strategy.HORIZONTAL
            );
        }

        if (verticalWinProbability.getValue() >= bestWinProbability) {
            if (verticalWinProbability.getValue() > bestWinProbability) {
                useFeatureMap.clear();
            }

            bestWinProbability = verticalWinProbability.getValue();

            useFeatureMap.put(() -> putSymbolToVerticalLine(
                    verticalWinProbability.getKey(), symbolToPut), Strategy.VERTICAL
            );
        }

        if (diagonalOneWinProbability >= bestWinProbability) {
            if (diagonalOneWinProbability > bestWinProbability) {
                useFeatureMap.clear();
            }

            bestWinProbability = diagonalOneWinProbability;

            useFeatureMap.put(() -> putSymbolToDiagonalOne(symbolToPut), Strategy.DIAGONAL_ONE);
        }

        if (diagonalTwoWinProbability >= bestWinProbability) {
            if (diagonalTwoWinProbability > bestWinProbability) {
                useFeatureMap.clear();
            }

            bestWinProbability = diagonalTwoWinProbability;

            useFeatureMap.put(() -> putSymbolToDiagonalTwo(symbolToPut), Strategy.DIAGONAL_TWO);
        }

        if (bestWinProbability == 0) {
            useFeatureMap.clear();
            useFeatureMap.put(() -> computerTurn(symbolToPut), Strategy.RANDOM);
        }

        List<Runnable> useFeaturesChoice = new ArrayList<>(useFeatureMap.keySet());
        if (useFeatureMap.size() > 1) {
            Collections.shuffle(useFeaturesChoice);
        }

        Runnable choice = useFeaturesChoice.get(0);

        System.out.println("Пользователь: " + symbolToCheck + "; " +
                " Вероятность выигрыша: " + bestWinProbability + "; " +
                "Стратегия " + useFeatureMap.get(choice));

        return new Pair<>(choice, bestWinProbability);
    }


    private static Pair<Integer, Float> getLineWithBestHorizontalWinProbability(char symbol) {
        int bestWinProbabilityLine = 0;
        float bestWinProbability = 0.f;
        for (int i = 0; i < map.length; i++) {
            float probability = getWinProbability(map[i], symbol);
            if (probability > bestWinProbability) {
                bestWinProbabilityLine = i;
                bestWinProbability = probability;
            }
        }
        return new Pair<>(bestWinProbabilityLine, bestWinProbability);
    }

    private static Pair<Integer, Float> getLineWithBestVerticalWinProbability(char symbol) {
        char[] line = new char[map.length];
        int lineCounter;

        int bestWinProbabilityLine = 0;
        float bestWinProbability = 0.f;

        for (int i = 0; i < map.length; i++) {

            lineCounter = 0;

            for (char[] lines : map) {
                line[lineCounter] = lines[i];
                lineCounter++;
            }

            float probability = getWinProbability(line, symbol);

            if (probability > bestWinProbability) {
                bestWinProbabilityLine = i;
                bestWinProbability = probability;
            }
        }

        return new Pair<>(bestWinProbabilityLine, bestWinProbability);
    }

    private static float getDiagonalOneWinProbability(char symbol) {
        char[] line = new char[map.length];
        for (int i = 0; i < map.length; i++) {
            line[i] = map[i][i];
        }
        return getWinProbability(line, symbol);
    }

    private static float getDiagonalSecondWinProbability(char symbol) {
        char[] line = new char[map.length];
        for (int i = 0, j = map.length - 1; i < map.length; i++, j--) {
            line[i] = map[i][j];
        }
        return getWinProbability(line, symbol);
    }

    private static void putSymbolToHorizontalLine(int lineNumber, char symbol) {
        int y;

        char[] line = map[lineNumber];
        if (isLineFull(line)) {
            computerTurn(symbol);
            return;
        }

        do {
            y = random.nextInt(map.length);
        } while (isCellNotValid(lineNumber, y));

        map[lineNumber][y] = symbol;
    }

    private static void putSymbolToVerticalLine(int lineNumber, char symbol) {
        int x;

        char[] line = new char[map.length];
        int lineCounter = 0;

        for (char[] lines : map) {
            line[lineCounter] = lines[lineNumber];
            lineCounter++;
        }

        if (isLineFull(line)) {
            computerTurn(symbol);
            return;
        }

        do {
            x = random.nextInt(map.length);
        } while (isCellNotValid(x, lineNumber));

        map[x][lineNumber] = symbol;
    }

    private static void putSymbolToDiagonalOne(char symbol) {
        int x;

        char[] line = new char[map.length];
        for (int i = 0; i < map.length; i++) {
            line[i] = map[i][i];
        }

        if (isLineFull(line)) {
            computerTurn(symbol);
            return;
        }

        do {
            x = random.nextInt(map.length);
        } while (isCellNotValid(line, x));

        line[x] = symbol;

        for (int i = 0; i < map.length; i++) {
            map[i][i] = line[i];
        }
    }

    private static void putSymbolToDiagonalTwo(char symbol) {
        int x;

        char[] line = new char[map.length];
        for (int i = 0, j = map.length - 1; i < map.length; i++, j--) {
            line[i] = map[i][j];
        }

        if (isLineFull(line)) {
            computerTurn(symbol);
            return;
        }

        do {
            x = random.nextInt(map.length);
        } while (isCellNotValid(line, x));

        line[x] = symbol;

        for (int i = 0, j = map.length - 1; i < map.length; i++, j--) {
            map[i][j] = line[i];
        }
    }

    private static void computerTurn(char computerSymbol) {

        int x, y;

        do {
            x = random.nextInt(map.length);
            y = random.nextInt(map.length);
        } while (isCellNotValid(x, y));

        map[x][y] = computerSymbol;
    }

    private static float getWinProbability(char[] line, char symbol) {
        if (isLineFull(line)) {
            return 0;
        }

        float checkedCells = 0;
        for (char ch : line) {
            if (ch == symbol) {
                checkedCells++;
            } else if (ch != CELL_EMPTY) {
                return 0;
            }
        }
        return (checkedCells + 1) / map.length;
    }

    private static boolean isMapFull() {
        for (char[] chars : map) {
            for (int i = 0; i < map.length; i++) {
                if (chars[i] == CELL_EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isLineFull(char[] line) {
        for (char ch : line) {
            if (ch == CELL_EMPTY) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCellNotValid(int x, int y) {
        return x < 0 || y < 0
                || x >= map.length || y >= map.length
                || map[x][y] != CELL_EMPTY;
    }

    private static boolean isCellNotValid(char[] line, int x) {
        return x < 0 || x >= map.length || line[x] != CELL_EMPTY;
    }
}
