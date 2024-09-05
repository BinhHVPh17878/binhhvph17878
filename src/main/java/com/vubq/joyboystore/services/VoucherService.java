package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VoucherService {

    Optional<Voucher> findById(String id);

    Optional<Voucher> findByCode(String code);

    Page<Voucher> getListOfVouchersByCriteria(DataTableRequest request);

    Voucher save(Voucher voucher);

    Page<Voucher> getAllPage(DataTableRequest request, String status);

    Voucher getById(String id);

    List<Voucher> getAllLikeCodeAndStillActive(String code, Date dateNow, EStatus status);

    Voucher getByCode(String code);
}
