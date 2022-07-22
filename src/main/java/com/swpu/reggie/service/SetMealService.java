package com.swpu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swpu.reggie.domain.Setmeal;
import com.swpu.reggie.dto.SetmealDto;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
