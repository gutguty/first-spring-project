package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.AddressNotFoundError;
import ru.gazprom.server.exception.CardNotFoundError;
import ru.gazprom.server.exception.OutboxError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Card;

import java.math.BigDecimal;
import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;



@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final CardService cardService;
    private final AddressService addressService;
    private final OutboxEventService outboxEventService;

    private Response<OrderDTO> calculatePaymentAmount(Card card, Long addressId, DeliveryType deliveryType, PaymentType paymentType) {
        return addressService.findAddressById(addressId)
            .<Response<OrderDTO>>map(address -> {

                Response<BigDecimal> deliveryPrice = deliveryService.calculatePrice(card, addressId, deliveryType);
                Response<PaymentDTO> paymentResult = paymentService.paymentProcess(paymentType, deliveryPrice.getData());

                if (!paymentResult.isSuccess()) {
                    return responseError("calculatePaymentAmount", paymentResult.getListErrors());
                }

                OrderDTO orderDTO = new OrderDTO(card.getId(), deliveryPrice.getData(), paymentResult.getData());
                return responseSuccess("calculatePaymentAmount", orderDTO);
            })
            .orElseGet(() -> responseError("calculatePaymentAmount", new AddressNotFoundError("Address with id " + addressId + " is not found")));
    }

    public Response<OrderDTO> order(Long cardId, Long addressId, DeliveryType deliveryType, PaymentType paymentType) {

        Response<OrderDTO> result = cardService.findCardById(cardId)
                .map(card -> calculatePaymentAmount(card, addressId, deliveryType, paymentType))
                .orElseGet(() -> responseError("order", new CardNotFoundError("Card with id " + cardId + " is not found")));


        if (!result.isSuccess()) {
            return result;
        }

        OrderDTO orderDTO = result.getData();
        Response<Void> outboxResult = outboxEventService.saveEvent("order-created-topic", orderDTO.getId().toString(), orderDTO);


        if (!outboxResult.isSuccess()) {
            log.error("Failed to save outbox event for order id = {}, {}", orderDTO.getId(), outboxResult.getListErrors());
            return responseError("order", new OutboxError("Failed to save outbox event for order"));
        }

        return result;
    }
}
