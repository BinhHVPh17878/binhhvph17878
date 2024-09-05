package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.dtos.HistoryCustomerDto;
import com.vubq.joyboystore.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    @Query(value = "SELECT o FROM Order o WHERE o.createdBy = :userId AND o.status = com.vubq.joyboystore.enums.EOrderStatus.WAIT_FOR_CONFIRMATION")
    List<Order> getAllOrderWaitForConfirmationByUserId(String userId);

    @Query(value = "SELECT o FROM Order o WHERE o.createdBy = :userId AND o.status = com.vubq.joyboystore.enums.EOrderStatus.SUCCESS")
    List<Order> getAllOrderSuccessByUserId(String userId);

    @Query(value = "SELECT o FROM Order o WHERE o.createdBy = :userId AND o.status = com.vubq.joyboystore.enums.EOrderStatus.CANCELLED")
    List<Order> getAllOrderCancelledByUserId(String userId);

    @Query(value = "SELECT o FROM Order o WHERE o.createdBy = :userId ORDER BY o.createdAt DESC")
    List<Order> getAllOrderByUserId(String userId);

    @Query(value = "SELECT DISTINCT new com.vubq.joyboystore.dtos.HistoryCustomerDto(o.fullName, o.phoneNumber, o.address) FROM Order o WHERE o.fullName like %:fullNameCustomer% AND o.type = 'AT_THE_SHOP'")
    List<HistoryCustomerDto> getHistoryCustomer(String fullNameCustomer);

    @Query(value = "SELECT DISTINCT new com.vubq.joyboystore.dtos.HistoryCustomerDto(o.fullName, o.phoneNumber, o.address) FROM Order o WHERE o.phoneNumber like %:phone% AND o.type = 'AT_THE_SHOP'")
    List<HistoryCustomerDto> getHistoryCustomerPN(String phone);

    List<Order> findAllByCreatedBy(String createdBy);
}
