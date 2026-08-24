package ru.gazprom.stockservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)   
    private Long cardId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reserved;
}
