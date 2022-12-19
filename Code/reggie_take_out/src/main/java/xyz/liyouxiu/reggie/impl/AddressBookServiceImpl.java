package xyz.liyouxiu.reggie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.liyouxiu.reggie.entity.AddressBook;
import xyz.liyouxiu.reggie.mapper.AddressBookMapper;
import xyz.liyouxiu.reggie.service.AddressBookService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
