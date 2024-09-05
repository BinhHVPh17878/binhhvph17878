package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.EOrderStatus;
import com.vubq.joyboystore.enums.EOrderType;
import com.vubq.joyboystore.enums.EPaymentType;
import com.vubq.joyboystore.enums.EPaymentType2;
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
@Table(name = "orders")
public class Order {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_amount_net")
    private Double totalAmountNet;

    @Column(name = "money_paid")
    private Double moneyPaid;

    @Column(name = "money_refunds")
    private Double moneyRefunds;

    @Column(name = "voucher_id")
    private String voucherId;

    @Column(name = "note_by_customer")
    private String noteByCustomer;

    @Column(name = "note_by_admin")
    private String noteByAdmin;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "completed_at")
    private Date completedAt;

    @Column(name = "cancel_by")
    private String cancelBy;

    @Column(name = "cancel_by_id")
    private String cancelById;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EOrderType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private EPaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type_2")
    private EPaymentType2 paymentType2;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;
}
