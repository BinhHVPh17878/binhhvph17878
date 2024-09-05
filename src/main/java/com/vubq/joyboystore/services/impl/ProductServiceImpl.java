package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.entities.Product;
import com.vubq.joyboystore.repositories.ProductRepository;
import com.vubq.joyboystore.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> getListOfProductsByCriteria(DataTableRequest request, String status) {
        request.setSortBy("createdAt");
        request.setSortDesc(true);
        PageRequest pageable = request.toPageable();
        BaseSpecification<Product> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Product> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        return this.productRepository.findAll(Specification.where(specNameContains).and(specStatusEquality), pageable);
    }

    @Override
    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public Product getById(String id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> getAllProductIdInOfStock() {
        return this.productRepository.getAllProductIdInOfStock();
    }

    @Override
    public Page<Product> getAllPage(
            DataTableRequest request,
            String status,
            String brandId,
            String categoryId,
            String sizeId,
            String colorId,
            String materialId,
            Double minPrice,
            Double maxPrice
    ) {
        PageRequest pageable = request.toPageable();
        BaseSpecification<Product> specNameContains = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.name})
                        .operation(SearchOperation.CONTAINS)
                        .value(request.getFilter().trim().toUpperCase())
                        .build());
        BaseSpecification<Product> specStatusEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{Product.Fields.status})
                        .operation(SearchOperation.EQUALITY)
                        .value(status)
                        .build());
        BaseSpecification<Product> specCategoryIdEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{"category", Category.Fields.id})
                        .operation(SearchOperation.EQUALITY)
                        .value(categoryId)
                        .build());
        BaseSpecification<Product> specBrandIdEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{"brand", Brand.Fields.id})
                        .operation(SearchOperation.EQUALITY)
                        .value(brandId)
                        .build());
        BaseSpecification<Product> specColorIdEquality = new BaseSpecification<>(
                SearchCriteria.builder()
                        .keys(new String[]{"productDetails", "color", Color.Fields.id})
                        .operation(SearchOperation.EQUALITY)
                        .value(colorId)
                        .build());
        return this.productRepository.findAll(
                Specification.where(specNameContains)
                        .and(status.equals("ALL") ? null : specStatusEquality)
                        .and(categoryId.equals("ALL") ? null : specCategoryIdEquality)
                        .and(brandId.equals("ALL") ? null : specBrandIdEquality)
                        .and(colorId.equals("ALL") ? null : specColorIdEquality)
                , pageable);
    }

    @Override
    public List<Product> getTop5ProductCreatedAtDESC() {
        return this.productRepository.getTop5ProductCreatedAtDESC();
    }
}
