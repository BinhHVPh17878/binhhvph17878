package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.enums.EVoucherType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDto {

    private String id;
    private String code;
    private EVoucherType type;
    private Double value;
    private Double max;
    private Integer quantity;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private EStatus status;
}
