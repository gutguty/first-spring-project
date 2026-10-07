package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.dto.PaymentRequest;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.PaymentService;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public Response<PaymentDTO> paymentProcess(@RequestBody PaymentRequest request) {
        return paymentService.paymentProcess(request.getPaymentType(), request.getAmount());
    }
}