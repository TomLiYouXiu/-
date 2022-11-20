package xyz.liyouxiu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.liyouxiu.reggie.entity.Dish;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:17
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
