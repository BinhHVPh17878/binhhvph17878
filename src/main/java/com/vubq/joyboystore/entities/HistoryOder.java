package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.EOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Builder
@Table(name = "history_oders")
public class HistoryOder {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "created_at")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;
}
