package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetail> saveAll(List<OrderDetail> orderDetails);

    List<OrderDetail> getListOfOrderDetailsByOrderId(String orderId);

    Integer getQuantitySold(String productId);

    List<OrderDetail> findAllByOrderId(String orderId);
}
