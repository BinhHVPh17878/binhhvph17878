package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {

    Page<Brand> findAll(Specification<Brand> spec, Pageable pageable);

    List<Brand> findAllByStatus(EStatus status);
}
