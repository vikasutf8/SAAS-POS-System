package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.PaymentType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSummery {

    private PaymentType type;
    private Double totalAmount;

    private Integer transactionCount;
    private Double percentage;

}
