package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Page<Category> getAllPage(DataTableRequest request, String status);

    Category getById(String id);

    Category save(Category category);

    List<Category> getAllStatusActive();
}
