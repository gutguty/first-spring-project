package ru.gazprom.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.exception.ExceptionResponse;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
    public List<Card> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/cards/{id}")
    public Card getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PostMapping("/cards")
    public Card createCard(@RequestBody Card card) {
        return cardService.createCard(card);
    }

    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @PutMapping("/cards/{id}")
    public Card updateCard(@PathVariable Long id, @RequestBody Card card) {
        return cardService.updateCard(id, card);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCardNotFoundException(CardNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Card not found"), HttpStatus.NOT_FOUND);
    }
}
