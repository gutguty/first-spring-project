package ru.gazprom.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazprom.stockservice.model.Stock;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean existsStockByCardId(Long cardId);

    Optional<Stock> findByCardId(Long cardId);

    List<Stock> findAllByCardIdIn(List<Long> cardIds); }
