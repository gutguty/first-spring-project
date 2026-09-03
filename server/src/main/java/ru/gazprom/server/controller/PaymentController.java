package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.PaymentService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payment")
    public Response<PaymentDTO> paymentProcess(@RequestParam PaymentType paymentType, @RequestParam BigDecimal amount) {
        return paymentService.paymentProcess(paymentType, amount);
    }
}