package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Material;
import com.vubq.joyboystore.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {

    Page<Material> findAll(Specification<Material> spec, Pageable pageable);

    @Query(value = "SELECT DISTINCT(m.id) FROM Material m JOIN ProductDetail pd ON m.id = pd.material.id WHERE pd.product.id = :productId")
    List<String> getAllByProductId(String productId);

    @Query(value = "SELECT m FROM Material m JOIN ProductDetail pd ON m.id = pd.material.id WHERE pd.product.id = :productId AND m.id = :materialId AND pd.quantity > 0")
    List<Material> getProductInStock(String productId, String materialId);

    List<Material> findAllByStatus(EStatus status);

    @Query(value = "SELECT m FROM Material m WHERE m.id IN :idIn")
    List<Material> getAllByIdIn(List<String> idIn);
}
