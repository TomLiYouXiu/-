package xyz.liyouxiu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.liyouxiu.reggie.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}