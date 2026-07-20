package ru.gazprom.server.service;

import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private List<Card> cards = new ArrayList<>();

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
        if(card.getTitle() == null) {
            throw new IllegalArgumentException("Title is required");
        }

        if (card.getPrice() == null || card.getPrice() < 0) {
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
