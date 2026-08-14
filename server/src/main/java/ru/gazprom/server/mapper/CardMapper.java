package ru.gazprom.server.mapper;


import org.springframework.stereotype.Component;
import ru.gazprom.server.dto.CardDTO;
import ru.gazprom.server.model.Card;

@Component
public class CardMapper {
    public CardDTO CardToDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getPrice(),
                card.getImage()
        );
    }
}
