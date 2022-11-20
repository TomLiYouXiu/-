package xyz.liyouxiu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.liyouxiu.reggie.entity.Category;

/**
 * @author liyouxiu
 * @date 2022/11/19 22:31
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据ID删除分类，删除之前需要进行判断
     * @param id
     */
    public void remove(Long id);
}
