package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.CategoryRepository;
import com.vubq.joyboystore.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Category> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Category.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Category> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Category.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.categoryRepository.findAll(Specification.where(specNameContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Category getById(String id) {
        return this.categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllStatusActive() {
        return this.categoryRepository.findAllByStatus(EStatus.ACTIVE);
    }
}
