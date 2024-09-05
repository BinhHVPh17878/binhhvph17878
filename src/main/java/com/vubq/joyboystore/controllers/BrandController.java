package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.BrandDto;
import com.vubq.joyboystore.entities.Brand;
import com.vubq.joyboystore.services.BrandService;
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
@RequestMapping("/webapi/brand")
public class BrandController extends BaseController {

    @Autowired
    private BrandService brandService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Brand> result = this.brandService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Brand brand = this.brandService.getById(id);
        BrandDto dto = BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .status(brand.getStatus())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(brand.getCreatedBy()) ? null : this.getUserNameByUserId(brand.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(brand.getUpdatedBy()) ? null : this.getUserNameByUserId(brand.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody BrandDto dto) {
        Brand brand = null;
        if (StringUtils.isEmpty(dto.getId())) {
            brand = Brand.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            brand = this.brandService.getById(dto.getId());
            if (brand == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy thương hiệu!");
            }
            brand.setName(dto.getName());
            brand.setDescription(dto.getDescription());
            brand.setStatus(dto.getStatus());
            brand.setUpdatedAt(new Date());
            brand.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.brandService.save(brand);
        return Response.build().ok();
    }

    @GetMapping("get-all-status-active")
    public Response getAllStatusActive() {
        return Response.build().ok().data(this.brandService.getAllStatusActive());
    }

    @GetMapping("get-all")
    public Response getAll() {
        return Response.build().ok().data(this.brandService.getAll());
    }
}
