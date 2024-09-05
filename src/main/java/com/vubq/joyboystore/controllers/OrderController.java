package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.*;
import com.vubq.joyboystore.entities.*;
import com.vubq.joyboystore.enums.*;
import com.vubq.joyboystore.services.*;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private HistoryOrderService historyOrderService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @PostMapping("pay-sales-at-the-counter")
    @Transactional
    public Response paySalesAtTheCounter(@RequestBody OrderSATCDto dto) {
        Order orderNew = Order.builder()
                .fullName(dto.getFullName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .totalAmount(dto.getTotalAmount())
                .totalAmountNet(dto.getTotalAmountNet())
                .moneyPaid(dto.getMoneyPaid())
                .moneyRefunds(dto.getMoneyRefunds())
                .noteByAdmin(dto.getNoteByAdmin())
                .noteByCustomer(dto.getNoteByCustomer())
                .createdAt(new Date())
                .createdBy(this.getTheCurrentlyLoggedInUserId())
                .status(EOrderStatus.SUCCESS)
                .type(EOrderType.AT_THE_SHOP)
                .paymentType(EPaymentType.PAYMENT_IN_SHOP)
                .paymentType2(EPaymentType2.CASH)
                .build();

        Voucher voucher = null;
        if (dto.getIsVoucher()) {
            voucher = this.voucherService.getById(dto.getVoucherId());
            if (voucher == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá không tồn tại.");
            } else {
                if (voucher.getQuantity() <= 0) {
                    return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá đã hết.");
                }
                orderNew.setVoucherId(voucher.getId());
                voucher.setQuantity(voucher.getQuantity() - 1);
            }
        }

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        List<ProductDetail> listProductDetail = new ArrayList<>();
        for (int i = 0; i < dto.getListOrderDetail().size(); i++) {
            ProductDetail productDetail = this.productDetailService.getById(dto.getListOrderDetail().get(i).getProductDetailId());
            if (dto.getListOrderDetail().get(i).getQuantity() > productDetail.getQuantity()) {
                return Response.build()
                        .code(Response.CODE_INTERNAL_ERROR)
                        .data("Số lượng sản phẩm [ " + productDetail.getProduct().getName() + " - " + productDetail.getSize().getName() + " - " + productDetail.getColor().getName() + " - " + productDetail.getMaterial().getName() + " ] không đủ.");
            }
            productDetail.setQuantity(productDetail.getQuantity() - dto.getListOrderDetail().get(i).getQuantity());
            listProductDetail.add(productDetail);
            OrderDetail orderDetail = OrderDetail.builder()
                    .productDetail(productDetail)
                    .quantity(dto.getListOrderDetail().get(i).getQuantity())
                    .totalAmount(dto.getListOrderDetail().get(i).getTotalAmount())
                    .totalAmountNet(dto.getListOrderDetail().get(i).getTotalAmountNet())
                    .productDetailPrice(dto.getListOrderDetail().get(i).getProductDetailPrice())
                    .productDetailPriceNet(dto.getListOrderDetail().get(i).getProductDetailPriceNet())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .status(EStatus.ACTIVE)
                    .build();
            listOrderDetail.add(orderDetail);

        }

        Order order = this.orderService.save(orderNew);
        if (voucher != null) {
            this.voucherService.save(voucher);
        }
        listOrderDetail.forEach(od -> od.setOrder(order));
        this.orderDetailService.saveAll(listOrderDetail);
        this.productDetailService.saveAll(listProductDetail);
        return Response.build().ok();
    }

    @PostMapping("order-online")
    @Transactional
    public Response orderOnline(@RequestBody OrderSATCDto dto) {
        Order orderNew = Order.builder()
                .fullName(dto.getFullName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .ward(dto.getWard())
                .phoneNumber(dto.getPhoneNumber())
                .totalAmount(dto.getTotalAmount())
                .totalAmountNet(dto.getTotalAmountNet())
                .noteByAdmin(dto.getNoteByAdmin())
                .noteByCustomer(dto.getNoteByCustomer())
                .createdAt(new Date())
                .createdBy(this.getTheCurrentlyLoggedInUserId())
                .status(EOrderStatus.WAIT_FOR_CONFIRMATION)
                .type(EOrderType.ONLINE)
                .paymentType(dto.getPaymentType())
                .paymentType2(EPaymentType2.CASH)
                .build();

        Voucher voucher = null;
        if (dto.getIsVoucher()) {
            voucher = this.voucherService.getById(dto.getVoucherId());
            if (voucher == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá không tồn tại.");
            } else {
                if (voucher.getQuantity() <= 0) {
                    return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Mã giảm giá đã hết.");
                }
                orderNew.setVoucherId(voucher.getId());
                voucher.setQuantity(voucher.getQuantity() - 1);
            }
        }

        List<OrderDetail> listOrderDetail = new ArrayList<>();
        for (int i = 0; i < dto.getListOrderDetail().size(); i++) {
            ProductDetail productDetail = this.productDetailService.getById(dto.getListOrderDetail().get(i).getProductDetailId());
            OrderDetail orderDetail = OrderDetail.builder()
                    .productDetail(productDetail)
                    .quantity(dto.getListOrderDetail().get(i).getQuantity())
                    .totalAmount(dto.getListOrderDetail().get(i).getTotalAmount())
                    .totalAmountNet(dto.getListOrderDetail().get(i).getTotalAmountNet())
                    .productDetailPrice(dto.getListOrderDetail().get(i).getProductDetailPrice())
                    .productDetailPriceNet(dto.getListOrderDetail().get(i).getProductDetailPriceNet())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .status(EStatus.ACTIVE)
                    .build();
            listOrderDetail.add(orderDetail);
        }

        Order order = this.orderService.save(orderNew);
        this.historyOrderService.save(HistoryOder.builder()
                .orderId(order.getId())
                .createdAt(new Date())
                .status(EOrderStatus.WAIT_FOR_CONFIRMATION)
                .build());
        if (voucher != null) {
            this.voucherService.save(voucher);
        }
        listOrderDetail.forEach(od -> od.setOrder(order));
        this.orderDetailService.saveAll(listOrderDetail);
        return Response.build().ok();
    }

    @GetMapping("get-all-page-online")
    public DataTableResponse getAllPage(
            DataTableRequest request,
            @RequestParam String status,
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) {
        Page<Order> result = this.orderService.getAllPage(request, status, dateFrom, dateTo, EOrderType.ONLINE);

        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.get().toList());
    }

    @GetMapping("get-all-page-at-the-counter")
    public DataTableResponse getAllPageAtTheCounter(
            DataTableRequest request,
            @RequestParam String status,
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) {
        Page<Order> result = this.orderService.getAllPage(request, status, dateFrom, dateTo, EOrderType.AT_THE_SHOP);

        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.get().toList());
    }

    @GetMapping("get-list-order-by-user")
    public Response getListOrderByUser() {
        List<Order> orderList = this.orderService.getAllOrderByUserId(this.getTheCurrentlyLoggedInUserId());
        List<OrderDTDto> orderDTDtos = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderDetail> orderDetails = this.orderDetailService.findAllByOrderId(order.getId());
            List<HistoryOder> historyOders = this.historyOrderService.findAllByOrderId(order.getId());
            Voucher voucher = null;
            if(!StringUtils.isEmpty(order.getVoucherId())) {
                voucher = this.voucherService.getById(order.getVoucherId());
            }
            OrderDTDto orderDTDto = new OrderDTDto();
            orderDTDto.setOrder(order);
            orderDTDto.setListOrderDetail(orderDetails);
            orderDTDto.setListHistoryOrder(historyOders);
            orderDTDto.setVoucher(voucher);
            orderDTDtos.add(orderDTDto);
            List<ListImageDto> listImageDtos = new ArrayList<>();
            for (OrderDetail od: orderDetails) {
                listImageDtos.add(
                        ListImageDto.builder()
                                .id(od.getId())
                                .listImage(this.imageService.getAllUrlBySecondaryId(od.getProductDetail().getProduct().getId()))
                                .build()
                );
            }
            orderDTDto.setListImage(listImageDtos);
        }
        return Response.build().ok().data(orderDTDtos);
    }

    @GetMapping("get-detail-by-id/{id}")
    public Response getDetailById(@PathVariable String id) {
        Order order = this.orderService.findById(id).orElse(null);
        List<OrderDetail> orderDetails = this.orderDetailService.findAllByOrderId(order.getId());
        List<HistoryOder> historyOders = this.historyOrderService.findAllByOrderId(order.getId());
        Voucher voucher = null;
        if(!StringUtils.isEmpty(order.getVoucherId())) {
            voucher = this.voucherService.getById(order.getVoucherId());
        }

        OrderDTDto orderDTDto = new OrderDTDto();
        orderDTDto.setOrder(order);
        orderDTDto.setListOrderDetail(orderDetails);
        orderDTDto.setListHistoryOrder(historyOders);
        orderDTDto.setVoucher(voucher);

        return Response.build().ok().data(orderDTDto);
    }

    @PostMapping("change-status")
    public Response changeStatus(@RequestBody ChangeStatusDto changeStatusDto) {
        Order order = this.orderService.findById(changeStatusDto.getId()).orElse(null);
        order.setStatus(changeStatusDto.getStatus());
        if(changeStatusDto.getStatus() == EOrderStatus.CANCELLED) {
            User user = this.userService.getById(this.getTheCurrentlyLoggedInUserId());
            List<Role> roleList = user.getRoles().stream().toList();
            if (roleList.get(0).getName() == ERole.ROLE_CUSTOMER) {
                order.setCancelBy("CUSTOMER");
            } else {
                order.setCancelBy("ADMIN");
            }
            order.setCancelById(this.getTheCurrentlyLoggedInUserId());
        }

        this.orderService.save(order);

        this.historyOrderService.save(HistoryOder.builder()
                .orderId(order.getId())
                .createdAt(new Date())
                .status(changeStatusDto.getStatus())
                .build());
        return Response.build().ok();
    }

    @PostMapping("get-history-customer")
    public Response getHistoryCustomer(@RequestBody HistoryCustomerDto historyCustomerDto) {
        return Response.build().ok().data(this.orderService.getHistoryCustomer(historyCustomerDto.getFullName()));
    }

    @PostMapping("get-history-customer-phone")
    public Response getHistoryCustomerPhone(@RequestBody HistoryCustomerDto historyCustomerDto) {
        return Response.build().ok().data(this.orderService.getHistoryCustomerPN(historyCustomerDto.getPhoneNumber()));
    }
}
