package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.BrandRepository;
import com.vubq.joyboystore.services.BrandService;
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
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }

    @Override
    public Page<Brand> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Brand> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Brand.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Brand> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Brand.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.brandRepository.findAll(Specification.where(specNameContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Brand getById(String id) {
        return this.brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        return this.brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllStatusActive() {
        return this.brandRepository.findAllByStatus(EStatus.ACTIVE);
    }
}
