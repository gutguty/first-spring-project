package ru.gazprom.server.service;

import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    private static List<Card> cards = new ArrayList<>();

    static {
        cards.add(new Card(1L, "Shoes", 1000, "shoes.png"));
        cards.add(new Card(2L, "Shirts", 2000, "shirts.png"));
        cards.add(new Card(3L, "Trousers", 3000, "trousers.png"));
        cards.add(new Card(4L, "Hats", 4000, "hats.png"));
        cards.add(new Card(5L, "Sweater", 2500, "sweater.png"));
        cards.add(new Card(6L, "Belt", 3200, "belt.png"));
        cards.add(new Card(7L, "Bag", 500, "bag.png"));
        cards.add(new Card(8L, "Gloves", 750, "gloves.png"));
    }

    public List<Card> getAll() {
        return cards;
    }

    public Card getCard(Long id) {
        return cards.stream()
                .filter(card -> card.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new CardNotFoundException("Card with " + id + " id not found"));
    }

    public Card createCard(Card card) {
        if(card.getTitle() == null || card.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (card.getPrice() == null || card.getPrice() <= 0) {
            throw new IllegalArgumentException("Price is invalid");
        }

        card.setId((long) cards.size() + 1);
        cards.add(card);
        return card;
    }

    public void deleteCard(Long id) {
        boolean removed = cards.removeIf(card -> card.getId().equals(id));
        if (!removed) {
            throw new CardNotFoundException("Card with " + id + " id not found");
        }
    }

    public Card updateCard(Long id, Card newCard) {
        Card existCard = cards.stream()
                .filter(card -> card.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new CardNotFoundException("Error update card: id - " + id + " card - " + newCard));

        int index = cards.indexOf(existCard);
        newCard.setId(id);
        cards.set(index, newCard);

        return newCard;
    }
}
