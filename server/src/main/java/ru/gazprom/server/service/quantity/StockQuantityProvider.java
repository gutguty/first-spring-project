package ru.gazprom.server.service.quantity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.StockService;

@Component
@Primary
@RequiredArgsConstructor
public class StockQuantityProvider implements QuantityProvider {

    private final StockService stockService;

    @Override
    public Integer getQuantity(Long cardId) {
        try {
            Response<StockDTO> response = stockService.getStockByCardId(cardId, "admin").block();
            if (response == null || !response.isSuccess() || response.getData() == null) {
                return null;
            }

            return response.getData().getQuantity();
        } catch (Exception e) {
            return null;
        }
    }
}
