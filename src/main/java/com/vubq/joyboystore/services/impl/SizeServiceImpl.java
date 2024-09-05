package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Size;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.SizeRepository;
import com.vubq.joyboystore.services.SizeService;
import com.vubq.joyboystore.utils.BaseSpecification;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.SearchCriteria;
import com.vubq.joyboystore.utils.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAll() {
        return this.sizeRepository.findAll();
    }

    @Override
    public Page<Size> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Size> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Size.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Size> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Size.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.sizeRepository.findAll(Specification.where(specNameContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Size getById(String id) {
        return this.sizeRepository.findById(id).orElse(null);
    }

    @Override
    public Size save(Size size) {
        return this.sizeRepository.save(size);
    }

    @Override
    public List<String> getAllByProductId(String productId) {
        return this.sizeRepository.getAllByProductId(productId);
    }

    @Override
    public List<Size> getProductInStock(String productId, String sizeId) {
        return this.sizeRepository.getProductInStock(productId, sizeId);
    }

    @Override
    public List<Size> getAllStatusActive() {
        return this.sizeRepository.findAllByStatus(EStatus.ACTIVE);
    }

    @Override
    public List<Size> getAllByIdIn(List<String> idIn) {
        return this.sizeRepository.getAllByIdIn(idIn);
    }
}
