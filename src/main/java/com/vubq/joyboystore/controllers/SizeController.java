package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.SizeDto;
import com.vubq.joyboystore.entities.Size;
import com.vubq.joyboystore.services.SizeService;
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
@RequestMapping("/webapi/size")
public class SizeController extends BaseController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Size> result = this.sizeService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Size size = this.sizeService.getById(id);
        SizeDto dto = SizeDto.builder()
                .id(size.getId())
                .name(size.getName())
                .description(size.getDescription())
                .status(size.getStatus())
                .createdAt(size.getCreatedAt())
                .updatedAt(size.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(size.getCreatedBy()) ? null : this.getUserNameByUserId(size.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(size.getUpdatedBy()) ? null : this.getUserNameByUserId(size.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody SizeDto dto) {
        Size size = null;
        if (StringUtils.isEmpty(dto.getId())) {
            size = Size.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            size = this.sizeService.getById(dto.getId());
            if (size == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy danh mục!");
            }
            size.setName(dto.getName());
            size.setDescription(dto.getDescription());
            size.setStatus(dto.getStatus());
            size.setUpdatedAt(new Date());
            size.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.sizeService.save(size);
        return Response.build().ok();
    }

    @GetMapping("get-all-status-active")
    public Response getAllStatusActive() {
        return Response.build().ok().data(this.sizeService.getAllStatusActive());
    }

    @GetMapping("get-all")
    public Response getAll() {
        return Response.build().ok().data(this.sizeService.getAll());
    }
}