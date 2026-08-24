package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

//12
public class CardDTO {
    //16
    private Long id;
    private String title;
    private Integer price;
    private String image;
}

//28 + 4 = 32 - shallow size

//new CardDTO(1L, "Boots", 500, "boots.png")

//Long
//12 + 8 + 4 = 24

//string
// 12 + 4 + 4 + 1 + 3 = 24
//24 + 16 + 5 + 3 = 48

//integer
// 12 + 4 = 16

//string
//24 + 16 + 9 + 7 = 56

// 176 - retained size
