package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.HistoryOder;

import java.util.List;

public interface HistoryOrderService {

    HistoryOder save(HistoryOder h);

    List<HistoryOder> findAllByOrderId(String orderId);
}
