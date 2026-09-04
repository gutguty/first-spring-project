package ru.gazprom.stockservice.validator;

import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.exception.FieldRequiredException;
import ru.gazprom.stockservice.exception.NegativeValueException;
import ru.gazprom.stockservice.exception.QuantityReservedException;
import ru.gazprom.stockservice.exception.ValidationException;
import ru.gazprom.stockservice.model.Stock;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateStockValidation {

    public List<ValidationException> validate(Stock newStock) {
        List<ValidationException> result = new ArrayList<>();

        if (newStock.getQuantity() == null) {
            result.add(new FieldRequiredException("Quantity"));
        } else if (newStock.getQuantity() < 0) {
            result.add(new NegativeValueException("Quantity", newStock.getQuantity()));
        }

        if (newStock.getReserved() == null) {
            result.add(new FieldRequiredException("Reserved"));
        } else if (newStock.getReserved() < 0) {
            result.add(new NegativeValueException("Reserved", newStock.getReserved()));
        }

        if (newStock.getReserved() > newStock.getQuantity()) {
            result.add(new QuantityReservedException(newStock.getQuantity(), newStock.getReserved()));
        }

        return result;
    }
}