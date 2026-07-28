package ru.gazprom.server.service;

import ru.gazprom.server.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.CardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardDTO toDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getPrice(),
                card.getImage()
        );
    }

    public List<CardDTO> getAll() {
        return cardRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CardDTO getCard(Long id) {
        return cardRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new CardNotFoundException("Card with " + id + " id not found"));
    }

    public CardDTO createCard(Card card) {
        Card savedCard = cardRepository.save(card);
        return toDto(savedCard);
    }

    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException("Card with " + id + " id not found");
        }
        cardRepository.deleteById(id);
    }

    public CardDTO updateCard(Long id, Card newCard) {
        Card existCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(
                        "Card with " + id + " not found"));

        existCard.setTitle(newCard.getTitle());
        existCard.setPrice(newCard.getPrice());
        existCard.setImage(newCard.getImage());

        return toDto(cardRepository.save(existCard));
    }
}
