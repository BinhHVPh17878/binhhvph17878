package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.CategoryDto;
import com.vubq.joyboystore.entities.Category;
import com.vubq.joyboystore.services.CategoryService;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Category> result = this.categoryService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Category category = this.categoryService.getById(id);
        CategoryDto dto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(category.getCreatedBy()) ? null : this.getUserNameByUserId(category.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(category.getUpdatedBy()) ? null : this.getUserNameByUserId(category.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody CategoryDto dto) {
        Category category = null;
        if (StringUtils.isEmpty(dto.getId())) {
            category = Category.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            category = this.categoryService.getById(dto.getId());
            if (category == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy danh mục!");
            }
            category.setName(dto.getName());
            category.setDescription(dto.getDescription());
            category.setStatus(dto.getStatus());
            category.setUpdatedAt(new Date());
            category.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.categoryService.save(category);
        return Response.build().ok();
    }

    @GetMapping("get-all-status-active")
    public Response getAllStatusActive() {
        return Response.build().ok().data(this.categoryService.getAllStatusActive());
    }

    @GetMapping("get-all")
    public Response getAll() {
        return Response.build().ok().data(this.categoryService.getAll());
    }
}
