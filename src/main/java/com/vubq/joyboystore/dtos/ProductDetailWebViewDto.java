package com.vubq.joyboystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailWebViewDto {

    private String id;
    private Double price;
    private Double priceNet;
    private Integer quantity;
    private SizeDto size;
    private ColorDto color;
    private MaterialDto material;
    private List<String> listImage;
    private ProductWebViewDto product;
}
