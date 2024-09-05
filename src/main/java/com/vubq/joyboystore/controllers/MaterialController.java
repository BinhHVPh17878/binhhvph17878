package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.MaterialDto;
import com.vubq.joyboystore.entities.Material;
import com.vubq.joyboystore.services.MaterialService;
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
@RequestMapping("/webapi/material")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Material> result = this.materialService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Material material = this.materialService.getById(id);
        MaterialDto dto = MaterialDto.builder()
                .id(material.getId())
                .name(material.getName())
                .description(material.getDescription())
                .status(material.getStatus())
                .createdAt(material.getCreatedAt())
                .updatedAt(material.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(material.getCreatedBy()) ? null : this.getUserNameByUserId(material.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(material.getUpdatedBy()) ? null : this.getUserNameByUserId(material.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody MaterialDto dto) {
        Material material = null;
        if (StringUtils.isEmpty(dto.getId())) {
            material = Material.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            material = this.materialService.getById(dto.getId());
            if (material == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy danh mục!");
            }
            material.setName(dto.getName());
            material.setDescription(dto.getDescription());
            material.setStatus(dto.getStatus());
            material.setUpdatedAt(new Date());
            material.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.materialService.save(material);
        return Response.build().ok();
    }

    @GetMapping("get-all-status-active")
    public Response getAllStatusActive() {
        return Response.build().ok().data(this.materialService.getAllStatusActive());
    }

    @GetMapping("get-all")
    public Response getAll() {
        return Response.build().ok().data(this.materialService.getAll());
    }
}
