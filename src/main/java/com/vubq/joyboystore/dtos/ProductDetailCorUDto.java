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
public class ProductDetailCorUDto {

    private String id;
    private Double price;
    private Double priceNet;
    private Integer quantity;
    private String productId;
    private String sizeId;
    private String colorId;
    private String materialId;
    private List<ImageDto> listImage;
    private List<ImageDto> listImageDelete;
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
}
