package xyz.liyouxiu.reggie.dto;
import lombok.Data;
import xyz.liyouxiu.reggie.entity.Dish;
import xyz.liyouxiu.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
