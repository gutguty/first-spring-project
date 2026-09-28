package ru.gazprom.server.utils;

import ru.gazprom.server.enums.SortType;
import ru.gazprom.server.model.Category;

import java.util.Comparator;

import static java.util.Comparator.nullsFirst;

public class CategoryComparatorFactory {

    public static Comparator<Category> compareByCreatedAt(SortType sortType) {

        Comparator<Category> comparator = Comparator
                .comparing(Category::getCreatedAt, nullsFirst(Comparator.naturalOrder()))
                .thenComparing(Category::getName, nullsFirst(Comparator.naturalOrder()));


        return sortType == SortType.DESC ? comparator.reversed() : comparator;
    }
}