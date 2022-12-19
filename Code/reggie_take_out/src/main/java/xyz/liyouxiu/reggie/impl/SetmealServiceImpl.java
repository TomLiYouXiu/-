package xyz.liyouxiu.reggie.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.liyouxiu.reggie.common.CustomException;
import xyz.liyouxiu.reggie.dto.SetmealDto;
import xyz.liyouxiu.reggie.entity.DishFlavor;
import xyz.liyouxiu.reggie.entity.Setmeal;
import xyz.liyouxiu.reggie.entity.SetmealDish;
import xyz.liyouxiu.reggie.mapper.SetmealMapper;
import xyz.liyouxiu.reggie.service.SetmealDishService;
import xyz.liyouxiu.reggie.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:21
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService{
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时需要保存套餐和菜品的对应关系
     * @param setmealDto
     */
    @Override
    @Transactional//保证数据的一致性
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        //保存套餐和菜品的关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        //

    }

    /**
     * 删除套餐同时删除菜品的关联数据
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐的状态查看是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<Setmeal>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if(count > 0){
            //若果不能删除，抛出异常信息
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper=new LambdaQueryWrapper<SetmealDish>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}
