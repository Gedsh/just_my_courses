package ru.geekbrains.garmatin.lesson14;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class LessonFourteenthTest {

    private final LessonFourteenth instance = new LessonFourteenth();

    @ParameterizedTest
    @MethodSource("dataForGetElementsAfterLastFour")
    void getElementsAfterLastFour(int[] inputArray, int[] expectedArray) {
        int[] actualArray = instance.getElementsAfterLastFour(inputArray);
        Assertions.assertArrayEquals(expectedArray, actualArray);
    }

    private static Stream<Arguments> dataForGetElementsAfterLastFour() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}, new int[]{1, 7}));
        out.add(Arguments.arguments(new int[]{4, 8, 7, 6, 5, 3, 5, 3, 8}, new int[]{8, 7, 6, 5, 3, 5, 3, 8}));
        out.add(Arguments.arguments(new int[]{9, 8, 7, 6, 5, 3, 4, 4, 4}, new int[]{}));
        out.add(Arguments.arguments(new int[]{4}, new int[]{}));
        return out.stream();
    }

    @Test
    void getElementsAfterLastFourException() {
        Assertions.assertThrows(RuntimeException.class, () -> instance.getElementsAfterLastFour(new int[]{1, 5, 7, 9, 0, -10}));
    }

    @Test
    void getElementsAfterLastFourEmptyArr() {
        Assertions.assertThrows(RuntimeException.class, () -> instance.getElementsAfterLastFour(new int[]{}));
    }

    @ParameterizedTest
    @MethodSource("dataForIsOneOrFourInArr")
    void isOneOrFourInArr(int[] inputArray, boolean expectedResult) {
        boolean actualResult = instance.isOneOrFourInArr(inputArray);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> dataForIsOneOrFourInArr() {
        List<Arguments> out = new ArrayList<>();

        out.add(Arguments.arguments(new int[]{1, 1, 1, 4, 4, 1, 4, 4}, true));
        out.add(Arguments.arguments(new int[]{1, 1, 1, 1, 1, 1}, false));
        out.add(Arguments.arguments(new int[]{4, 4, 4, 4}, false));
        out.add(Arguments.arguments(new int[]{1, 4, 4, 1, 1, 4, 3}, false));

        return out.stream();
    }

    @Test
    void isOneOrFourInArrEmptyArr() {
        Assertions.assertFalse(instance.isOneOrFourInArr(new int[]{}));
    }
}
