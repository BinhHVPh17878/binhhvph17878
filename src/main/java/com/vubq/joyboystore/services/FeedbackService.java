package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Feedback;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

public interface FeedbackService {

    Page<Feedback> getListOfFeedbacksByCriteria(DataTableRequest request, String productId, Double rate);

    Double getRateProduct(String productId);
}
