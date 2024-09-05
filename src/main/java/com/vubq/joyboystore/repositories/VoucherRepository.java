package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {

    Optional<Voucher> findByCode(String code);

    Page<Voucher> findAll(Specification<Voucher> spec, Pageable pageable);

    @Query(value = "SELECT v FROM Voucher v WHERE v.code LIKE %:code% AND :dateNow <= v.endDate AND v.status = :status")
    List<Voucher> getAllLikeCodeAndStillActive(String code, Date dateNow, EStatus status);
}
