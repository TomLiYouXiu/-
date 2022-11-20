package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.Dish;
import xyz.liyouxiu.reggie.mapper.DishMapper;
import xyz.liyouxiu.reggie.service.DishService;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:24
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
