package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.HistoryOder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryOrderRepository extends JpaRepository<HistoryOder, String> {

    List<HistoryOder> findAllByOrderIdOrderByCreatedAtDesc(String orderId);
}
