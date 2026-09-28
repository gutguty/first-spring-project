package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.enums.PaymentType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
   private Long cardId;
   private Long addressId;
   private DeliveryType deliveryType;
   private PaymentType paymentType;
}
