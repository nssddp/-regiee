package com.swpu.reggie.dto;


import com.swpu.reggie.domain.Setmeal;
import com.swpu.reggie.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
