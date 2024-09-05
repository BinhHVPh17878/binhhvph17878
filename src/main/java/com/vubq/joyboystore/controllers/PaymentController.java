package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.VNPayDto;
import com.vubq.joyboystore.utils.Response;
import com.vubq.joyboystore.utils.VNPay;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/webapi/payment")
public class PaymentController {

    @Transactional
    @PostMapping
    public Response vnPayment(@RequestBody VNPayDto vnpay) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Hoa don thanh toan Online";
        String orderType = "170000";
        String vnp_TxnRef = VNPay.getRandomNumber(8);
        String vnp_IpAddr = "0:0:0:0:0:0:0:1";
        String vnp_TmnCode = "6JSY03EA";

        int amount = vnpay.getMoney() * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = vnpay.getBankCode();
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNPay.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        // Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        // Billing
        vnp_Params.put("vnp_Bill_Mobile", vnpay.getBillMobile());
        vnp_Params.put("vnp_Bill_Email", vnpay.getBillEmail());
        vnp_Params.put("vnp_Bill_FirstName", vnpay.getFullname());
        vnp_Params.put("vnp_Bill_LastName", vnpay.getFullname());
        vnp_Params.put("vnp_Bill_Address", vnpay.getBillAddress());
        vnp_Params.put("vnp_Bill_City", vnpay.getBillCity());
        vnp_Params.put("vnp_Bill_Country", vnpay.getBillCountry());
        if (vnpay.getBillState() != null && !vnpay.getBillState().isEmpty()) {
            vnp_Params.put("vnp_Bill_State", vnpay.getBillState());
        }
        // Invoice
        vnp_Params.put("vnp_Inv_Phone", vnpay.getInvPhone());
        vnp_Params.put("vnp_Inv_Email", vnpay.getInvEmail());
        vnp_Params.put("vnp_Inv_Customer", vnpay.getInvCustomer());
        vnp_Params.put("vnp_Inv_Address", vnpay.getInvAddress());
        vnp_Params.put("vnp_Inv_Company", vnpay.getInvCompany());
        vnp_Params.put("vnp_Inv_Taxcode", vnpay.getInvTaxcode());
        vnp_Params.put("vnp_Inv_Type", vnpay.getInvType());
        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPay.hmacSHA512(VNPay.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPay.vnp_PayUrl + "?" + queryUrl;
        return Response.build().ok().data(paymentUrl);
    }
}
