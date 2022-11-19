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

  