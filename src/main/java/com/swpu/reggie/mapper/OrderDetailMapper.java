package com.swpu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swpu.reggie.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
