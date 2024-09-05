package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {

    Page<ProductDetail> findAll(Specification<ProductDetail> spec, Pageable pageable);

    List<ProductDetail> findAll(Specification<ProductDetail> spec);

    @Query(value = "SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<ProductDetail> findAllByProductId(String productId);

    @Query(value = "SELECT DISTINCT pd.size.id FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<String> findAllSizeIdByProductId(String productId);

    @Query(value = "SELECT DISTINCT pd.color.id FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<String> findAllColorIdByProductId(String productId);

    @Query(value = "SELECT DISTINCT pd.material.id FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<String> findAllMaterilIdByProductId(String productId);

    @Query(value = "SELECT pd FROM ProductDetail pd WHERE pd.quantity > 0")
    List<ProductDetail> getAllProductDetailInOfStock();

    @Query(value = "SELECT DISTINCT pd.size.id FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.quantity > 0")
    List<String> findAllSizeIdByProductIdInOfStock(String productId);

    @Query(value = "SELECT DISTINCT pd.color.id FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.quantity > 0")
    List<String> findAllColorIdByProductIdInOfStock(String productId);

    @Query(value = "SELECT DISTINCT pd.material.id FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.quantity > 0")
    List<String> findAllMaterialIdByProductIdInOfStock(String productId);

    @Query(value = "SELECT SUM(pd.quantity) FROM ProductDetail pd WHERE pd.product.id = :productId")
    Integer getQuantityOfProductAvailable(String productId);

    @Query(value = "SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.id = :productId " +
            "AND pd.size.id = :sizeId " +
            "AND pd.color.id = :colorId " +
            "AND pd.material.id = :materialId")
    ProductDetail getProductDetailByAttributes(String productId, String sizeId, String colorId, String materialId);

    @Query(value = "SELECT pd FROM ProductDetail pd " +
            "JOIN Product p " +
            "ON p.id = pd.product.id " +
            "WHERE p.status = com.vubq.joyboystore.enums.EStatus.ACTIVE " +
            "AND pd.quantity > 0 AND p.name LIKE %:searchBy%")
    List<ProductDetail> getAllByByStatusActiveAndQuantityGreaterThan0AndSearchBy(String searchBy);

    @Query(value = "SELECT pd FROM ProductDetail pd WHERE pd.id IN :idIn")
    List<ProductDetail> getAllByIdIn(List<String> idIn);

    @Query(value = "SELECT pd FROM ProductDetail pd " +
            "WHERE pd.product.id = :productId " +
            "AND pd.status = com.vubq.joyboystore.enums.EStatus.ACTIVE " +
            "AND pd.product.status = com.vubq.joyboystore.enums.EStatus.ACTIVE " +
            "AND pd.quantity > 0")
    List<ProductDetail> getAllProductDetailView(String productId);
}
