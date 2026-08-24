package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {
    private Long id;
    private Long cardId;
    private Integer quantity;
    private Integer reserved;
}
