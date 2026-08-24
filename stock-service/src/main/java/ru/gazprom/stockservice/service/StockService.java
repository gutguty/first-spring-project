package ru.gazprom.stockservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.stockservice.exception.ForbiddenException;
import ru.gazprom.stockservice.exception.StockNotFoundException;
import ru.gazprom.stockservice.model.Stock;
import ru.gazprom.stockservice.repository.StockRepository;
import ru.gazprom.stockservice.users.AllowedUsers;
import ru.gazprom.stockservice.validator.CreateStockValidation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final AllowedUsers allowedUsers;
    private final CreateStockValidation createStockValidation;

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException("Stock with " + id + " id not found"));
    }

    public Stock getStockByCardId(Long cardId, String user) {
        if (!allowedUsers.getAllowedUsers().contains(user)) {
            throw new ForbiddenException("Access for " + user + " user is not allowed");
        }

        return stockRepository.findByCardId(cardId).orElseThrow(() -> new StockNotFoundException("Stock with " + cardId + " id not found"));
    }

    @Transactional
    public Stock createStockById(Stock stock, String user) {
        if (!allowedUsers.getAllowedUsers().contains(user)) {
            throw new ForbiddenException("Access for " + user + " user is not allowed");
        }

        createStockValidation.validate(stock);

        return stockRepository.save(stock);
    }

    public void deleteStockById(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new StockNotFoundException("Stock with " + id + " id not found");
        }

        stockRepository.deleteById(id);
    }

    @Transactional
    public Stock updateStockById(Long id, Stock newStock) {
        Stock existStock = stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException("Stock with " + id + " is not found"));

        if (newStock.getQuantity() != null) {
            existStock.setQuantity(newStock.getQuantity());
        }

        if (newStock.getReserved() != null) {
            existStock.setReserved(newStock.getReserved());
        }

        return stockRepository.save(existStock);
    }
}
