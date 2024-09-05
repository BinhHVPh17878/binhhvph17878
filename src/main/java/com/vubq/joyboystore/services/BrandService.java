package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    List<Brand> getAll();

    Page<Brand> getAllPage(DataTableRequest request, String status);

    Brand getById(String id);

    Brand save(Brand brand);

    List<Brand> getAllStatusActive();
}
