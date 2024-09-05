package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
@Entity
@Table(name="product_details")
public class ProductDetail {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_net")
    private Double priceNet;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @ManyToOne(optional = false)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @ManyToOne(optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;
}
