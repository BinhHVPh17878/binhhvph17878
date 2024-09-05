package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWebShopDTO {

    private String id;
    private String name;
    private String description;
    private Double price;
    private Double priceNet;
    private Category category;
    private Brand brand;
    private Date createdAt;
    private Double rate;
    private Integer quantitySold;
    private EStatus status;
    private Integer quantityOfProductAvailable;
    private List<String> listImage;
    private List<SizeDto> sizes;
    private List<ColorDto> colors;
    private List<MaterialDto> materials;
    private Integer totalProductsAvailable;
}
