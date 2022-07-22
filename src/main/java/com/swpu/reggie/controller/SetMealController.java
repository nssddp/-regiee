package com.swpu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swpu.reggie.common.R;
import com.swpu.reggie.domain.Category;
import com.swpu.reggie.domain.Dish;
import com.swpu.reggie.domain.Setmeal;
import com.swpu.reggie.domain.SetmealDish;
import com.swpu.reggie.dto.DishDto;
import com.swpu.reggie.dto.SetmealDto;
import com.swpu.reggie.service.CategoryService;
import com.swpu.reggie.service.DishService;
import com.swpu.reggie.service.SetMealDishService;
import com.swpu.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<Boolean> save(@RequestBody SetmealDto setmealDto) {

        setMealService.saveWithDish(setmealDto);

        return R.success(true);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {}, name = {}", page, pageSize, name);

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> SetMealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(name != null, Setmeal::getName, name);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setMealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, SetMealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;

        }).collect(Collectors.toList());

        SetMealDtoPage.setRecords(list);

        return R.success(SetMealDtoPage);
    }

    /**
     * 停售和起售功能
     *
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<Boolean> changeStatus(@RequestParam List<Long> ids, @PathVariable String status) {

        int i = 0;
        Setmeal setmeal;
        if (status.equals("1")) {
            while (ids.size() >= i + 1) {
                setmeal = setMealService.getById(ids.get(i));
                setmeal.setStatus(1);
                setMealService.updateById(setmeal);
                i++;
            }
        } else {
            while (ids.size() >= i + 1) {
                setmeal = setMealService.getById(ids.get(i));
                setmeal.setStatus(0);
                setMealService.updateById(setmeal);
                i++;
            }
        }

        return R.success(true);
    }


    /**
     * 删除功能
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam List<Long> ids) {

        setMealService.removeWithDish(ids);

        return R.success(true);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {

        SetmealDto setmealDto = setMealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }

    /***
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<Boolean> update(@RequestBody SetmealDto setmealDto) {

        setMealService.updateWithDish(setmealDto);

        return R.success(true);
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setMealService.list(queryWrapper);

        return R.success(list);
    }

    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable String id) {

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setMealDishService.list(queryWrapper);

        List<DishDto> detail = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long dishId = item.getDishId();

            Dish dish = dishService.getById(dishId);

            if (dish != null){
                String imgSrc = dish.getImage();
                dishDto.setImage(imgSrc);
            }

            return dishDto;
        }).collect(Collectors.toList());

        /*LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setMealDishService.list(queryWrapper);
*/
        return R.success(detail);
    }


}
