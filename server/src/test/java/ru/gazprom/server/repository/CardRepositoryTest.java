package ru.gazprom.server.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.model.Category;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category shoes;
    private Category clothes;

    @BeforeEach
    void setData() {
        cardRepository.deleteAll();
        categoryRepository.deleteAll();

        shoes = categoryRepository.save(new Category(null, "Shoes"));
        clothes = categoryRepository.save(new Category(null, "Clothes"));
        cardRepository.save(new Card(null, "Sneakers", 1000, "sneakers.png", shoes));
        cardRepository.save(new Card(null, "Boots", 2000, "boots.png", shoes));
        cardRepository.save(new Card(null, "Shirts", 3000, "shirts.png", clothes));
    }

    @Test
    void findByCategoryId() {
        List<Card> cards = cardRepository.findByCategoryId(shoes.getId());

        assertEquals(2, cards.size());
        assertTrue(cards.stream().allMatch(card -> card.getCategory().getId().equals(shoes.getId())));
    }

    @Test
    void findByCategoryIdNoMatches() {
        List<Card> cards = cardRepository.findByCategoryId(999L);

        assertTrue(cards.isEmpty());
    }

    @Test
    void findByCategoryName() {
        List<Card> cards = cardRepository.findByCategoryName("Clothes");

        assertEquals(1, cards.size());
        assertEquals("Shirts", cards.getFirst().getTitle());
    }

    @Test
    void findByCategoryNameNoMatches() {
        List<Card> cards = cardRepository.findByCategoryName("Nothing");

        assertTrue(cards.isEmpty());
    }

    @Test
    void findByPriceBetween() {
        List<Card> cards = cardRepository.findByPriceBetween(1500, 3000);
        assertEquals(2, cards.size());
        assertEquals("Boots", cards.getFirst().getTitle());
    }

    @Test
    void findByPriceBetweenNoMatch() {
        List<Card> cards = cardRepository.findByPriceBetween(15000, 30000);
        assertTrue(cards.isEmpty());
    }
}