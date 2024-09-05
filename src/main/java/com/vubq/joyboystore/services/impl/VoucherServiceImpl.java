package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Size;
import com.vubq.joyboystore.entities.Voucher;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.VoucherRepository;
import com.vubq.joyboystore.services.VoucherService;
import com.vubq.joyboystore.utils.BaseSpecification;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.SearchCriteria;
import com.vubq.joyboystore.utils.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public Optional<Voucher> findById(String id) {
        return this.voucherRepository.findById(id);
    }

    @Override
    public Optional<Voucher> findByCode(String code) {
        return this.voucherRepository.findByCode(code);
    }

    @Override
    public Page<Voucher> getListOfVouchersByCriteria(DataTableRequest request) {
        request.setSortBy("createdAt");
        request.setSortDesc(true);
        PageRequest pageable = request.toPageable();
        BaseSpecification<Voucher> specCodeContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Voucher.Fields.code})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        return this.voucherRepository.findAll(Specification.where(specCodeContains), pageable);
    }

    @Override
    public Voucher save(Voucher voucher) {
        return this.voucherRepository.save(voucher);
    }

    @Override
    public Page<Voucher> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Voucher> specCodeContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Voucher.Fields.code})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Voucher> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Size.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.voucherRepository.findAll(Specification.where(specCodeContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Voucher getById(String id) {
        return this.voucherRepository.findById(id).orElse(null);
    }

    @Override
    public List<Voucher> getAllLikeCodeAndStillActive(String code, Date dateNow, EStatus status) {
        return this.voucherRepository.getAllLikeCodeAndStillActive(code, dateNow, status);
    }

    @Override
    public Voucher getByCode(String code) {
        return this.voucherRepository.findByCode(code).orElse(null);
    }
}
