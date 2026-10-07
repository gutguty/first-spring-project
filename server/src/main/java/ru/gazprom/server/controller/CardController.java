package ru.gazprom.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.enums.SortType;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/cards")
    public Response<List<CardDTO> > getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/cards/{id}")
    public Response<CardDTO> getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }

    @PostMapping("/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<CardDTO> createCard(@RequestBody Card card) {
        return cardService.createCard(card);
    }

    @DeleteMapping("/cards/{id}")
    public Response<Void> deleteCard(@PathVariable Long id) {
        return cardService.deleteCard(id);
    }

    @PutMapping("/cards/{id}")
    public Response<CardDTO> updateCardById(@PathVariable Long id, @RequestBody Card card) {
        return cardService.updateCardById(id, card);
    }

    @GetMapping("/cards/sorted")
    public Response<List<CardDTO>> getAllCardsSorted(@RequestParam SortType sortType) {
        return cardService.getAllCardsSorted(sortType);
    }

    @GetMapping("/cards/sorted/price")
    public Response<List<CardDTO>> getAllCardsSortedByPrice(@RequestParam SortType sortType) {
        return cardService.getAllCardsSortedByPrice(sortType);
    }

    @GetMapping("/cards/sorted/title")
    public Response<List<CardDTO>> getAllCardsSortedByTitle(@RequestParam SortType sortType) {
        return cardService.getAllCardsSortedByTitle(sortType);
    }
}
