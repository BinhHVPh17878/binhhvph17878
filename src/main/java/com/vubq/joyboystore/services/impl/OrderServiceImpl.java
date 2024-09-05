package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.dtos.HistoryCustomerDto;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EOrderStatus;
import com.vubq.joyboystore.enums.EOrderType;
import com.vubq.joyboystore.repositories.OrderRepository;
import com.vubq.joyboystore.services.OrderService;
import com.vubq.joyboystore.utils.BaseSpecification;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.SearchCriteria;
import com.vubq.joyboystore.utils.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public Page<Order> getListOfOrdersByCriteriaSellAtTheShop(DataTableRequest request, String dateFrom, String dateTo) {
        request.setSortBy("createdAt");
        request.setSortDesc(true);
        PageRequest pageable = request.toPageable();
        BaseSpecification<Order> specFullNameCustomerContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.fullName})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Order> specPhoneNumberContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.phoneNumber})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Order> specTypeEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.type})
                        .operation(SearchOperation.EQUALITY)
                        .value(EOrderType.AT_THE_SHOP)
                        .build());
        BaseSpecification<Order> specDateFromGreaterThanOrEqual = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.completedAt})
                        .operation(SearchOperation.GREATER_THAN_OR_EQUAL)
                        .value(dateFrom + " 00:00:00.000")
                        .build());
        BaseSpecification<Order> specDateToLessThanOrEqual = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.completedAt})
                        .operation(SearchOperation.LESS_THAN_OR_EQUAL)
                        .value(dateTo + " 23:59:59.999")
                        .build());
        return this.orderRepository.findAll(
                Specification
                        .where(specFullNameCustomerContains)
                        .or(specPhoneNumberContains)
                        .and(specTypeEquality)
                        .and(specDateFromGreaterThanOrEqual)
                        .and(specDateToLessThanOrEqual)
                , pageable);
    }

    @Override
    public List<Order> getAllOrderByUserId(String userId) {
        return this.orderRepository.getAllOrderByUserId(userId);
    }

//    @Override
//    public Page<Order> getListOfOrdersByCriteria(DataTableRequest request, OrderSearchForm orderSearchForm) {
//        request.setSortBy("createdAt");
//        request.setSortDesc(true);
//        PageRequest pageable = request.toPageable();
//        Specification specification = new Specification() {
//            @Override
//            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                if (orderSearchForm.getOrderStatus().size() > 0) {
//                    predicates.add(criteriaBuilder.and(root.get(Order.Fields.status).in(orderSearchForm.getOrderStatus())));
//                }
//                predicates.add(criteriaBuilder.equal(root.get(Order.Fields.type), EOrderType.ONLINE));
//                query.where(predicates.toArray(new Predicate[]{}));
//                return null;
//            }
//        };
//        return this.orderRepository.findAll(specification, pageable);
//    }

    @Override
    public Optional<Order> findById(String id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrderWaitForConfirmationByUserId(String userId) {
        return this.orderRepository.getAllOrderWaitForConfirmationByUserId(userId);
    }

    @Override
    public List<Order> getAllOrderCancelledByUserId(String userId) {
        return this.orderRepository.getAllOrderCancelledByUserId(userId);
    }

    @Override
    public List<Order> getAllOrderSuccessByUserId(String userId) {
        return this.orderRepository.getAllOrderSuccessByUserId(userId);
    }

    @Override
    public List<HistoryCustomerDto> getHistoryCustomer(String fullNameCustomer) {
        return this.orderRepository.getHistoryCustomer(fullNameCustomer);
    }

    @Override
    public List<HistoryCustomerDto> getHistoryCustomerPN(String phone) {
        return this.orderRepository.getHistoryCustomerPN(phone);
    }

    @Override
    public Page<Order> getAllPage(DataTableRequest request, String status, String dateFrom, String dateTo, EOrderType type) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Order> specFullNameCustomerContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.fullName})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Order> specPhoneNumberContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.phoneNumber})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Order> specTypeEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.type})
                        .operation(SearchOperation.EQUALITY)
                        .value(type)
                        .build());
        BaseSpecification<Order> specDateFromGreaterThanOrEqual = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.createdAt})
                        .operation(SearchOperation.GREATER_THAN_OR_EQUAL)
                        .value(dateFrom + " 00:00:00.000")
                        .build());
        BaseSpecification<Order> specDateToLessThanOrEqual = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.createdAt})
                        .operation(SearchOperation.LESS_THAN_OR_EQUAL)
                        .value(dateTo + " 23:59:59.999")
                        .build());
        BaseSpecification<Order> specStatus = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Order.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.orderRepository.findAll(
                Specification
                        .where(specFullNameCustomerContains)
                        .or(specPhoneNumberContains)
                        .and(specTypeEquality)
                        .and(specDateFromGreaterThanOrEqual)
                        .and(specDateToLessThanOrEqual)
                        .and(status.equals("ALL") ? null : specStatus)
                , pageable);
    }

    @Override
    public List<Order> getAllByCreatedBy(String createdBy) {
        return this.orderRepository.findAllByCreatedBy(createdBy);
    }
}
