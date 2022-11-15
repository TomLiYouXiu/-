package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.Employee;
import xyz.liyouxiu.reggie.mapper.EmployeeMapper;
import xyz.liyouxiu.reggie.service.EmployeeService;

/**
 * @author liyouxiu
 * @date 2022/11/15 19:39
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}
