package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.AddressRepository;
import ru.gazprom.server.repository.CardRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final CardRepository cardRepository;
    private final AddressRepository addressRepository;

    public Response<OrderDTO> order(Long cardId, Long addressId, DeliveryType deliveryType, PaymentType paymentType) {

        return cardRepository.findById(cardId)
                .map(card -> addressRepository.findById(addressId)
                                .<Response<OrderDTO>>map(address -> {
                                    Response<BigDecimal> deliveryPrice = deliveryService.calculatePrice(cardId, addressId, deliveryType);
                                    Response<PaymentDTO> paymentResult = paymentService.paymentProcess(paymentType, deliveryPrice.getData());
                                    if (!paymentResult.isSuccess()) {
                                        return new Response<>(LocalDateTime.now(), "order", false, null, paymentResult.getListErrors());
                                    }
                                    return new Response<>(LocalDateTime.now(), "order", true,
                                            new OrderDTO(deliveryPrice.getData(), paymentResult.getData()), List.of());
                                })
                                .orElseGet(() -> new Response<>(LocalDateTime.now(), "order", false, null,
                                        List.of(new AddressNotFoundException("Address with id " + addressId + " is not found"))))
                )
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "order", false, null,
                        List.of(new CardNotFoundException("Card with id " + cardId + " is not found"))));
    }
}
