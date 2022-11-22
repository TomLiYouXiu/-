package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.liyouxiu.reggie.dto.DishDto;
import xyz.liyouxiu.reggie.entity.Dish;
import xyz.liyouxiu.reggie.entity.DishFlavor;
import xyz.liyouxiu.reggie.mapper.DishMapper;
import xyz.liyouxiu.reggie.service.DishFlavorService;
import xyz.liyouxiu.reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:24
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long disId = dishDto.getId();//菜品ID
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(disId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据ID插叙菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavor = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(flavor);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);
        //清理当前提交过来的口味表
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);
        //添加口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
