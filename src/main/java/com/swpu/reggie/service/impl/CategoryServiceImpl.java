package com.swpu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swpu.reggie.common.CustomException;
import com.swpu.reggie.domain.Category;
import com.swpu.reggie.domain.Dish;
import com.swpu.reggie.domain.Setmeal;
import com.swpu.reggie.mapper.CategoryMapper;
import com.swpu.reggie.service.CategoryService;
import com.swpu.reggie.service.DishService;
import com.swpu.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据id删除分类
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();

        dishQueryWrapper.eq(Dish::getCategoryId, id);

        int count1 = dishService.count(dishQueryWrapper);

        if (count1 > 0) {
            throw new CustomException("关联菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count2 = setMealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            throw new CustomException("关联套餐，不能删除");
        }

        super.removeById(id);

    }
}
