package ru.gazprom.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
    public List<CardDTO> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardService.getCard(id);
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
    public CardDTO updateCard(@PathVariable Long id, @RequestBody Card card) {
        return cardService.updateCard(id, card);
    }


}
