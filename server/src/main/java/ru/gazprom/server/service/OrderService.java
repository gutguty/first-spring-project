package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.AddressNotFoundError;
import ru.gazprom.server.exception.CardNotFoundError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Card;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;



@Service
@RequiredArgsConstructor
public class OrderService {
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final CardService cardService;
    private final AddressService addressService;

    private Response<OrderDTO> calculatePaymentAmount(Card card, Long addressId, DeliveryType deliveryType, PaymentType paymentType) {
        return addressService.findAddressById(addressId)
            .<Response<OrderDTO>>map(address -> {

                Response<BigDecimal> deliveryPrice = deliveryService.calculatePrice(card, addressId, deliveryType);
                Response<PaymentDTO> paymentResult = paymentService.paymentProcess(paymentType, deliveryPrice.getData());

                if (!paymentResult.isSuccess()) {
                    return responseError("calculatePaymentAmount", paymentResult.getListErrors());
                }

                OrderDTO orderDTO = new OrderDTO(deliveryPrice.getData(), paymentResult.getData());
                return responseSuccess("calculatePaymentAmount", orderDTO);
            })
            .orElseGet(() -> responseError("calculatePaymentAmount", new AddressNotFoundError("Address with id " + addressId + " is not found")));
    }

    public Response<OrderDTO> order(Long cardId, Long addressId, DeliveryType deliveryType, PaymentType paymentType) {

        return cardService.findCardById(cardId)
                .map(card -> calculatePaymentAmount(card, addressId, deliveryType, paymentType))
                .orElseGet(() -> responseError("order", new CardNotFoundError("Card with id " + cardId + " is not found")));
    }
}
