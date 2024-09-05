package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Feedback;
import com.vubq.joyboystore.repositories.FeedbackRepository;
import com.vubq.joyboystore.services.FeedbackService;
import com.vubq.joyboystore.utils.DataTableRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Double getRateProduct(String productId) {
        Double rate = this.feedbackRepository.getRateProduct(productId);
        return rate == null ? 0 : rate;
    }

    @Override
    public Page<Feedback> getListOfFeedbacksByCriteria(DataTableRequest request, String productId, Double rate) {
        request.setSortBy("createdAt");
        request.setSortDesc(true);
        PageRequest pageable = request.toPageable();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("productDetail").get("product").get("id"), productId)));
                if (rate != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("rate"), rate)));
                }
                query.where(predicates.toArray(new Predicate[]{}));
                return null;
            }
        };
        return this.feedbackRepository.findAll(specification, pageable);
    }
}
