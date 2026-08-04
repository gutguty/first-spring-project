package ru.gazprom.server.service;

import jakarta.transaction.Transactional;
import ru.gazprom.server.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.mapper.CardMapper;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.CardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public List<CardDTO> getAll() {
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    public CardDTO getCard(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::toDto)
                .orElseThrow(() -> new CardNotFoundException("Card with " + id + " id not found"));
    }

    public CardDTO createCard(Card card) {
        if (card.getTitle() == null || card.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");

        }

        if (card.getPrice() == null || card.getPrice() <= 0) {
            throw new IllegalArgumentException("Price is invalid");
        }

        Card savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    @Transactional
    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException("Card with " + id + " id not found");
        }
        cardRepository.deleteById(id);
    }

    @Transactional
    public CardDTO updateCard(Long id, Card newCard) {
        Card existCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(
                        "Card with " + id + " not found"));

        existCard.setTitle(newCard.getTitle());
        existCard.setPrice(newCard.getPrice());
        existCard.setImage(newCard.getImage());

        return cardMapper.toDto(cardRepository.save(existCard));
    }
}
