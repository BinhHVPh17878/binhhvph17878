package com.vubq.joyboystore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VNPayDto {
    private String id;
    private int money;
    private String bankCode;
    private String billMobile;
    private String billEmail;
    private String fullname;
    // Billing
    private String billAddress;
    private String billCity;
    private String billCountry;
    private String billState;
    // Invoice
    private String invPhone;
    private String invEmail;
    private String invCustomer;
    private String invAddress;
    private String invCompany;
    private String invTaxcode;
    private String invType;
}
