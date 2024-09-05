package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.HistoryOder;
import com.vubq.joyboystore.repositories.HistoryOrderRepository;
import com.vubq.joyboystore.services.HistoryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryOrderServiceImpl implements HistoryOrderService {

    @Autowired
    private HistoryOrderRepository historyOrderRepository;

    @Override
    public HistoryOder save(HistoryOder h) {
        return this.historyOrderRepository.save(h);
    }

    @Override
    public List<HistoryOder> findAllByOrderId(String orderId) {
        return this.historyOrderRepository.findAllByOrderIdOrderByCreatedAtDesc(orderId);
    }
}
