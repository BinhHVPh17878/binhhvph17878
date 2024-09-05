package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.OrderDetail;
import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.enums.EOrderStatus;
import com.vubq.joyboystore.enums.EOrderType;
import com.vubq.joyboystore.enums.EPaymentType;
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
public class OrderByUserDTO {

    private String id;
    private EOrderType type;
    private String fullNameCustomer;
    private String phoneNumber;
    private String address;
    private String email;
    private User createdBy;
    private Date createdAt;
    private Date completedAt;
    private Double totalAmount;
    private String voucherId;
    private String noteByCustomer;
    private String city;
    private String district;
    private String ward;
    private String noteByAdmin;
    private EPaymentType paymentType;
    private EOrderStatus status;
    private List<OrderDetail> orderDetails;
}
