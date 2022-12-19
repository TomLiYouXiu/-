package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.SetmealDish;
import xyz.liyouxiu.reggie.mapper.SetmealDishMapper;
import xyz.liyouxiu.reggie.service.SetmealDishService;

/**
 * @author liyouxiu
 * @date 2022/12/5 14:19
 */
@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService {
}
