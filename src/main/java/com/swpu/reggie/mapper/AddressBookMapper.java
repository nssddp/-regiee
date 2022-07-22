package com.swpu.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swpu.reggie.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
