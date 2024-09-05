package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSATCDto {

    private String id;
    private String name;
    private String description;
    private Double price;
    private Double priceNet;
    private CategoryDto category;
    private BrandDto brand;
    private List<String> listImage;
}
