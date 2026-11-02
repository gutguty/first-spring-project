package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CardRow {
    private Long id;
    private Long categoryId;
    private String title;
    private BigDecimal price;
    private String image;
}