package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private String id;
    private String publicId;
    private String url;
    private EImageType type;
    private String secondaryId;
}
