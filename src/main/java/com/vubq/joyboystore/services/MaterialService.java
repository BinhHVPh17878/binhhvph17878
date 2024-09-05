package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Material;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialService {

    List<Material> getAll();

    Page<Material> getAllPage(DataTableRequest request, String status);

    Material getById(String id);

    Material save(Material material);

    List<String> getAllByProductId(String productId);

    List<Material> getAllByIdIn(List<String> idIn);

    List<Material> getProductInStock(String productId, String materialId);

    List<Material> getAllStatusActive();
}
