package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Product;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<Product> getListOfProductsByCriteria(DataTableRequest dataTableRequest, String status);

//    Page<Product> getListOfProductsByCriteriaWebShop(DataTableRequest dataTableRequest, List<String> categories, List<String> sizes, List<String> colors, List<String> materials);

    Product save(Product product);

    Product getById(String id);

    Page<Product> getAllPage(
            DataTableRequest request,
            String status,
            String brandId,
            String categoryId,
            String sizeId,
            String colorId,
            String materialId,
            Double minPrice,
            Double maxPrice
    );

    List<String> getAllProductIdInOfStock();

    List<Product> getTop5ProductCreatedAtDESC();
}
