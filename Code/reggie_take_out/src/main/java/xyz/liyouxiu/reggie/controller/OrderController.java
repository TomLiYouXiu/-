package xyz.liyouxiu.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.liyouxiu.reggie.common.R;
import xyz.liyouxiu.reggie.entity.Employee;
import xyz.liyouxiu.reggie.entity.Orders;
import xyz.liyouxiu.reggie.service.OrderService;


/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String id){
        log.info("page={},pageSize={},name={}",page,pageSize,id);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构条件构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(id),Orders::getId,id);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        //执行查询
        orderService.page(pageInfo,queryWrapper);
        pageInfo.setTotal(pageInfo.getRecords().size());
        return R.success(pageInfo);
    }
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        log.info("page={},pageSize={},name={}",page,pageSize);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构条件构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        //执行查询
        orderService.page(pageInfo,queryWrapper);
        pageInfo.setTotal(pageInfo.getRecords().size());
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        LambdaQueryWrapper<Orders> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getId,orders.getId());
        orderService.update(orders,lambdaQueryWrapper);
        return R.success("状态更新成功");
    }
}