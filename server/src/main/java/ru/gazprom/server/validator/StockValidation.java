package ru.gazprom.server.validator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.exception.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;
@Component
@Slf4j
public class StockValidation {

    public Mono<Response<StockDTO>> validate(StockDTO stockDTO, String methodName) {

        List<ValidationError> listErrors = new ArrayList<>();

        if (stockDTO.getCardId() == null) {
            listErrors.add(new FieldRequiredError("cardId"));
        }

        if (stockDTO.getQuantity() == null) {
            listErrors.add(new FieldRequiredError("Quantity"));
        } else if (stockDTO.getQuantity() < 0) {
            listErrors.add(new NegativeValueError("Quantity", stockDTO.getQuantity()));
        }

        if (stockDTO.getReserved() == null) {
            listErrors.add(new FieldRequiredError("Reserved"));
        } else if (stockDTO.getReserved() < 0) {
            listErrors.add(new NegativeValueError("Reserved", stockDTO.getReserved()));
        }

        if (stockDTO.getReserved() != null && stockDTO.getQuantity() != null
                && stockDTO.getReserved() > stockDTO.getQuantity()) {
            listErrors.add(new QuantityReservedError(stockDTO.getQuantity(), stockDTO.getReserved()));
        }

        if (!listErrors.isEmpty()) {
            log.error("Validation errors in stock={}", listErrors.stream()
                    .map(ValidationError::getMessage)
                    .toList()
            );

            return Mono.just(responseError(methodName, listErrors));
        }

        return Mono.just(responseSuccess(methodName, stockDTO));
    }
}
