package xyz.liyouxiu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.liyouxiu.reggie.entity.Employee;

/**
 * @author liyouxiu
 * @date 2022/11/15 19:37
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
