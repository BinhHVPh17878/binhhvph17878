package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    Page<Feedback> findAll(Specification<Feedback> spec, Pageable pageable);

    @Query(value = "SELECT AVG(f.rate) FROM Feedback f WHERE f.productDetail.product.id = :productId")
    Double getRateProduct(String productId);
}
