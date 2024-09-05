package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.OrderDetail;
import com.vubq.joyboystore.repositories.OrderDetailRepository;
import com.vubq.joyboystore.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetails) {
        return this.orderDetailRepository.saveAll(orderDetails);
    }

    @Override
    public List<OrderDetail> getListOfOrderDetailsByOrderId(String orderId) {
        return this.orderDetailRepository.findAllByOrderId(orderId);
    }

    @Override
    public Integer getQuantitySold(String productId) {
        Integer quantitySold = this.orderDetailRepository.getQuantitySold(productId);
        return quantitySold == null ? 0 : quantitySold;
    }

    @Override
    public List<OrderDetail> findAllByOrderId(String orderId) {
        return this.orderDetailRepository.findAllByOrderId(orderId);
    }
}
