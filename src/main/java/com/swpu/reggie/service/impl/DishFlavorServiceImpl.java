package com.swpu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swpu.reggie.domain.DishFlavor;
import com.swpu.reggie.mapper.DishFlavorMapper;
import com.swpu.reggie.service.DishFlavorService;
import com.swpu.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
