package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query(value = "SELECT DISTINCT p.id FROM Product p JOIN ProductDetail pd ON p.id = pd.product.id WHERE pd.quantity > 0")
    List<String> getAllProductIdInOfStock();

    @Query(value = "SELECT p FROM Product p JOIN ProductDetail pd ON p.id = pd.product.id JOIN OrderDetail od ON od.productDetail.id = pd.id GROUP BY p.id ORDER BY SUM(od.quantity) DESC LIMIT 10")
    List<Product> getTopQuantitySellProduct();

    @Query(value = "SELECT p FROM Product p JOIN Feedback f ON p.id = f.productDetail.product.id GROUP BY p.id ORDER BY AVG(f.rate) DESC LIMIT 10")
    List<Product> getTopRateProduct();

    @Query(value = "SELECT p FROM Product p WHERE p.status = com.vubq.joyboystore.enums.EStatus.ACTIVE ORDER BY p.createdAt DESC LIMIT 5")
    List<Product> getTop5ProductCreatedAtDESC();
}
