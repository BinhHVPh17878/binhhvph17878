package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.entities.Size;
import com.vubq.joyboystore.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeDto {

    private String id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;
    private Boolean isOutOfStock;

    public static SizeDto toDto(Size color) {
        return SizeDto.builder()
                .id(color.getId())
                .name(color.getName())
                .description(color.getDescription())
                .status(color.getStatus())
                .createdAt(color.getCreatedAt())
                .build();
    }
}
