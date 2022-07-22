package com.swpu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swpu.reggie.domain.Dish;
import com.swpu.reggie.dto.DishDto;

import java.util.List;


public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void removeWithStatus(List<Long> ids);

}
