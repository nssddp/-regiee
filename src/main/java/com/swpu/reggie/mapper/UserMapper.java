package com.swpu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swpu.reggie.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
