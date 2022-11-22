package xyz.liyouxiu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.liyouxiu.reggie.dto.DishDto;
import xyz.liyouxiu.reggie.entity.Dish;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:19
 */
public interface DishService extends IService<Dish> {
    //新增菜品同时插入菜品对应的口味数据，需要操作两个表
    public void saveWithFlavor(DishDto dishDto);

    //根据ID插叙菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息
    public void updateWithFlavor(DishDto dishDto);
}
