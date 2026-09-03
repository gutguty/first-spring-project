package ru.gazprom.server.validator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.exception.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StockValidation {

    public Mono<Response<StockDTO>> validate(StockDTO stockDTO, String methodName) {

        List<ValidationException> listErrors = new ArrayList<>();

        if (stockDTO.getCardId() == null) {
            listErrors.add(new FieldRequiredException("cardId"));
        }

        if (stockDTO.getQuantity() == null) {
            listErrors.add(new FieldRequiredException("Quantity"));
        } else if (stockDTO.getQuantity() < 0) {
            listErrors.add(new NegativeValueException("Quantity", stockDTO.getQuantity()));
        }

        if (stockDTO.getReserved() == null) {
            listErrors.add(new FieldRequiredException("Reserved"));
        } else if (stockDTO.getReserved() < 0) {
            listErrors.add(new NegativeValueException("Reserved", stockDTO.getReserved()));
        }

        if (stockDTO.getReserved() != null && stockDTO.getQuantity() != null
                && stockDTO.getReserved() > stockDTO.getQuantity()) {
            listErrors.add(new QuantityReservedException(stockDTO.getQuantity(), stockDTO.getReserved()));
        }

        if (!listErrors.isEmpty()) {
            log.error("Validation errors in stock={}", listErrors.stream()
                    .map(ValidationException::getMessage)
                    .toList()
            );

            return Mono.just(new Response<>(LocalDateTime.now(), methodName, false, null, listErrors));
        }

        return Mono.just(new Response<>(LocalDateTime.now(), methodName, true, stockDTO, List.of()));
    }
}
