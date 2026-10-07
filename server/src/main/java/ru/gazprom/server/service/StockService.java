package ru.gazprom.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.dto.StockRequest;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;
import ru.gazprom.server.exception.WebCustomClientError;
import ru.gazprom.server.validator.StockValidation;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static ru.gazprom.server.utils.ResponseUtils.responseError;

@Service
@Slf4j
public class StockService {
    private final WebClient webClient;
    private final StockValidation stockValidation;

    private static final int BATCH_SIZE = 3;
    private static final int MAX_CONCURRENT_BATCHES = 4;

    public StockService(WebClient webClient, StockValidation stockValidation) {
        this.webClient = webClient;
        this.stockValidation = stockValidation;
    }

    ParameterizedTypeReference<Response<StockDTO>> typeRef = new ParameterizedTypeReference<>() {

    };

    ParameterizedTypeReference<Response<List<StockDTO>>> listTypeRef = new ParameterizedTypeReference<>() {

    };

    private Mono<List<StockDTO>> getStocksBatch(List<Long> batch) {
        return webClient
                .get()
                .uri(uri -> uri
                        .path("/api/stock/cards")
                        .queryParam("ids", batch)
                        .build()
                )
                .retrieve()
                .bodyToMono(listTypeRef)
                .timeout(Duration.ofSeconds(3))
                .map(wrapper -> {
                    if (!wrapper.isSuccess() || wrapper.getData() == null) {
                        log.error("stock-service returned error for batch {}", batch);
                        return List.<StockDTO>of();
                    }
                    return wrapper.getData();
                })
                .onErrorResume(e -> {
                    log.error("Batch {} failed with error {}", batch, e.getMessage());
                    return Mono.just(List.of());
                });
    }

    public Mono<Response<StockDTO>> getStockByCardId(Long cardId, String user) {

        return webClient
                .get()
                .uri("/api/stock/card/{cardId}" , cardId)
                .header("User-Auth", user)
                .retrieve()
                .bodyToMono(typeRef)
                .timeout(Duration.ofSeconds(3))
                .flatMap(wrapper -> {
                    if (!wrapper.isSuccess()) {
                        List<ValidationError> listErrors = wrapper.getListErrors();
                        log.error("stock-service returned error: {}", listErrors);

                        List<ValidationError> errors = listErrors.stream()
                                .map(e -> new WebCustomClientError(e.getMessage(), HttpStatus.BAD_REQUEST))
                                .collect(Collectors.toList());

                        Response<StockDTO> errorResponse = responseError("getStockByCardId", errors);
                        return Mono.just(errorResponse);
                    }
                    return stockValidation.validate(wrapper.getData(), "getStockByCardId");
                })
                .onErrorResume(WebClientResponseException.class, exception -> {
                    log.error("Stock service error: status={}", exception.getStatusCode());
                    WebCustomClientError error = new WebCustomClientError("Error stock-service in method getStockByCardId" + exception.getStatusCode(),
                            HttpStatus.valueOf(exception.getStatusCode().value()));
                    Response<StockDTO> errorResponse = responseError("getStockByCardId", error);
                    return Mono.just(errorResponse);
                });
    }

    public Mono<List<StockDTO>> getStocksByCardIds(List<Long> cardIds) {

        return Flux.fromIterable(cardIds)
                .buffer(BATCH_SIZE)
                .flatMapSequential(this::getStocksBatch, MAX_CONCURRENT_BATCHES)
                .flatMapIterable(list -> list)
                .collectList();
    }

    public Mono<Response<StockDTO>> createStockById(StockRequest request, String user) {
        return webClient
                .post()
                .uri("/api/stock")
                .header("User-Auth", user)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeRef)
                .timeout(Duration.ofSeconds(3))
                .flatMap(wrapper -> {
                    if (!wrapper.isSuccess()) {
                        log.error("stock-service returned error: {}", wrapper.getListErrors());
                        List<ValidationError> errors = wrapper.getListErrors().stream()
                                .map(e -> (ValidationError) new WebCustomClientError(e.getMessage(), HttpStatus.BAD_REQUEST))
                                .collect(Collectors.toList());
                        Response<StockDTO> errorResponse = responseError("createStockById", errors);
                        return Mono.just(errorResponse);
                    }
                    return stockValidation.validate(wrapper.getData(), "createStockById");
                })
                .onErrorResume(WebClientResponseException.class, exception -> {
                    log.error("Stock service error status={}", exception.getStatusCode());
                    WebCustomClientError error = new WebCustomClientError("Error stock-service in method createStockById" + exception.getStatusCode(),
                            HttpStatus.valueOf(exception.getStatusCode().value()));
                    Response<StockDTO> errorResponse = responseError("createStockById", error);
                    return Mono.just(errorResponse);
                });
    }
}