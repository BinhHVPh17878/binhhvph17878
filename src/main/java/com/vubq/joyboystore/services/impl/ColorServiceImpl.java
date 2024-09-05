package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.ColorRepository;
import com.vubq.joyboystore.services.ColorService;
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
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> getAll() {
        return this.colorRepository.findAll();
    }

    @Override
    public Page<Color> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Color> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Color.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Color> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Color.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.colorRepository.findAll(Specification.where(specNameContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Color getById(String id) {
        return this.colorRepository.findById(id).orElse(null);
    }

    @Override
    public Color save(Color color) {
        return this.colorRepository.save(color);
    }

    @Override
    public List<String> getAllByProductId(String productId) {
        return this.colorRepository.getAllByProductId(productId);
    }

    @Override
    public List<Color> getProductInStock(String productId, String colorId) {
        return this.colorRepository.getProductInStock(productId, colorId);
    }

    @Override
    public List<Color> getAllStatusActive() {
        return this.colorRepository.findAllByStatus(EStatus.ACTIVE);
    }

    @Override
    public List<Color> getAllByIdIn(List<String> idIn) {
        return this.colorRepository.getAllByIdIn(idIn);
    }
}
