package ru.gazprom.server.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.gazprom.server.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.exception.FieldRequiredException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationException;
import ru.gazprom.server.mapper.CardMapper;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.model.Category;
import ru.gazprom.server.repository.CardRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    private static final BigDecimal DISCOUNT_LIMIT = new BigDecimal("10000");
    private static final BigDecimal DISCOUNT_COEFFICENT = new BigDecimal("0.95");


    public Response<List<CardDTO>> getAllCards() {
        Function<Card, CardDTO> calculateDiscount = card -> {
            CardDTO cardDTO = cardMapper.CardToDto(card);
            if (cardDTO.getPrice().compareTo(DISCOUNT_LIMIT) > 0) {
                cardDTO.setPrice(cardDTO.getPrice().multiply(DISCOUNT_COEFFICENT));
            }
            return cardDTO;
        };

        List<CardDTO> cards = cardRepository.findAll().stream()
                .map(calculateDiscount)
                .collect(Collectors.toList());
        return new Response<>(LocalDateTime.now(), "getAllCards", true, cards, List.of());
    }

    public Response<CardDTO> getCardById(Long id) {
        return cardRepository.findById(id)
                .map(card -> new Response<>(LocalDateTime.now(), "getCardById", true, cardMapper.CardToDto(card), List.of()))
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "getCardById", false, null,
                        List.of(new CardNotFoundException("Card with " + id + " id not found"))));
    }


    @Transactional
    public Response<CardDTO> createCard(Card card) {
        Consumer<Card> logCreateCard = c -> log.info("Card created title={}, price={}", c.getTitle(), c.getPrice());
        List<ValidationException> errors = new ArrayList<>();

        if (card.getTitle() == null || card.getTitle().isBlank()) {
            errors.add(new FieldRequiredException("title"));
        }
        if (card.getPrice() == null || card.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new FieldRequiredException("price"));
        }
        if (!errors.isEmpty()) {
            return new Response<>(LocalDateTime.now(), "createCard", false, null, errors);
        }
        Card savedCard = cardRepository.save(card);
        logCreateCard.accept(savedCard);
        return new Response<>(LocalDateTime.now(), "createCard", true, cardMapper.CardToDto(savedCard), List.of());
    }

    public Response<Void> deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            return new Response<>(LocalDateTime.now(), "deleteCard", false, null,
                    List.of(new CardNotFoundException("Card with " + id + " id not found")));
        }
        cardRepository.deleteById(id);
        return new Response<>(LocalDateTime.now(), "deleteCard", true, null, List.of());
    }

    @Transactional
    public Response<CardDTO> updateCardById(Long id, Card newCard) {
        return cardRepository.findById(id)
                .map(existCard -> {
                    existCard.setTitle(newCard.getTitle());
                    existCard.setPrice(newCard.getPrice());
                    existCard.setImage(newCard.getImage());
                    Card updated = cardRepository.save(existCard);
                    return new Response<>(LocalDateTime.now(), "updateCardById", true, cardMapper.CardToDto(updated), List.of());
                })
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "updateCardById", false, null,
                        List.of(new CardNotFoundException("Card with " + id + " not found"))));
    }
}
