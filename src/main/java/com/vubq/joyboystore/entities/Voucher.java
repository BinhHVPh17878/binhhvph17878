package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.enums.EVoucherType;
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
@Table(name="vouchers")
public class Voucher {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EVoucherType type;

    @Column(name = "value")
    private Double value;

    @Column(name = "max")
    private Double max;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

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
