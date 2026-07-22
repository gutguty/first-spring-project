package ru.gazprom.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;

import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {
    private CardService cardService;

    @BeforeEach
    void setCards() {
        cardService = new CardService();
        cardService.createCard(new Card(null, "Shoes", 1000, "shoes.png"));
        cardService.createCard(new Card(null, "Shirts", 2000, "shirts.png"));
        cardService.createCard(new Card(null,"Trousers", 3000, "trousers.png"));
    }

    //Create test card
    @Test
    void createCardSuccess() {
        Card created = cardService.createCard(
                new Card(null, "Jacket", 5000, "jacket.png"));
        assertEquals(4, cardService.getAll().size());
        assertEquals("Jacket", created.getTitle());
        assertEquals(4L, created.getId());
    }

    @Test
    void createCardEmptyTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "", 2000, "shirts.png")));
    }

    @Test
    void createCardSpaceTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "    ", 2000, "shirts.png")));
    }

    @Test
    void createCardNullPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "Shirts", 0, "shirts.png")));
    }

    @Test
    void createCardNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "Shirts", -100, "shirts.png")));
    }

    @Test
    void createCardNullTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, null, 1000, "shoes.png")));
    }

    //getTestCard
    @Test
    void getAllCardsTest() {
        var result = cardService.getAll();
        assertEquals(3, result.size());

    }

    @Test
    void getCardById() {
        var result = cardService.getCard(2L);
        var expected = new Card(2L, "Shirts", 2000, "shirts.png");
        assertEquals(expected, result);
    }

    @Test
    void getInvalidId() {
        assertThrows(CardNotFoundException.class,
                () -> cardService.getCard(999L));
    }

    //deleteTestCard
    @Test
    void deleteCardById() {
        cardService.deleteCard(2L);
        assertEquals(2, cardService.getAll().size());
    }

    @Test
    void deleteCardByIdNotFoundCard() {
        cardService.deleteCard(2L);

        assertThrows(CardNotFoundException.class,
                () -> cardService.getCard(2L));
    }

    @Test
    void deleteInvalidId() {
        assertThrows(CardNotFoundException.class,
                () -> cardService.deleteCard(999L));
    }

    //updateTestCard
    @Test
    void updateCard() {
        var expected = new Card(2L, "Laptop", 15000, "laptop.png");
        cardService.updateCard(2L, expected);
        assertEquals(cardService.getCard(2L), expected);
    }

    @Test
    void updateInvalidId() {
        var expected = new Card(2L, "Laptop", 15000, "laptop.png");

        assertThrows(CardNotFoundException.class,
                () -> cardService.updateCard(999L, expected));
    }
}
