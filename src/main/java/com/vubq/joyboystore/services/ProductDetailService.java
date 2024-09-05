package com.vubq.joyboystore.services;

import com.vubq.joyboystore.dtos.FilterProductAttributeDTO;
import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.entities.ProductDetail;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductDetailService {

    ProductDetail getById(String id);

    Page<ProductDetail> getAllBySearchCriteria(DataTableRequest dataTableRequest);

    Page<ProductDetail> getListOfProductDetailsByCriteriaWebShop(
            DataTableRequest dataTableRequest,
            List<String> categories,
            List<String> sizes,
            List<String> colors,
            List<String> materials,
            List<String> brands,
            List<Double> priceApprox);

    ProductDetail save(ProductDetail productDetail);

    List<ProductDetail> getAllByProductId(String productId);

    List<String> getAllSizeIdByProductId(String productId);

    List<String> getAllColorIdByProductId(String productId);

    List<String> getAllMaterialIdByProductId(String productId);

    List<ProductDetail> getAllProductDetailInOfStock();

    List<ProductDetail> saveAll(List<ProductDetail> productDetails);

    Integer getQuantityOfProductAvailable(String productId);

    List<ProductDetail> filterProductAttributes(FilterProductAttributeDTO filterProductAttributeDTO);

    ProductDetail getProductDetailByAttributes(String productId, String sizeId, String colorId, String materialId);

    Page<ProductDetail> getAllPage(
            DataTableRequest dataTableRequest,
            String status,
            String brandId,
            String categoryId,
            String sizeId,
            String colorId,
            String materialId,
            Double minPrice,
            Double maxPrice
    );

    List<ProductDetail> getAllByByStatusActiveAndQuantityGreaterThan0AndSearchBy(String searchBy);

    List<ProductDetail> getAllByIdIn(List<String> idIn);

    List<ProductDetail> getAllProductDetailView(String productId);
}
