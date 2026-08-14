package ru.gazprom.server.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.CardRepository;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setCards() {
        cardRepository.deleteAll();
        cardRepository.save(new Card(null, "Shoes", 1000, "shoes.png", null));
        cardRepository.save(new Card(null, "Shirts", 2000, "shirts.png", null));
        cardRepository.save(new Card(null,"Trousers", 3000, "trousers.png", null));
    }

    //Create test card
    @Test
    void createCardSuccess() {
        CardDTO created = cardService.createCard(new Card(null, "Jacket", 5000, "jacket.png", null));
        assertEquals(4, cardService.getAllCards().size());
        assertEquals("Jacket", created.getTitle());
        assertNotNull(created.getId());
    }

    @Test
    void createCardEmptyTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "", 2000, "shirts.png", null)));
    }

    @Test
    void createCardSpaceTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "    ", 2000, "shirts.png", null)));
    }

    @Test
    void createCardZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "Shirts", 0, "shirts.png", null)));
    }

    @Test
    void createCardNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "Shirts", -100, "shirts.png", null)));
    }

    @Test
    void createCardNullTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, null, 1000, "shoes.png", null)));
    }

    @Test
    void createCardNullPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> cardService.createCard(new Card(null, "Shirt", null, "shoes.png", null)));
    }

    //getTestCard
    @Test
    void getAllCardsTest() {
        var result = cardService.getAllCards();
        assertEquals(3, result.size());
    }

    @Test
    void getCardById() {
        Long id = cardRepository.findAll().getFirst().getId();
        CardDTO result = cardService.getCardById(id);
        assertEquals("Shoes", result.getTitle());
        assertEquals(1000, result.getPrice());
    }

    @Test
    void getInvalidId() {
        assertThrows(CardNotFoundException.class,
                () -> cardService.getCardById(999L));
    }

    //deleteTestCard
    @Test
    void deleteCardById() {
        Long id = cardRepository.findAll().getFirst().getId();
        cardService.deleteCard(id);
        assertEquals(2, cardService.getAllCards().size());
    }

    @Test
    void deleteCardByIdNotFoundCard() {
        Long id = cardRepository.findAll().getFirst().getId();
        cardService.deleteCard(id);
        assertThrows(CardNotFoundException.class,
                () -> cardService.getCardById(id));
    }

    @Test
    void deleteInvalidId() {
        assertThrows(CardNotFoundException.class,
                () -> cardService.deleteCard(999L));
    }

    //updateTestCard
    @Test
    void updateCardById() {
        Long id = cardRepository.findAll().getFirst().getId();
        var newCard = new Card(null, "Laptop", 15000, "laptop.png", null);
        var updatedCard = cardService.updateCardById(id, newCard);
        assertEquals(id, updatedCard.getId());
        assertEquals("Laptop", updatedCard.getTitle());
        assertEquals(15000, updatedCard.getPrice());
    }

    @Test
    void updateInvalidId() {
        var expected = new Card(2L, "Laptop", 15000, "laptop.png", null);

        assertThrows(CardNotFoundException.class,
                () -> cardService.updateCardById(999L, expected));
    }
}
