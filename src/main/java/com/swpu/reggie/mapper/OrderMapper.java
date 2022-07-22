package com.swpu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swpu.reggie.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
