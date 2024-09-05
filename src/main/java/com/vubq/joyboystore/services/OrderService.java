package com.vubq.joyboystore.services;

import com.vubq.joyboystore.dtos.HistoryCustomerDto;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EOrderStatus;
import com.vubq.joyboystore.enums.EOrderType;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order save(Order order);

    Page<Order> getListOfOrdersByCriteriaSellAtTheShop(DataTableRequest request, String dateFrom, String dateTo);

//    Page<Order> getListOfOrdersByCriteria(DataTableRequest request, OrderSearchForm orderSearchForm);

    Optional<Order> findById(String id);

    List<Order> getAllOrderWaitForConfirmationByUserId(String userId);

    List<Order> getAllOrderCancelledByUserId(String userId);

    List<Order> getAllOrderSuccessByUserId(String userId);

    List<Order> getAllOrderByUserId(String userId);

    List<HistoryCustomerDto> getHistoryCustomer(String fullNameCustomer);

    List<HistoryCustomerDto> getHistoryCustomerPN(String fullNameCustomer);

    Page<Order> getAllPage(DataTableRequest request, String status, String dateFrom, String dateTo, EOrderType type);

    List<Order> getAllByCreatedBy(String createdBy);
}
