package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductViewDto {

    private String id;
    private String name;
    private String description;
    private String categoryName;
    private String brandName;
    private String listSizeString;
    private String listColorString;
    private String listMaterialString;
    private Double minPrice;
    private Double maxPrice;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;
}
