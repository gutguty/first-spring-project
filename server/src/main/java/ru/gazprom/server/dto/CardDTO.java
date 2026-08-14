package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDTO {
    private Long id;
    private String title;
    private Integer price;
    private String image;
}
