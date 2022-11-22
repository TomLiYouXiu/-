# 流程

## 开发流程

### 需求分析

产品原型，需求规格说明书

### 设计

产品文档，UI界面设计，概要设计，详细设计，数据库设计

### 编码

项目代码，单元测试

### 测试

测试用例，测试报告

### 上线运维

软件环境安转，配置

## 角色分工

* 项目经理
* 产品经理
* UI设计师
* 架构师
* 开发工程师
* 测试工程师
* 运维工程师

## 软件环境

开发环境

测试环境

生产环境

# 项目

## 项目介绍

![](https://pic.imgdb.cn/item/63733c8016f2c2beb1267bc5.jpg)

## 技术选型

![](https://pic.imgdb.cn/item/6373522a16f2c2beb147dda6.jpg)

## 功能架构

![](https://pic.imgdb.cn/item/637352e516f2c2beb148d924.jpg)

## 角色

![](https://pic.imgdb.cn/item/637353b016f2c2beb14a4b71.jpg)

# 开发

## 数据库

直接引入SQL文件，在资料/SQL中

![](https://pic.imgdb.cn/item/637355be16f2c2beb14e0641.jpg)

## maven

新建maven项目，导入pom文件

导入配置文件，设置启动项

## 前端

导入前端的代码

编写配置类访问静态资源

~~~java
package xyz.liyouxiu.reggie.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author liyouxiu
 * @date 2022/11/15 18:08
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射……");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

    }
}

~~~

## 业务功能开发

### 登录

创建Controller，Service，Mapper

因为用了MyBatisPlus所以，Service和ServiceImpl需要实现或者继承MyBatisPlus的接口和方法

~~~java
package xyz.liyouxiu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.liyouxiu.reggie.entity.Employee;

/**
 * @author liyouxiu
 * @date 2022/11/15 19:38
 */
public interface EmployeeService extends IService<Employee> {
}
~~~

~~~java
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
~~~

导入结果返回类

~~~java
package xyz.liyouxiu.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
~~~

**功能分析**

![](https://pic.imgdb.cn/item/63737f0816f2c2beb18cf7f0.jpg)

~~~java
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

}
~~~

**功能测试**

### 退出

![](https://pic.imgdb.cn/item/63738a2c16f2c2beb19f8346.jpg)

~~~java
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
~~~

### 完善登录功能

配置过滤器或者拦截器

![](https://pic.imgdb.cn/item/6374910c16f2c2beb1e4a307.jpg)

~~~java
package xyz.liyouxiu.reggie.filter;

/**
 * @author liyouxiu
 * @date 2022/11/16 15:15
 */

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import xyz.liyouxiu.reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检测用户是否完成登录
 */
//配置拦截器名称和拦截路径
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        //不需要处理的请求
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2.判断请求是否需要处理
        boolean check = check(urls, requestURI);
        //3.不需要处理则直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            filterChain.doFilter(request,response);
            return;
        }
        //5.如果没有登录返回未登录结果
        //通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


~~~

![](https://pic.imgdb.cn/item/63748e7516f2c2beb1e11a19.jpg)

**功能测试**

### 新增员工

![](https://pic.imgdb.cn/item/6374a0ba16f2c2beb1012769.jpg)

~~~java
/**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save (HttpServletRequest request,@RequestBody Employee employee){

        //设置初始密码，MD5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //其余信息设置
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的ID
        Long empId=(Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        log.info("新增员工，员工信息：{}",employee);
        return R.success("新增员工成功");
    }
~~~

### 全局异常捕获

~~~java
package xyz.liyouxiu.reggie.common;

/**
 * @author liyouxiu
 * @date 2022/11/16 17:02
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionsHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //Duplicate entry
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
~~~

### 员工信息分页查询

[![](https://pic.imgdb.cn/item/6374af6f16f2c2beb117f2b6.jpg)](https://pic.imgdb.cn/item/6374af6f16f2c2beb117f2b6.jpg)

**设置分页插件**

~~~java
package xyz.liyouxiu.reggie.config;

/**
 * @author liyouxiu
 * @date 2022/11/16 17:47
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置MP分页插件
 */
@Configuration
public abstract class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}

~~~

~~~java
 /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        pageInfo.setTotal(pageInfo.getRecords().size());
        return R.success(pageInfo);
    }
~~~

### 启用/禁用员工账号

**创建通用的员工修改信息的方法**

~~~java
/**
     * 根据ID修改员工的信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        log.info(employee.toString());
        //设置跟新时间和更新人
        employee.setUpdateTime(LocalDateTime.now());
        Long updateUser=(Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(updateUser);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
~~~

js处理Long类型的代码时会丢失精度，可以使用String类型的数据

使用组件SpringMVC的组件消息装换器

### 编辑员工信息

[![](https://pic.imgdb.cn/item/6378952916f2c2beb1fe6869.jpg)](https://pic.imgdb.cn/item/6378952916f2c2beb1fe6869.jpg)

~~~java
/**
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据ID查询员工信息");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工");
    }
~~~

### 公共字段自动填充

[![](https://pic.imgdb.cn/item/6379ce5a16f2c2beb118a95d.jpg)](https://pic.imgdb.cn/item/6379ce5a16f2c2beb118a95d.jpg)

 MybatisPlus公共字段自动填充，也就是在插入或者更新的时候为指定字段赋予指定的值，使用他的好处就是可以对字段进行统一的处理，避免了重复代码

[![](https://pic.imgdb.cn/item/6379d18316f2c2beb11e47fc.jpg)](https://pic.imgdb.cn/item/6379d18316f2c2beb11e47fc.jpg)

~~~java
package xyz.liyouxiu.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author liyouxiu
 * @date 2022/11/20 15:07
 * 自定义元素数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insertFill】");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",new Long(1));
        metaObject.setValue("updateUser",new Long(1));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【updateFill】");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(1));
    }
}

~~~

**进行代码完善**

[![](https://pic.imgdb.cn/item/6379d9bd16f2c2beb12bf324.jpg)](https://pic.imgdb.cn/item/6379d9bd16f2c2beb12bf324.jpg)

[![](https://pic.imgdb.cn/item/6379dc7016f2c2beb1300b6c.jpg)](https://pic.imgdb.cn/item/6379dc7016f2c2beb1300b6c.jpg)



### 新增分类

~~~java
 /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("saving category");
        categoryService.save(category);
        return R.success("新增分类成功");
    }
~~~

### 分类信息分页查询

~~~java
 /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page={},pageSize={}",page,pageSize);
        //构造分页构造器
        Page<Category> pageInfo=new Page(page,pageSize);
        //构条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        //添加排序条件
        queryWrapper.orderByDesc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        pageInfo.setTotal(pageInfo.getRecords().size());
        return R.success(pageInfo);
    }
~~~

### 删除分类

需要查看是否存在关联

[![](https://pic.imgdb.cn/item/637a28c216f2c2beb1acef42.jpg)](https://pic.imgdb.cn/item/637a28c216f2c2beb1acef42.jpg)

### 修改分类

直接Controller层进行控制即可

### 文件的上传与下载

[![](https://pic.imgdb.cn/item/637b3da016f2c2beb13a2400.jpg)](https://pic.imgdb.cn/item/637b3da016f2c2beb13a2400.jpg)

~~~java
package xyz.liyouxiu.reggie.controller;

/**
 * @author liyouxiu
 * @date 2022/11/22 10:25
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.liyouxiu.reggie.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * 文件的上传与下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件的上传
     * 文件名要和前台保持一致
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info("文件的上传{}",file.toString());
        //获取原始上传的文件名
        String originalFilename = file.getOriginalFilename();
        //使用UUID重写生成文件名称，防止文件名称重复造成文件覆盖
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建目录对象
        File dir=new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //不存在需要创建
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            byte[] bytes=new byte[1024];
            int len=0;
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

~~~

### 新增菜品

[![](https://pic.imgdb.cn/item/637c819716f2c2beb1f261bb.jpg)](https://pic.imgdb.cn/item/637c819716f2c2beb1f261bb.jpg)

[![](https://pic.imgdb.cn/item/637c955916f2c2beb1162032.jpg)](https://pic.imgdb.cn/item/637c955916f2c2beb1162032.jpg)

### 菜品分页

[![](https://pic.imgdb.cn/item/637cbfa816f2c2beb156d15a.jpg)](https://pic.imgdb.cn/item/637cbfa816f2c2beb156d15a.jpg)

**多表查询**

### 修改菜品

[![](https://pic.imgdb.cn/item/637ccf2d16f2c2beb17155d1.jpg)](https://pic.imgdb.cn/item/637ccf2d16f2c2beb17155d1.jpg)

类似添加？？

### 菜品管理（批量删除、起售停售）