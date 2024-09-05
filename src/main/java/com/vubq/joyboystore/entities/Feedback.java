package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "content")
    private String content;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;
}
