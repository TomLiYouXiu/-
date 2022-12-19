package xyz.liyouxiu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.liyouxiu.reggie.entity.User;

/**
 * @author liyouxiu
 * @date 2022/12/10 16:56
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
