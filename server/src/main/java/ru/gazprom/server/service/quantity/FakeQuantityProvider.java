package ru.gazprom.server.service.quantity;


import org.springframework.stereotype.Component;

@Component("fakeQuantity")
public class FakeQuantityProvider implements QuantityProvider {

    @Override
    public Integer getQuantity(Long cardId) {
        return 10;
    }
}
