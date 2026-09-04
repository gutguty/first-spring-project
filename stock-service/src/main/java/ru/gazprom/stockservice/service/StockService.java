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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return new Response<>(LocalDateTime.now(), "getAllStock", true, stocks, List.of());
    }

    public Response<Stock> getStockById(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);

        if (stock.isEmpty()) {
            log.error("Stock in method getStockById NOT FOUND with id = {}", id);
            return new Response<>(LocalDateTime.now(), "getStockById",false, null,
                    List.of(new StockNotFoundException("Stock with id " + id + " not found")));
        }

        return new Response<>(LocalDateTime.now(), "getStockById", true, stock.get(), List.of());
    }

    public Response<Stock> getStockByCardId(Long cardId, String user) {
        if (!allowedUsers.getAllowedUsers().contains(user)) {
            log.error("FORBIDDEN in method getStockByCardId for user = {}", user);
            return new Response<>(LocalDateTime.now(), "getStockByCardId",false, null,
                    List.of(new ForbiddenException("Access for " + user + " user is not allowed")));
        }

        Optional<Stock> stock = stockRepository.findByCardId(cardId);
        if (stock.isEmpty()) {
            log.error("Stock in method getStockByCardId NOT FOUND with cardId = {}", cardId);
            return new Response<>(LocalDateTime.now(), "getStockByCardId",false, null,
                    List.of(new StockNotFoundException("Stock with cardId " + cardId + " not found")));
        }


        return new Response<>(LocalDateTime.now(), "getStockByCardId",true, stock.get(), List.of());
    }

    @Transactional
    public Response<Stock> createStockById(Stock stock, String user) {

        List<ValidationException> resultErrors = createStockValidation.validate(stock, user);

        if (!resultErrors.isEmpty()) {
            log.error("Validation FAILED in method createStockById in creating stock by {}",resultErrors.stream().map(ValidationException::getMessage).toList());
            return new Response<>(LocalDateTime.now(), "createStockById",false, null, resultErrors);
        }

        stockRepository.save(stock);
        return new Response<>(LocalDateTime.now(), "createStockById",true, stock, List.of());
    }

    public Response<Void> deleteStockById(Long id) {

        if (!stockRepository.existsById(id)) {
            log.error("Stock in method deleteStockById NOT FOUND with id = {}", id);
            return new Response<>(LocalDateTime.now(), "deleteStockById",false, null,
                    List.of(new StockNotFoundException("Stock with id " + id + " not found")));
        }

        stockRepository.deleteById(id);
        return new Response<>(LocalDateTime.now(), "deleteStockById", true, null, List.of());
    }

    @Transactional
    public Response<Stock> updateStockById(Long id, Stock newStock) {
        Optional<Stock> existStockOptional = stockRepository.findById(id);
        if (existStockOptional.isEmpty()) {
            log.error("Stock in method updateStockById NOT FOUND with id = {}", id);
            return new Response<>(LocalDateTime.now(), "updateStockById", false, null,
                    List.of(new StockNotFoundException("Stock with id " + id + " not found")));
        }

        List<ValidationException> errors = updateStockValidation.validate(newStock);
        if (!errors.isEmpty()) {
            log.error("ERRORS in method updateStockById {}", errors.stream().map(ValidationException::getMessage));
            return new Response<>(LocalDateTime.now(), "updateStockById", false, null, errors);
        }

        Stock existStock = existStockOptional.get();
        existStock.setQuantity(newStock.getQuantity());
        existStock.setReserved(newStock.getReserved());

        Stock updated = stockRepository.save(existStock);
        return new Response<>(LocalDateTime.now(), "updateStockById", true, updated, List.of());
    }

    public Response<List<Stock>> getStockLessLimit(Integer limit) {
        Predicate<Stock> isEnabledForReserve = stock -> (stock.getQuantity() - stock.getReserved()) < limit;

        List<Stock> limitStockItems = stockRepository.findAll().stream()
                .filter(isEnabledForReserve)
                .toList();

        return new Response<>(LocalDateTime.now(), "getStockLessLimit", true, limitStockItems, List.of());
    }
}
