package xyz.liyouxiu.reggie.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.Setmeal;
import xyz.liyouxiu.reggie.mapper.SetmealMapper;
import xyz.liyouxiu.reggie.service.SetmealService;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:21
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService{
}
