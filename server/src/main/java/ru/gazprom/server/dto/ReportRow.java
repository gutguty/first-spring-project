package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportRow {
    private Long cardId;
    private String cardName;
    private Integer quantity;
}
