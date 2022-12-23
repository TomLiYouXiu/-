package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.ShoppingCart;
import xyz.liyouxiu.reggie.mapper.SetmealMapper;
import xyz.liyouxiu.reggie.mapper.ShoppingCartMapper;
import xyz.liyouxiu.reggie.service.ShoppingCartService;

/**
 * @author liyouxiu
 * @date 2022/12/21 18:10
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
