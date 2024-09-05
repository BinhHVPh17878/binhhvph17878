package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.VoucherDto;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.services.VoucherService;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/voucher")
public class VoucherController extends BaseController {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private VoucherService voucherService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Voucher> result = this.voucherService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Voucher voucher = this.voucherService.getById(id);
        VoucherDto dto = VoucherDto.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .type(voucher.getType())
                .value(voucher.getValue())
                .max(voucher.getMax())
                .quantity(voucher.getQuantity())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .status(voucher.getStatus())
                .createdAt(voucher.getCreatedAt())
                .updatedAt(voucher.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(voucher.getCreatedBy()) ? null : this.getUserNameByUserId(voucher.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(voucher.getUpdatedBy()) ? null : this.getUserNameByUserId(voucher.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @GetMapping("get-all-Like-code-and-still-active")
    public Response getAllLikeCodeAndStillActive(@RequestParam(value = "code") String code) {
        List<Voucher> listVoucher = this.voucherService.getAllLikeCodeAndStillActive(code, new Date(), EStatus.ACTIVE);
        List<VoucherDto> listVoucherDto = new ArrayList<>();
        listVoucher.forEach(v -> {
            VoucherDto voucherDto = VoucherDto.builder()
                    .id(v.getId())
                    .code(v.getCode())
                    .type(v.getType())
                    .value(v.getValue())
                    .max(v.getMax())
                    .quantity(v.getQuantity())
                    .startDate(v.getStartDate())
                    .endDate(v.getEndDate())
                    .status(v.getStatus())
                    .createdAt(v.getCreatedAt())
                    .updatedAt(v.getUpdatedAt())
                    .createdBy(StringUtils.isEmpty(v.getCreatedBy()) ? null : this.getUserNameByUserId(v.getCreatedBy()))
                    .updatedBy(StringUtils.isEmpty(v.getUpdatedBy()) ? null : this.getUserNameByUserId(v.getUpdatedBy()))
                    .build();
            listVoucherDto.add(voucherDto);
        });
        return Response.build().ok().data(listVoucherDto);
    }

    @GetMapping("get-by-code/{code}")
    public Response getByCode(@PathVariable String code) {
        return Response.build().ok().data(this.voucherService.getByCode(code));
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody VoucherDto dto) {
        Voucher voucher = null;
        if (StringUtils.isEmpty(dto.getId())) {
            String[] prefixes = {"SALE", "PROMO", "DISCOUNT"};
            voucher = Voucher.builder()
                    .code(generateDiscountCode(prefixes[RANDOM.nextInt(prefixes.length)]))
                    .type(dto.getType())
                    .value(dto.getValue())
                    .max(dto.getMax())
                    .quantity(dto.getQuantity())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            voucher = this.voucherService.getById(dto.getId());
            if (voucher == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy mã giảm giá!");
            }
            voucher.setType(dto.getType());
            voucher.setValue(dto.getValue());
            voucher.setMax(dto.getMax());
            voucher.setQuantity(dto.getQuantity());
            voucher.setStartDate(dto.getStartDate());
            voucher.setEndDate(dto.getEndDate());
            voucher.setStatus(dto.getStatus());
            voucher.setUpdatedAt(new Date());
            voucher.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.voucherService.save(voucher);
        return Response.build().ok();
    }

    public static String generateDiscountCode(String prefix) {
        int randomLength = 12;
        int prefixLength = prefix.length();
        int suffixLength = randomLength - prefixLength;

        StringBuilder discountCode = new StringBuilder(prefix);
        for (int i = 0; i < suffixLength; i++) {
            discountCode.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return discountCode.toString();
    }
}
