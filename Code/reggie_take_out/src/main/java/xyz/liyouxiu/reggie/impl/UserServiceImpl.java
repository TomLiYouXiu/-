package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.User;
import xyz.liyouxiu.reggie.mapper.UserMapper;
import xyz.liyouxiu.reggie.service.UserService;

/**
 * @author liyouxiu
 * @date 2022/12/10 16:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
