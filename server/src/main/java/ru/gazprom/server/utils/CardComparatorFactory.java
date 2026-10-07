package ru.gazprom.server.utils;


import ru.gazprom.server.enums.SortType;
import ru.gazprom.server.model.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.nullsFirst;

public class CardComparatorFactory {

    public static Comparator<Card> compareByPriceAndTitle(SortType sortType) {
        List<Comparator<Card>> criteria = new ArrayList<>();
        criteria.add(Comparator.comparing(Card::getTitle, nullsFirst(Comparator.naturalOrder())));
        criteria.add(Comparator.comparing(Card::getPrice, nullsFirst(Comparator.naturalOrder())));

        Comparator<Card> comparator = Comparator.comparing(card -> 0);
        for (Comparator<Card> elem: criteria) {
            comparator = comparator.thenComparing(elem);
        }

        return sortType == SortType.DESC ? comparator.reversed() : comparator;
    }
}