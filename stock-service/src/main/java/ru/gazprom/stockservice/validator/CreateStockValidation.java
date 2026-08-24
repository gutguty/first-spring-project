package ru.gazprom.stockservice.validator;

import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.model.Stock;
import ru.gazprom.stockservice.repository.StockRepository;

@Component
public class CreateStockValidation {

    private final StockRepository stockRepository;

    public CreateStockValidation(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void validate(Stock stock) {
        if (stock.getCardId() == null || stock.getQuantity() == null || stock.getReserved() == null) {
            throw new IllegalArgumentException("Field null");
        }
        if (stock.getQuantity() < 0 || stock.getReserved() < 0) {
            throw new IllegalArgumentException("Quantity and reserved is negative");
        }
        if (stock.getReserved() > stock.getQuantity()) {
            throw new IllegalArgumentException("Reserved bigger than quantity");
        }
        if (stockRepository.existsStockByCardId(stock.getCardId())) {
            throw new IllegalArgumentException("Stock with cardId " + stock.getCardId() + " already exists");
        }
    }
}