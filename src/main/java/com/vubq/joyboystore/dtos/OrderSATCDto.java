package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSATCDto {

    private String fullName;
    private String phoneNumber;
    private String address;
    private String city;
    private String district;
    private String ward;
    private String voucherId;
    private Double totalAmount;
    private Double totalAmountNet;
    private String noteByAdmin;
    private String noteByCustomer;
    private Double moneyPaid;
    private Double moneyRefunds;
    private Boolean isVoucher;
    private EPaymentType paymentType;
    List<OrderDetailSATCDto> listOrderDetail;
}
