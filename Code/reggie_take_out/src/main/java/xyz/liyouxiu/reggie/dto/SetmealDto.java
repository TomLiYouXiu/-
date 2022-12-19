package xyz.liyouxiu.reggie.dto;

import lombok.Data;
import xyz.liyouxiu.reggie.entity.Setmeal;
import xyz.liyouxiu.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
