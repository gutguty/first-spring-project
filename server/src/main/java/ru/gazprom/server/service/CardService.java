package ru.gazprom.server.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.gazprom.server.dto.CardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.CardNotFoundError;
import ru.gazprom.server.exception.FieldRequiredError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;
import ru.gazprom.server.mapper.CardMapper;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.CardRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

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

        return responseSuccess("getAllCards", cards);
    }

    public Response<CardDTO> getCardById(Long id) {
        return cardRepository.findById(id)
                .map(card -> responseSuccess("getCardById", cardMapper.CardToDto(card)))
                .orElseGet(() -> responseError("getCardById", new CardNotFoundError("Card with " + id + " id not found")));
    }


    @Transactional
    public Response<CardDTO> createCard(Card card) {
        Consumer<Card> logCreateCard = c -> log.info("Card created title={}, price={}", c.getTitle(), c.getPrice());
        List<ValidationError> errors = new ArrayList<>();

        if (card.getTitle() == null || card.getTitle().isBlank()) {
            errors.add(new FieldRequiredError("title"));
        }
        if (card.getPrice() == null || card.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new FieldRequiredError("price"));
        }
        if (!errors.isEmpty()) {
            return responseError("createCard", errors);
        }
        Card savedCard = cardRepository.save(card);
        logCreateCard.accept(savedCard);
        return responseSuccess("createCard", cardMapper.CardToDto(savedCard));
    }

    public Response<Void> deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            return responseError("deleteCard", new CardNotFoundError("Card with " + id + " id not found"));
        }
        cardRepository.deleteById(id);
        return responseSuccess("deleteCard", null);
    }

    @Transactional
    public Response<CardDTO> updateCardById(Long id, Card newCard) {
        return cardRepository.findById(id)
                .map(existCard -> {
                    existCard.setTitle(newCard.getTitle());
                    existCard.setPrice(newCard.getPrice());
                    existCard.setImage(newCard.getImage());
                    Card updated = cardRepository.save(existCard);
                    return responseSuccess("updateCardById", cardMapper.CardToDto(updated));
                })
                .orElseGet(() -> responseError("updateCardById", new CardNotFoundError("Card with " + id + " not found")));
    }

//    Нейминг должен совпадать с методом в репозитории ?
    public Optional<Card> findCardById(Long cardId) {
        return cardRepository.findById(cardId);
    }

}
