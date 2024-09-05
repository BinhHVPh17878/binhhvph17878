package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.ColorDto;
import com.vubq.joyboystore.entities.Color;
import com.vubq.joyboystore.services.ColorService;
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
@RequestMapping("/webapi/color")
public class ColorController extends BaseController {

    @Autowired
    private ColorService colorService;

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam(value = "status") String status) {
        Page<Color> result = this.colorService.getAllPage(request, status);
        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.getContent());
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable(value = "id") String id) {
        Color color = this.colorService.getById(id);
        ColorDto dto = ColorDto.builder()
                .id(color.getId())
                .name(color.getName())
                .description(color.getDescription())
                .status(color.getStatus())
                .createdAt(color.getCreatedAt())
                .updatedAt(color.getUpdatedAt())
                .createdBy(StringUtils.isEmpty(color.getCreatedBy()) ? null : this.getUserNameByUserId(color.getCreatedBy()))
                .updatedBy(StringUtils.isEmpty(color.getUpdatedBy()) ? null : this.getUserNameByUserId(color.getUpdatedBy()))
                .build();
        return Response.build().ok().data(dto);
    }

    @PostMapping
    public Response createOrUpdate(@RequestBody ColorDto dto) {
        Color color = null;
        if (StringUtils.isEmpty(dto.getId())) {
            color = Color.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .createdAt(new Date())
                    .createdBy(this.getTheCurrentlyLoggedInUserId())
                    .build();
        } else {
            color = this.colorService.getById(dto.getId());
            if (color == null) {
                return Response.build().code(Response.CODE_INTERNAL_ERROR).data("Không tìm thấy danh mục!");
            }
            color.setName(dto.getName());
            color.setDescription(dto.getDescription());
            color.setStatus(dto.getStatus());
            color.setUpdatedAt(new Date());
            color.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
        }
        this.colorService.save(color);
        return Response.build().ok();
    }

    @GetMapping("get-all-status-active")
    public Response getAllStatusActive() {
        return Response.build().ok().data(this.colorService.getAllStatusActive());
    }

    @GetMapping("get-all")
    public Response getAll() {
        return Response.build().ok().data(this.colorService.getAll());
    }
}
