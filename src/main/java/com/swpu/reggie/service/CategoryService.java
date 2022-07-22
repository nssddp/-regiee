package com.swpu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swpu.reggie.domain.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
