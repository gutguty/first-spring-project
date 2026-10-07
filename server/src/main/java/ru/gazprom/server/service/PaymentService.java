package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.payment.PaymentFactory;
import ru.gazprom.server.payment.PaymentStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentFactory paymentFactory;

    public Response<PaymentDTO> paymentProcess(PaymentType paymentType, BigDecimal amount) {
        PaymentStrategy strategy = paymentFactory.getStrategy(paymentType);
        PaymentDTO paymentDTO = strategy.paymentProcess(amount);
        return responseSuccess("paymentProcess", paymentDTO);
    }
}