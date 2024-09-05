package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCorUDto {

    private String id;
    private String name;
    private String description;
    private Double price;
    private Double priceNet;
    private String categoryId;
    private String brandId;
    private List<ImageDto> listImage;
    private List<ImageDto> listImageDelete;
    private List<ProductDetailCorUDto> listProductDetail;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;

    public void addImageToList(ImageDto image) {
        if (this.listImage == null) {
            this.listImage = new ArrayList<>();
        }
        this.listImage.add(image);
    }

    public void addImageDeleteToList(ImageDto image) {
        if (this.listImageDelete == null) {
            this.listImageDelete = new ArrayList<>();
        }
        this.listImageDelete.add(image);
    }

    public void addProductDetailToList(ProductDetailCorUDto productDetail) {
        if (this.listProductDetail == null) {
            this.listProductDetail = new ArrayList<>();
        }
        this.listProductDetail.add(productDetail);
    }
}
