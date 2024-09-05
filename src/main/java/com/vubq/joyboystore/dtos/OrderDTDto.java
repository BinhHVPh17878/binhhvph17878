package com.vubq.joyboystore.dtos;

import com.vubq.joyboystore.entities.HistoryOder;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.OrderDetail;
import com.vubq.joyboystore.entities.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTDto {

    private Order order;
    private List<OrderDetail> listOrderDetail;
    private List<HistoryOder> listHistoryOrder;
    private Voucher voucher;
    private List<ListImageDto> listImage;
}
