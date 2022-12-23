package xyz.liyouxiu.reggie.dto;
import lombok.Data;
import xyz.liyouxiu.reggie.entity.Dish;
import xyz.liyouxiu.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
