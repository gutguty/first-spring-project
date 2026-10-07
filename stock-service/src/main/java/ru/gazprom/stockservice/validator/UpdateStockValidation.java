package ru.gazprom.stockservice.validator;

import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.exception.FieldRequiredError;
import ru.gazprom.stockservice.exception.NegativeValueError;
import ru.gazprom.stockservice.exception.QuantityReservedError;
import ru.gazprom.stockservice.exception.ValidationError;
import ru.gazprom.stockservice.model.Stock;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateStockValidation {

    public List<ValidationError> validate(Stock newStock) {
        List<ValidationError> result = new ArrayList<>();

        if (newStock.getQuantity() == null) {
            result.add(new FieldRequiredError("Quantity"));
        } else if (newStock.getQuantity() < 0) {
            result.add(new NegativeValueError("Quantity", newStock.getQuantity()));
        }

        if (newStock.getReserved() == null) {
            result.add(new FieldRequiredError("Reserved"));
        } else if (newStock.getReserved() < 0) {
            result.add(new NegativeValueError("Reserved", newStock.getReserved()));
        }

        if (newStock.getReserved() > newStock.getQuantity()) {
            result.add(new QuantityReservedError(newStock.getQuantity(), newStock.getReserved()));
        }

        return result;
    }
}