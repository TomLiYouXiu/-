package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.common.CustomException;
import xyz.liyouxiu.reggie.entity.Category;
import xyz.liyouxiu.reggie.entity.Dish;
import xyz.liyouxiu.reggie.entity.Setmeal;
import xyz.liyouxiu.reggie.mapper.CategoryMapper;
import xyz.liyouxiu.reggie.service.CategoryService;
import xyz.liyouxiu.reggie.service.DishService;
import xyz.liyouxiu.reggie.service.SetmealService;


/**
 * @author liyouxiu
 * @date 2022/11/19 22:33
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
   @Autowired
   private DishService dishService;

   @Autowired
   private SetmealService setmealService;
    /**
     * 根据ID删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类ID进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        //查询当前的分类是否关联了菜品，如果已经关联，跑出一个业务异常
        if(count>0){
            //已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前的分类是否关联了套餐，如果已经关联，跑出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        count = setmealService.count(setmealLambdaQueryWrapper);
        if(count>0){
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
