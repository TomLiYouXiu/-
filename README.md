# 瑞吉外卖
项目实训

# DAY1

* 环境的搭建
* 各个层的准备
* 数据库
* 简单业务的实现
  * 登入
    * 获取用户输入的账号和密码，通过MP进行比较（eq，返回一个员工存入Session）
  * 登出
    * 登出（删除Session，页面跳转）

# DAY2

* 业务实现

  * 完善登录功能（添加拦截器）
    * 1.创建自定义拦截器
    * 2.启动类上加入@ServletComponentScan
    * 3.完善处理器逻辑（静态资源不拦截，以及一些数据无法显示的页面）

* 新增员工

  * 页面发送ajax请求，将新增员工的数据以JSON的形式提交
  * Controller层直接使用MyBatisPlus进行数据库操作

* 全局异常捕获

  * 对例如数据库的异常进行处理
  * @ControllerAdvice设置错误

* 员工信息分页查询

  * 设置分页插件，注解@Configuration别忘了

  * ~~~java
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

* 启用/禁用员工账号

  * 设置了一个通用类进行数据的更新和账号启用（请求方式和MP基本一样）

* 编辑员工信息

  * 需要在原框中显示自己的原来的信息进行展示

  

# DAY3

* 公共字段自动填充

  *  MybatisPlus公共字段自动填充，也就是在插入或者更新的时候为指定字段赋予指定的值，使用他的好处就是可以对字段进行统一的处理，避免了重复代码

  * ![](https://pic.imgdb.cn/item/6379d18316f2c2beb11e47fc.jpg)

  * ~~~java
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

  * 进阶代码完善

    * ![](https://pic.imgdb.cn/item/6379dc7016f2c2beb1300b6c.jpg)

* 新增分类

  * 直接Controller层进行代码书写

* 分类信息分页查询

  * 直接Controller层进行代码书写

* 删除分类

  * 需要考虑删除时是否存在关联

    * ![](https://pic.imgdb.cn/item/637a28c216f2c2beb1acef42.jpg)

  * Service方法

    * ~~~java
      package xyz.liyouxiu.reggie.impl;
      
      import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
      import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Service;
      import xyz.liyouxiu.reggie.common.CustomException;
      import xyz.liyouxiu.reggie.entity.Category;
      import xyz.liyouxiu.reggie.entity.Dish;
      import xyz.liyouxiu.reggie.entity.Setmeal;
      import xyz.liyouxiu.reggie.mapper.CategoryMapper;
      import xyz.liyouxiu.reggie.service.CategoryService;
      import xyz.liyouxiu.reggie.service.DishService;
      import xyz.liyouxiu.reggie.service.SetmealService;
      
      
      /**
       * @author liyouxiu
       * @date 2022/11/19 22:33
       */
      @Service
      public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
         @Autowired
         private DishService dishService;
      
         @Autowired
         private SetmealService setmealService;
          /**
           * 根据ID删除分类，删除之前需要进行判断
           * @param id
           */
          @Override
          public void remove(Long id) {
              LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
              //添加查询条件，根据分类ID进行查询
              dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
              int count = dishService.count(dishLambdaQueryWrapper);
              //查询当前的分类是否关联了菜品，如果已经关联，跑出一个业务异常
              if(count>0){
                  //已经关联了菜品，抛出业务异常
                  throw new CustomException("当前分类下关联了菜品，不能删除");
              }
              //查询当前的分类是否关联了套餐，如果已经关联，跑出一个业务异常
              LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
              setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
              count = setmealService.count(setmealLambdaQueryWrapper);
              if(count>0){
                  //已经关联了套餐，抛出业务异常
                  throw new CustomException("当前分类下关联了套餐，不能删除");
              }
              //正常删除
              super.removeById(id);
          }
      }
      
      ~~~

  * 自定义异常（继承RuntimeException）

    * ~~~java
      package xyz.liyouxiu.reggie.common;
      
      /**
       * @author liyouxiu
       * @date 2022/11/20 21:42
       * 自定义业务异常
       */
      public class CustomException extends RuntimeException {
          public CustomException(String message){
              super(message);
          }
      }
      
      ~~~

  * 全局异常捕捉

    * ~~~java
       /**
           * 异常处理方法
           * @return
           */
          @ExceptionHandler(CustomException.class)
          public R<String> exceptionsHandler(CustomException ex){
              log.error(ex.getMessage());
              //Duplicate entry
              return R.error(ex.getMessage());
          }
      ~~~

  * Controller层的正常调用

* 修改分类

  * 直接Controller层进行控制即可

# DAY4

* 文件的上传与下载

  * ~~~java
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

  * MultipartFile主要是对这个方法的实现

* 新增菜品

  * 新增菜品是两个表的增加需要从写Service方法来实现从底层来说就是对两个表的操作融合到一个方法中

* 菜品的分页

  * 因为单个对象中并没有所有的属性此时引入了DTO

* 修改菜品

  * 类似新增



# 明天TODO

- [√] 菜品的批量删除
- [√] 菜品的批量禁售/起售
- [√] 套餐管理（修改、起售停售）
- [√] 订单明细
# 预览网址

[李优秀 - Homepage (liyouxiu.xyz)](http://liyouxiu.xyz/)
