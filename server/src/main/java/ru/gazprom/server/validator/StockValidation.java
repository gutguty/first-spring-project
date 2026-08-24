package ru.gazprom.server.validator;


import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.exception.InvalidStockResponseException;

@Component
public class StockValidation {

    public Mono<StockDTO> validate(StockDTO stockDTO) {
        if (stockDTO == null) {
            throw new InvalidStockResponseException("Response is null");
        }

        if (stockDTO.getCardId() == null) {
            throw new InvalidStockResponseException("CardId is null");
        }

        if (stockDTO.getQuantity() == null || stockDTO.getQuantity() < 0) {
            throw new InvalidStockResponseException("Quantity is negative = " + stockDTO.getQuantity());
        }

        if (stockDTO.getReserved() == null || stockDTO.getReserved() < 0) {
            throw new InvalidStockResponseException("Reserved is negative = " + stockDTO.getReserved());
        }

        if (stockDTO.getQuantity() - stockDTO.getReserved() < 0) {
            throw new InvalidStockResponseException("Reserved " + stockDTO.getReserved() +" is less than Quantity " + stockDTO.getQuantity());
        }

        return Mono.just(stockDTO);
    }
}
