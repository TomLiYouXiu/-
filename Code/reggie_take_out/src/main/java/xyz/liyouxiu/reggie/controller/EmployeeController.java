package xyz.liyouxiu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import xyz.liyouxiu.reggie.common.R;
import xyz.liyouxiu.reggie.entity.Employee;
import xyz.liyouxiu.reggie.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liyouxiu
 * @date 2022/11/15 19:41
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行MD5加密
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //eq mp中的比较方法
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        //getOne唯一的数据
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到返回登录失败结果
        if(emp==null){
            return R.error("登录失败");
        }
        //4.密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用
        if(emp.getStatus()==0){
            return R.error("员工已禁用");
        }
        //6.登录成功，将员工的ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1.清理Session中保存的当前登录的员工ID
        request.getSession().removeAttribute("employee");
        //2.返回结果
        return R.success("退出成功");
    }
}
