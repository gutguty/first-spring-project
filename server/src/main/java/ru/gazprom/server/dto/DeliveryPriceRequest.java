package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gazprom.server.enums.DeliveryType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPriceRequest {
    private Long cardId;
    private Long addressId;
    private DeliveryType deliveryType;
}