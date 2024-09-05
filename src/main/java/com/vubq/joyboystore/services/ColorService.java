package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ColorService {

    List<Color> getAll();

    Page<Color> getAllPage(DataTableRequest request, String status);

    Color getById(String id);

    Color save(Color color);

    List<String> getAllByProductId(String productId);

    List<Color> getAllByIdIn(List<String> idIn);

    List<Color> getProductInStock(String productId, String colorId);

    List<Color> getAllStatusActive();
}
