package com.swpu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swpu.reggie.domain.Orders;
import com.swpu.reggie.dto.OrderDto;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);

    OrderDto getByIdWithDetail(Long id);
}
