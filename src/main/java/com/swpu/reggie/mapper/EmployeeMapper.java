package com.swpu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swpu.reggie.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
