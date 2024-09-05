package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Material;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.repositories.MaterialRepository;
import com.vubq.joyboystore.services.MaterialService;
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
import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public List<Material> getAll() {
        return this.materialRepository.findAll();
    }

    @Override
    public Page<Material> getAllPage(DataTableRequest request, String status) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Material> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Material.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Material> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Material.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.materialRepository.findAll(Specification.where(specNameContains).and(status.equals("ALL") ? null : specStatusEquality), pageable);
    }

    @Override
    public Material getById(String id) {
        return this.materialRepository.findById(id).orElse(null);
    }

    @Override
    public Material save(Material material) {
        return this.materialRepository.save(material);
    }

    @Override
    public List<String> getAllByProductId(String productId) {
        return this.materialRepository.getAllByProductId(productId);
    }

    @Override
    public List<Material> getProductInStock(String productId, String materialId) {
        return this.materialRepository.getProductInStock(productId, materialId);
    }

    @Override
    public List<Material> getAllStatusActive() {
        return this.materialRepository.findAllByStatus(EStatus.ACTIVE);
    }

    @Override
    public List<Material> getAllByIdIn(List<String> idIn) {
        return this.materialRepository.getAllByIdIn(idIn);
    }
}
