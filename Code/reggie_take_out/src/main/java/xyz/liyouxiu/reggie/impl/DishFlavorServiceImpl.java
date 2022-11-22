package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.DishFlavor;
import xyz.liyouxiu.reggie.mapper.DishFlavorMapper;
import xyz.liyouxiu.reggie.service.DishFlavorService;

/**
 * @author liyouxiu
 * @date 2022/11/22 15:51
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
