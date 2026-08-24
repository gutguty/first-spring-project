package ru.gazprom.server.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.dto.StockRequest;
import ru.gazprom.server.exception.WebCustomClientException;
import ru.gazprom.server.validator.StockValidation;

import java.time.Duration;

@Service
public class StockService {
    private static final Logger log = LogManager.getLogger(StockService.class);
    private final WebClient webClient;
    private final StockValidation stockValidation;

    public StockService(WebClient webClient, StockValidation stockValidation) {
        this.webClient = webClient;
        this.stockValidation = stockValidation;
    }

    public Mono<StockDTO> getStockByCardId(Long cardId, String user) {
        return webClient
                .get()
                .uri("/api/stock/card/{cardId}", cardId)
                .header("User-Auth", user)
                .retrieve()
                .bodyToMono(StockDTO.class)
                .flatMap(stockValidation::validate)
                .timeout(Duration.ofSeconds(3))

                .onErrorMap(WebClientResponseException.class, exception -> {
                   log.error("Stock service return = {}", exception.getStatusCode());
                   String message = "Error stock-service = " + exception.getStatusCode();
                   return new WebCustomClientException(message, exception.getStatusCode());
                });
    }

    public Mono<StockDTO> createStockById(StockRequest request, String user) {
        return webClient
                .post()
                .uri("/api/stock")
                .header("User-Auth", user)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockDTO.class)
                .flatMap(stockValidation::validate)
                .timeout(Duration.ofSeconds(3))

                .onErrorMap(WebClientResponseException.class, exception -> {
                    log.error("Stock service return = {}", exception.getStatusCode());
                    String message = "Error stock-service = " + exception.getStatusCode();
                    return new WebCustomClientException(message, exception.getStatusCode());
                });

    }

}
