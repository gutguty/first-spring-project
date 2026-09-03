package ru.gazprom.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.dto.StockRequest;
import ru.gazprom.server.dto.StockServiceResponse;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationException;
import ru.gazprom.server.exception.WebCustomClientException;
import ru.gazprom.server.validator.StockValidation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class StockService {
    private final WebClient webClient;
    private final StockValidation stockValidation;

    public StockService(WebClient webClient, StockValidation stockValidation) {
        this.webClient = webClient;
        this.stockValidation = stockValidation;
    }



    public Mono<Response<StockDTO>> getStockByCardId(Long cardId, String user) {

        ParameterizedTypeReference<Response<StockDTO>> typeRef = new ParameterizedTypeReference<>() {

        };


        return webClient
                .get()
                .uri("/api/stock/card/{cardId}" , cardId)
                .header("User-Auth", user)
                .retrieve()
                .bodyToMono(typeRef)
                .timeout(Duration.ofSeconds(3))
                .flatMap(wrapper -> {
                    if (!wrapper.isSuccess()) {
                        List<ValidationException> listErrors = wrapper.getListErrors();
                        log.error("stock-service returned error: {}", listErrors);

                        List<ValidationException> errors = listErrors.stream()
                                .map(e -> new WebCustomClientException(e.getMessage(), HttpStatus.BAD_REQUEST))
                                .collect(Collectors.toList());
                        Response<StockDTO> errorResponse = new Response<>(LocalDateTime.now(), "getStockByCardId", false, null, errors);
                        return Mono.just(errorResponse);
                    }
                    return stockValidation.validate(wrapper.getData(), "getStockByCardId");
                })
                .onErrorResume(WebClientResponseException.class, exception -> {
                    log.error("Stock service error: status={}", exception.getStatusCode());
                    WebCustomClientException error = new WebCustomClientException("Error stock-service in method getStockByCardId" + exception.getStatusCode(),
                            HttpStatus.valueOf(exception.getStatusCode().value()));
                    Response<StockDTO> errorResponse = new Response<>(LocalDateTime.now(), "getStockByCardId", false, null, List.of(error));
                    return Mono.just(errorResponse);
                });
    }

    public Mono<Response<StockDTO>> createStockById(StockRequest request, String user) {
        return webClient
                .post()
                .uri("/api/stock")
                .header("User-Auth", user)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockServiceResponse.class)
                .timeout(Duration.ofSeconds(3))
                .flatMap(wrapper -> {
                    if (!wrapper.isSuccess()) {
                        log.error("stock-service returned error: {}", wrapper.getListErrors());
                        List<ValidationException> errors = wrapper.getListErrors().stream()
                                .map(e -> (ValidationException) new WebCustomClientException(e.getMessage(), HttpStatus.BAD_REQUEST))
                                .collect(Collectors.toList());
                        Response<StockDTO> errorResponse = new Response<>(LocalDateTime.now(), "getStockByCardId", false, null, errors);
                        return Mono.just(errorResponse);
                    }
                    return stockValidation.validate(wrapper.getStock(), "createStockById");
                })
                .onErrorResume(WebClientResponseException.class, exception -> {
                    log.error("Stock service error status={}", exception.getStatusCode());
                    WebCustomClientException error = new WebCustomClientException("Error stock-service in method createStockById" + exception.getStatusCode(),
                            HttpStatus.valueOf(exception.getStatusCode().value()));
                    return Mono.just(new Response<>(LocalDateTime.now(), "createStockById", false, null, List.of(error)));
                });
    }
}