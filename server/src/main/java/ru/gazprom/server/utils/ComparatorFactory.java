package ru.gazprom.server.utils;


import ru.gazprom.server.enums.SortType;

import java.util.Comparator;
import java.util.function.Function;

public class ComparatorFactory {
    public static <T, U extends Comparable<? super U>> Comparator<T> compare(SortType sortType, Function<T, U> keyExtractor) {
        Comparator<T> comparator = Comparator.comparing(keyExtractor);
        return sortType == SortType.DESC ? comparator.reversed() : comparator;
    }
}