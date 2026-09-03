package ru.gazprom.stockservice.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.exception.*;
import ru.gazprom.stockservice.model.Stock;
import ru.gazprom.stockservice.repository.StockRepository;
import ru.gazprom.stockservice.users.AllowedUsers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CreateStockValidation {

    private final StockRepository stockRepository;
    private final AllowedUsers allowedUsers;

    public CreateStockValidation(StockRepository stockRepository, AllowedUsers allowedUsers) {
        this.stockRepository = stockRepository;
        this.allowedUsers = allowedUsers;
    }

    public List<ValidationException> validate(Stock stock, String user) {
        List<ValidationException> result = new ArrayList<>();

        if (!allowedUsers.getAllowedUsers().contains(user)) {
            result.add(new ForbiddenException("Access for user " + user + " is not allowed"));
        }

        if (stock.getCardId() == null) {
            result.add(new FieldRequiredException("cardId"));
        }

        if (stock.getQuantity() == null) {
            result.add(new FieldRequiredException("Quantity"));
        } else if (stock.getQuantity() < 0) {
            result.add(new NegativeValueException("Quantity", stock.getQuantity()));
        }

        if (stock.getReserved() == null) {
            result.add(new FieldRequiredException("Reserved"));
        } else if (stock.getReserved() < 0) {
            result.add(new NegativeValueException("Reserved", stock.getReserved()));
        }

        if (stock.getReserved() > stock.getQuantity()) {
            result.add(new QuantityReservedException(stock.getQuantity(), stock.getReserved()));
        }

        if (stockRepository.existsStockByCardId(stock.getCardId())) {
            result.add(new AlreadyExistsException(stock.getCardId()));
        }
        return result;
    }
}