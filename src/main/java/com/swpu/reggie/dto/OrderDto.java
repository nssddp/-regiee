package com.swpu.reggie.dto;

import com.swpu.reggie.domain.OrderDetail;
import com.swpu.reggie.domain.Orders;
import lombok.Data;
import org.springframework.core.annotation.Order;
import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;

}
