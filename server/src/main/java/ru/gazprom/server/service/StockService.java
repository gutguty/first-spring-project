package ru.gazprom.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.dto.StockErrorDTO;
import ru.gazprom.server.dto.StockRequest;
import ru.gazprom.server.dto.StockServiceResponse;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;
import ru.gazprom.server.exception.WebCustomClientError;
import ru.gazprom.server.validator.StockValidation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

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
                        List<ValidationError> errors = wrapper.getListErrors().stream()
                                .map(e -> (ValidationError) new WebCustomClientError(e.getMessage(), HttpStatus.BAD_REQUEST))
                                .collect(Collectors.toList());
                        Response<StockDTO> errorResponse = responseError("createStockById", errors);
                        return Mono.just(errorResponse);
                    }
                    return stockValidation.validate(wrapper.getStock(), "createStockById");
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