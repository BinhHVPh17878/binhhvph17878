package com.vubq.joyboystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailSATCDto {

    private String productDetailId;
    private Double productDetailPrice;
    private Double productDetailPriceNet;
    private Double totalAmount;
    private Double totalAmountNet;
    private Integer quantity;
}
