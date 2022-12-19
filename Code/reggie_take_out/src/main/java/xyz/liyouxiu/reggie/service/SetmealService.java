package xyz.liyouxiu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.liyouxiu.reggie.dto.SetmealDto;
import xyz.liyouxiu.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:20
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的对应关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐同时删除菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);



}
