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
public class ProductWebViewDto {

    private String id;
    private String name;
    private String description;
    private Double price;
    private Double priceNet;
    private CategoryDto category;
    private BrandDto brand;
    private Double rate;
    private List<String> listImage;
    private List<ProductDetailWebViewDto> listProductDetail;
}
