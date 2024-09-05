package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.enums.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStatusDto {

    String id;
    EOrderStatus status;
}
