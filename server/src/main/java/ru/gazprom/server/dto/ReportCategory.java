package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportCategory {
    private Long categoryId;
    private String categoryName;
    private List<ReportRow> rows;
    private Integer quantity;
}
