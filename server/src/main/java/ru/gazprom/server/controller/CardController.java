package ru.gazprom.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/cards")
    public List<CardDTO> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }

    @PostMapping("/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody Card card) {
        return cardService.createCard(card);
    }

    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @PutMapping("/cards/{id}")
    public CardDTO updateCardById(@PathVariable Long id, @RequestBody Card card) {
        return cardService.updateCardById(id, card);
    }


}
