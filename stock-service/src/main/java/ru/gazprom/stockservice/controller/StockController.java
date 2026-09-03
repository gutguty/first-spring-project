package ru.gazprom.stockservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.stockservice.exception.ForbiddenException;
import ru.gazprom.stockservice.exception.Response;
import ru.gazprom.stockservice.users.AllowedUsers;
import ru.gazprom.stockservice.model.Stock;
import ru.gazprom.stockservice.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stock")
    public Response<List<Stock>> getAllStock() {
        return stockService.getAllStock();
    }

    @GetMapping("/stock/{id}")
    public Response<Stock> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }


    @PostMapping("/stock")
    public Response<Stock> createStockById(@RequestBody Stock stock, @RequestHeader ("User-Auth") String user) {
        return stockService.createStockById(stock, user);
    }

    @DeleteMapping("/stock/{id}")
    public Response<Void> deleteStockById(@PathVariable Long id) {
        return stockService.deleteStockById(id);
    }

    @PutMapping("/stock/{id}")
    public Response<Stock> updateStockById(@PathVariable Long id,@RequestBody Stock stock) {
       return stockService.updateStockById(id, stock);
    }

    @GetMapping("/stock/card/{cardId}")
    public Response<Stock> getStockByCardId(@PathVariable Long cardId, @RequestHeader ("User-Auth") String user) {
        return stockService.getStockByCardId(cardId, user);
    }

    @GetMapping("/stock/limit")
    public Response<List<Stock>> getLimitStock(@RequestParam Integer limit) {
        return stockService.getStockLessLimit(limit);
    }
}
