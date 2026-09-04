package ru.gazprom.stockservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gazprom.stockservice.exception.*;
import ru.gazprom.stockservice.model.Stock;
import ru.gazprom.stockservice.repository.StockRepository;
import ru.gazprom.stockservice.users.AllowedUsers;
import ru.gazprom.stockservice.validator.CreateStockValidation;
import ru.gazprom.stockservice.validator.UpdateStockValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static ru.gazprom.stockservice.utils.ResponseUtils.responseError;
import static ru.gazprom.stockservice.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockRepository stockRepository;
    private final AllowedUsers allowedUsers;
    private final CreateStockValidation createStockValidation;
    private final UpdateStockValidation updateStockValidation;

    public Response<List<Stock>> getAllStock() {
        List<Stock> stocks = stockRepository.findAll();
        return responseSuccess("getAllStock", stocks);
    }

    public Response<Stock> getStockById(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);

        if (stock.isEmpty()) {
            log.error("Stock in method getStockById NOT FOUND with id = {}", id);
            return responseError("getStockById", new StockNotFoundError("Stock with id " + id + " not found"));
        }

        return responseSuccess("getStockById", stock.get());
    }

    public Response<Stock> getStockByCardId(Long cardId, String user) {
        if (!allowedUsers.getAllowedUsers().contains(user)) {
            log.error("FORBIDDEN in method getStockByCardId for user = {}", user);
            return responseError("getStockByCardId", new ForbiddenError("Access for " + user + " user is not allowed"));
        }

        Optional<Stock> stock = stockRepository.findByCardId(cardId);
        if (stock.isEmpty()) {
            log.error("Stock in method getStockByCardId NOT FOUND with cardId = {}", cardId);
            return responseError("getStockByCardId", new StockNotFoundError("Stock with cardId " + cardId + " not found"));
        }

        return responseSuccess("getStockByCardId", stock.get());
    }

    @Transactional
    public Response<Stock> createStockById(Stock stock, String user) {

        List<ValidationError> resultErrors = createStockValidation.validate(stock, user);

        if (!resultErrors.isEmpty()) {
            log.error("Validation FAILED in method createStockById in creating stock by {}",resultErrors.stream().map(ValidationError::getMessage).toList());
            return responseError("createStockById", resultErrors);
        }

        stockRepository.save(stock);
        return responseSuccess("createStockById", stock);
    }

    public Response<Void> deleteStockById(Long id) {

        if (!stockRepository.existsById(id)) {
            log.error("Stock in method deleteStockById NOT FOUND with id = {}", id);
            return responseError("deleteStockById", new StockNotFoundError("Stock with id " + id + " not found"));
        }

        stockRepository.deleteById(id);
        return responseSuccess("deleteStockById", null);
    }

    @Transactional
    public Response<Stock> updateStockById(Long id, Stock newStock) {
        Optional<Stock> existStockOptional = stockRepository.findById(id);
        if (existStockOptional.isEmpty()) {
            log.error("Stock in method updateStockById NOT FOUND with id = {}", id);
            return responseError("updateStockById", new StockNotFoundError("Stock with id " + id + " not found"));
        }

        List<ValidationError> errors = updateStockValidation.validate(newStock);
        if (!errors.isEmpty()) {
            log.error("ERRORS in method updateStockById {}", errors.stream().map(ValidationError::getMessage));
            return responseError("updateStockById", errors);
        }

        Stock existStock = existStockOptional.get();
        existStock.setQuantity(newStock.getQuantity());
        existStock.setReserved(newStock.getReserved());

        Stock updated = stockRepository.save(existStock);
        return responseSuccess("updateStockById", updated);
    }

    public Response<List<Stock>> getStockLessLimit(Integer limit) {
        Predicate<Stock> isEnabledForReserve = stock -> (stock.getQuantity() - stock.getReserved()) < limit;

        List<Stock> limitStockItems = stockRepository.findAll().stream()
                .filter(isEnabledForReserve)
                .toList();

        return responseSuccess("getStockLessLimit", limitStockItems);
    }
}
