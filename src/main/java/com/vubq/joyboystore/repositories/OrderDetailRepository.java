package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    @Query(value = "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findAllByOrderId(String orderId);

    @Query(value = "SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.productDetail.product.id = :productId")
    Integer getQuantitySold(String productId);
}
