package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Size;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SizeService {

    List<Size> getAll();

    Page<Size> getAllPage(DataTableRequest request, String status);

    Size getById(String id);

    Size save(Size size);

    List<String> getAllByProductId(String productId);

    List<Size> getAllByIdIn(List<String> idIn);

    List<Size> getProductInStock(String productId, String sizeId);

    List<Size> getAllStatusActive();
}
