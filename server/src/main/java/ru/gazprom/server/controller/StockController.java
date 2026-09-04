package ru.gazprom.server.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.gazprom.server.dto.StockDTO;
import ru.gazprom.server.dto.StockRequest;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.StockService;

@RestController
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/api/cards/{id}/stock")
    public Mono<Response<StockDTO>> getCardByCardId(@PathVariable Long id, @RequestHeader("User-Auth") String user) {
        return stockService.getStockByCardId(id, user);
    }

    @PostMapping("/api/cards/{id}/stock")
    public Mono<Response<StockDTO>> createCardById(@PathVariable Long id, @RequestHeader("User-Auth") String user, @RequestBody StockRequest request) {
        request.setCardId(id);
        return stockService.createStockById(request, user);
    }
}
