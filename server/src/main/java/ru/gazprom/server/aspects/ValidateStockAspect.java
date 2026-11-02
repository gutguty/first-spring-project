package ru.gazprom.server.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.validator.StockValidation;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidateStockAspect {
    private final StockValidation stockValidation;

    @Around("@annotation(ru.gazprom.server.annotation.ValidateStock)")
    public Mono<Response<StockDTO>> validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Mono<Response<StockDTO>> mono = (Mono<Response<StockDTO>>) joinPoint.proceed();
        String methodName = joinPoint.getSignature().getName();

        return mono.flatMap(response -> {
            if (response.isSuccess() && response.getData() != null) {
                return stockValidation.validate(response.getData(), methodName);
            }

            return Mono.just(response);
        });
    }
}
