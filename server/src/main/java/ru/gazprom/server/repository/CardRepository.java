package ru.gazprom.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gazprom.server.model.Card;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByCategoryId(Long id);

    List<Card> findByCategoryName(String categoryName);

    @Query("SELECT c FROM Card c WHERE c.price BETWEEN :min AND :max")
    List<Card> findByPriceBetween(@Param("min") Integer min, @Param("max") Integer max);

}
