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
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    @Column(name = "product_detail_price")
    private Double productDetailPrice;

    @Column(name = "product_detail_price_net")
    private Double productDetailPriceNet;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_amount_net")
    private Double totalAmountNet;

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
